
CREATE TABLE [dbo].[��Ա��Ϣ] (
[����] nvarchar(255) NOT NULL,
[���֤��] nvarchar(255) PRIMARY KEY,
[Ȩ��] int NOT NULL
)

CREATE TABLE [dbo].[�г���Ϣ] (
[����] nvarchar(255) PRIMARY KEY,
[����] nvarchar(255) CHECK(����='����'or ����='����') NOT NULL,
[ ʼ��վ] nvarchar(255) NOT NULL,
[ �յ�վ] nvarchar(255) NOT NULL,
[����ʱ��] time(0) NOT NULL,
[����ʱ��] time(0) NOT NULL,
[ ��ʱ] time(0) NOT NULL
)

CREATE TABLE [dbo].[�г�����] (
[����] nvarchar(255) NOT NULL,
[վ��] int NOT NULL,
[��վ����] nvarchar(255),
[���] int,
[����ʱ��] time(0),
[ͣ��ʱ��] time(0),
[����ʱ��] time(0),
[һ�����۸�] money,
[�������۸�] money,
PRIMARY KEY(����,վ��)
)

CREATE TABLE [dbo].[��λ��Ϣ] (
[����] nvarchar(255) NOT NULL,
[�����] int NOT NULL,
[��λ��] nvarchar(255) NOT NULL,
[��λ����] nvarchar(255) NOT NULL,
PRIMARY KEY(����,�����,��λ��)
)

CREATE TABLE [dbo].[��վ��Ϣ] (
[��վ���] nvarchar(255) NOT NULL,
[��վ����] nvarchar(255) PRIMARY KEY,
)

CREATE TABLE [dbo].[������Ϣ] (
[����] nvarchar(255) NOT NULL,
[��������] smalldatetime NOT NULL,
[�����] int NOT NULL,
[��λ��] nvarchar(255) NOT NULL,
[��������] smalldatetime NOT NULL,
[�����] nvarchar(255),
[���֤��] nvarchar(255) NOT NULL,
[����վ] nvarchar(255) NOT NULL,
[����վ] nvarchar(255) NOT NULL,
PRIMARY KEY(����,�����,��λ��,���֤��)
)

CREATE TABLE [dbo].[ʣ����λ��Ϣ](
[����] [nvarchar](55) NOT NULL,
[��������] [nvarchar](55) NOT NULL,
[վ��] [int] NOT NULL,
[һ��������] [int] NULL,
[����������] [int] NULL,
PRIMARY KEY(����,��������,վ��)
)

alter table �г�����
add constraint FK__�г�����__��վ����  foreign key (��վ����) references ��վ��Ϣ (��վ����),
	constraint FK__�г�����__����  foreign key (����) references �г���Ϣ (����),
	constraint CK__�г�����__��� check (���>=0),
	constraint CK__�г�����__�۸� check (һ�����۸�>=0 or �������۸�>=0)

alter table ��λ��Ϣ
add constraint CK__��λ��Ϣ__��λ���� check (��λ����='һ����' or ��λ����='������'),
	constraint FK__��λ��Ϣ__����  foreign key (����) references �г���Ϣ (����)

alter table ��Ա��Ϣ
add constraint CK__��Ա��Ϣ__Ȩ�� check (Ȩ��=1 or Ȩ��=0)

alter table ������Ϣ
add constraint FK__������Ϣ__���֤�� foreign key (���֤��) references ��Ա��Ϣ (���֤��),
	constraint FK__������Ϣ__���� foreign key (����) references �г���Ϣ (����),
	constraint CK__������Ϣ__�������� default getdate() for ��������

alter table ʣ����λ��Ϣ
add constraint CK__ʣ����λ��Ϣ__������λ check (һ��������>=0 or ����������>=0)

create nonclustered index Index__�г���Ϣ__���� on �г���Ϣ(����)
create nonclustered index Index__�г�����__��վ���� on �г�����(��վ����)
create nonclustered index Index__������Ϣ__�������� on ������Ϣ(��������)
create nonclustered index Index__������Ϣ__����վ on ������Ϣ(����վ)
create nonclustered index Index__������Ϣ__����վ on ������Ϣ(����վ)
create nonclustered index Index__��Ա��Ϣ__���� on ��Ա��Ϣ(����)

--��ѯ�г���Ϣ(����վĿ�ĵ�)
if exists (select name from sysobjects where name='getTrain_dist' and type='P')
	drop procedure getTrain_dist
go
create procedure getTrain_dist
	@start nvarchar(255),@end nvarchar(255)
as
	if OBJECT_ID('tempdb..#temp') is not null
		drop table #temp
	set nocount on 
	create table #temp(���� nvarchar(255),���� nvarchar(255),��վ nvarchar(255),��վ nvarchar(255),����ʱ�� time(0),����ʱ�� time(0),����ʱ�� int,һ�����۸� float,�������۸� float)
	declare num cursor for
	(select distinct ���� 
	from �г�����
	where ��վ���� in (@start) 
		and ���� in (select distinct ���� 
					from �г�����
					where ��վ���� in (@end))
	)
	open num
	declare @t nvarchar(255)
	fetch next from num into @t	
	while @@fetch_status=0
	begin
		if((select distinct վ�� from �г����� where ���� = @t and ��վ���� = @start)< 
			(select distinct վ�� from �г����� where ���� = @t and ��վ���� = @end))
		begin
			insert #temp
			select �г�����.����,����,@start as ��վ,@end as ��վ,
			(select ����ʱ�� from �г����� where ����=@t and ��վ����=@start) as ����ʱ��,
			(select ����ʱ�� from �г����� where ����=@t and ��վ����=@end) as ����ʱ��,
			datediff(minute,(select ����ʱ�� from �г����� where ����=@t and ��վ����=@start),
				(select ����ʱ�� from �г����� where ����=@t and ��վ����=@end)) as ����ʱ��,
			(select һ�����۸� from �г����� where ����=@t and ��վ����=@end)-(select һ�����۸� from �г����� where ����=@t and ��վ����=@start) as һ�����۸�,
			(select �������۸� from �г����� where ����=@t and ��վ����=@end)-(select �������۸� from �г����� where ����=@t and ��վ����=@start) as �������۸�
			from �г����� inner join �г���Ϣ on �г�����.����=�г���Ϣ.����
			group by �г�����.����,����
			having �г�����.����=@t		
		end
		fetch next from num into @t
	end 
	close num
	deallocate num
	select * from #temp
	truncate table #temp
	drop table #temp
	
--execute getTrain_dist @start='�Ϻ�',@end='��ɽ��'

--��ѯ������λ��������Ŀ�ĵ�)
if exists (select name from sysobjects where name='getTrain_freeseat' and type='P')
	drop procedure getTrain_freeseat
go
create procedure getTrain_freeseat
	@start nvarchar(255),@end nvarchar(255),@starttime nvarchar(255)
as
	if OBJECT_ID('tempdb..#temp') is not null
		drop table #temp
	set nocount on 
	create table #temp(���� nvarchar(255),�������� date,һ�������� int,���������� int)
	declare num1 cursor for
	(select distinct ���� 
	from �г�����
	where ��վ���� in (@start) 
		and ���� in (select distinct ���� 
					from �г�����
					where ��վ���� in (@end))
	)
	open num1
	declare @t nvarchar(255)
	fetch next from num1 into @t	
	while @@fetch_status=0
	begin
		insert #temp
		select distinct ����,��������,min(һ��������) as һ����ʣ��,min(����������) as ������ʣ��
		from ʣ����λ��Ϣ
		group by ����,��������,վ��
		having վ��>=(select վ�� from �г����� where ��վ����=@start and ����=@t)
			and վ��<(select վ�� from �г����� where ��վ����=@end and ����=@t)
			and ����=@t	
			and ��������=@starttime
		fetch next from num1 into @t
	end 
	close num1
	deallocate num1
	select ����,��������,min(һ��������) as һ��������,min(����������) as ���������� from #temp
	group by ����,��������
	truncate table #temp
	drop table #temp
	
--execute getTrain_freeseat @start='�Ϻ�',@end='��ɽ��',@starttime='2015.6.1'


--��Ʊ
if exists (select name from sysobjects where name='setTrain_ticket' and type='P')
	drop procedure setTrain_ticket
go
create procedure setTrain_ticket
	@num nvarchar(55),@starttime date,@buystation nvarchar(255),
	@id nvarchar(55),@start nvarchar(255),@end nvarchar(255),@type nvarchar(255),@flag int output
as
	if (select count(*) from ������Ϣ where ����=@num and ��������=@starttime and ���֤��=@id)<1
	begin
		if OBJECT_ID('tempdb..#temp') is not null
		drop table #temp
		set nocount on 
		create table #temp(���� int)
		declare @carriagenum int,@seatnum nvarchar(55),@price money,@price_start money,@price_end money,@t int
		if @type='һ����'
		begin
			select @price_start=һ�����۸� from �г����� where ����=@num and ��վ����=@start
			select @price_end=һ�����۸� from �г����� where ����=@num and ��վ����=@end
			set @price = @price_end - @price_start
			insert #temp
			select һ��������
			from ʣ����λ��Ϣ
			group by ����,��������,վ��,һ��������
			having վ��>=(select վ�� from �г����� where ��վ����=@start and ����=@num)
			and վ��<(select վ�� from �г����� where ��վ����=@end and ����=@num)
			and ����=@num
			and ��������=@starttime
		end
		else
		begin
			
			select @price_start=�������۸� from �г����� where ����=@num and ��վ����=@start
			select @price_end=�������۸� from �г����� where ����=@num and ��վ����=@end
			set @price = @price_end - @price_start
			insert #temp
			select ����������
			from ʣ����λ��Ϣ
			group by ����,��������,վ��,����������
			having վ��>=(select վ�� from �г����� where ��վ����=@start and ����=@num)
			and վ��<(select վ�� from �г����� where ��վ����=@end and ����=@num)
			and ����=@num
			and ��������=@starttime
		end
		select @carriagenum=�����,@seatnum=��λ��
		from ��λ��Ϣ
		where ����=@num 
			and ��λ����=@type
			and (����� not in (select ����� from ������Ϣ where ����=@num and ��������=@starttime)
				or ��λ�� not in (select ��λ�� from ������Ϣ where ����=@num and ��������=@starttime))
		select @t=min(����) from #temp
		truncate table #temp
		drop table #temp
		if(@t>0)
		begin
			insert
			into ������Ϣ
			values (@num,@starttime,@carriagenum,@seatnum,getdate(),@buystation,@id,@start,@end,@price)
			set @flag=1
		end
		else
		set @flag=2
	end
	else
	set @flag=0
execute setTrain_ticket @num='D282',@starttime='2015.6.2',@type='һ����',
		@buystation='����',@id='1',@start='�Ϻ�����',@end='���ݱ�'

--��Ʊ
if exists (select name from sysobjects where name='setTrain_ticket_off' and type='P')
	drop procedure setTrain_ticket_off
go
create procedure setTrain_ticket_off
	@num nvarchar(55),@starttime date,@id nvarchar(55),@flag int output
as
	set @flag=0
	delete
	from ������Ϣ
	where ����=@num and ��������=@starttime and ���֤��=@id
	set @flag=1

execute setTrain_ticket_off @num='D282',@starttime='2015.6.1',@id='310135199412094566'

--��Ʊ������
if exists (select name from sysobjects where name='tr_freeseat' and type='TR')
	drop trigger tr_freeseat
go
create trigger tr_freeseat
	on ������Ϣ
	for insert
as
	begin transaction
		declare @num nvarchar(55),@carriagenum int,@seatnum nvarchar(55),@starttime date,
				@start nvarchar(255),@end nvarchar(255),@level nvarchar(255)
		select @start=����վ,@end=����վ,@num=����,@carriagenum=�����,@seatnum=��λ��,@starttime=��������
		from inserted
		select @level=��λ����
		from ��λ��Ϣ
		where ����=@num and �����=@carriagenum and ��λ��=@seatnum
		if @level='һ����'
		begin
			update ʣ����λ��Ϣ set һ��������=һ��������-1
			where վ��>=(select վ�� from �г����� where ��վ����=@start and ����=@num)
				and վ��<(select վ�� from �г����� where ��վ����=@end and ����=@num)
				and ����=@num
				and ��������=@starttime
		end
		else 
		begin
			update ʣ����λ��Ϣ set ����������=����������-1
			where վ��>=(select վ�� from �г����� where ��վ����=@start and ����=@num)
				and վ��<(select վ�� from �г����� where ��վ����=@end and ����=@num)
				and ����=@num
				and ��������=@starttime
		end
	commit tran

--��Ʊ������
if exists (select name from sysobjects where name='tr_freeseat_off' and type='TR')
	drop trigger tr_freeseat_off
go
create trigger tr_freeseat_off
	on ������Ϣ
	for delete
as
	begin transaction
		declare @num nvarchar(55),@carriagenum int,@seatnum nvarchar(55),@starttime date,
				@start nvarchar(255),@end nvarchar(255),@level nvarchar(255)
		select @start=����վ,@end=����վ,@num=����,@carriagenum=�����,@seatnum=��λ��,@starttime=��������
		from deleted
		select @level=��λ����
		from ��λ��Ϣ
		where ����=@num and �����=@carriagenum and ��λ��=@seatnum
		if @level='һ����'
		begin
			update ʣ����λ��Ϣ set һ��������=һ��������+1
			where վ��>=(select վ�� from �г����� where ��վ����=@start and ����=@num)
				and վ��<(select վ�� from �г����� where ��վ����=@end and ����=@num)
				and ����=@num
				and ��������=@starttime
		end
		else 
		begin
			update ʣ����λ��Ϣ set ����������=����������+1
			where վ��>=(select վ�� from �г����� where ��վ����=@start and ����=@num)
				and վ��<(select վ�� from �г����� where ��վ����=@end and ����=@num)
				and ����=@num
				and ��������=@starttime
		end
	commit tran

--�����û��͹���Ա��Ϣ
if exists (select name from sysobjects where name='setPerson_info' and type='P')
	drop procedure setPerson_infot
go
create procedure setPerson_info
	@id nvarchar(55),@name nvarchar(255),@level int
as
	insert
	into ��Ա��Ϣ
	values (@name,@id,@level)

execute setPerson_info @name='����',@id='310100197002012345',@level=0

--��Ʊ��ͼ
create view s_buyinfo
as 
	select ����,��Ա��Ϣ.���֤��,����,��������,�����,��λ��,����վ,����վ
	from ������Ϣ right join ��Ա��Ϣ on ������Ϣ.���֤��=��Ա��Ϣ.���֤��

--����վ��ѯ
if exists (select name from sysobjects where name='getTrain_station' and type='P')
	drop procedure getTrain_station
go
create procedure getTrain_station
	@station nvarchar(255)
as
	if OBJECT_ID('tempdb..#temp') is not null
		drop table #temp
	set nocount on 
	create table #temp(���� nvarchar(255),ʼ��վ nvarchar(255),�յ�վ nvarchar(255),��� int,����ʱ�� time(0),ͣ��ʱ�� time(0),����ʱ�� time(0),һ�����۸� money,�������۸� money)
	declare num2 cursor for
	(select distinct ���� from �г����� where ��վ����=@station)
	open num2
	declare @t nvarchar(255)
	fetch next from num2 into @t	
	while @@fetch_status=0
	begin
		insert #temp
		select �г�����.����,ʼ��վ,�յ�վ,���,�г�����.����ʱ��,ͣ��ʱ��,����ʱ��,һ�����۸�,�������۸�
		from �г����� inner join �г���Ϣ on �г�����.����=�г���Ϣ.����
		group by �г�����.����,��վ����,ʼ��վ,�յ�վ,���,�г�����.����ʱ��,ͣ��ʱ��,����ʱ��,һ�����۸�,�������۸�
		having �г�����.����=@t and ��վ����=@station
		fetch next from num2 into @t
	end
	close num2
	deallocate num2
	select * from #temp
	truncate table #temp
	drop table #temp

execute getTrain_station @station='�Ϻ�'


create datebase ticket