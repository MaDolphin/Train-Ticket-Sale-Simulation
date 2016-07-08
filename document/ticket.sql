
CREATE TABLE [dbo].[人员信息] (
[姓名] nvarchar(255) NOT NULL,
[身份证号] nvarchar(255) PRIMARY KEY,
[权限] int NOT NULL
)

CREATE TABLE [dbo].[列车信息] (
[车次] nvarchar(255) PRIMARY KEY,
[类型] nvarchar(255) CHECK(类型='动车'or 类型='高铁') NOT NULL,
[ 始发站] nvarchar(255) NOT NULL,
[ 终点站] nvarchar(255) NOT NULL,
[发车时间] time(0) NOT NULL,
[到达时间] time(0) NOT NULL,
[ 历时] time(0) NOT NULL
)

CREATE TABLE [dbo].[列车详情] (
[车次] nvarchar(255) NOT NULL,
[站次] int NOT NULL,
[车站名称] nvarchar(255),
[里程] int,
[到达时间] time(0),
[停留时间] time(0),
[运行时间] time(0),
[一等座价格] money,
[二等座价格] money,
PRIMARY KEY(车次,站次)
)

CREATE TABLE [dbo].[座位信息] (
[车次] nvarchar(255) NOT NULL,
[车厢号] int NOT NULL,
[座位号] nvarchar(255) NOT NULL,
[座位类型] nvarchar(255) NOT NULL,
PRIMARY KEY(车次,车厢号,座位号)
)

CREATE TABLE [dbo].[车站信息] (
[车站编号] nvarchar(255) NOT NULL,
[车站名称] nvarchar(255) PRIMARY KEY,
)

CREATE TABLE [dbo].[购买信息] (
[车次] nvarchar(255) NOT NULL,
[发车日期] smalldatetime NOT NULL,
[车厢号] int NOT NULL,
[座位号] nvarchar(255) NOT NULL,
[购买日期] smalldatetime NOT NULL,
[购买点] nvarchar(255),
[身份证号] nvarchar(255) NOT NULL,
[出发站] nvarchar(255) NOT NULL,
[到达站] nvarchar(255) NOT NULL,
PRIMARY KEY(车次,车厢号,座位号,身份证号)
)

CREATE TABLE [dbo].[剩余座位信息](
[车次] [nvarchar](55) NOT NULL,
[发车日期] [nvarchar](55) NOT NULL,
[站次] [int] NOT NULL,
[一等座空余] [int] NULL,
[二等座空余] [int] NULL,
PRIMARY KEY(车次,发车日期,站次)
)

alter table 列车详情
add constraint FK__列车详情__车站名称  foreign key (车站名称) references 车站信息 (车站名称),
	constraint FK__列车详情__车次  foreign key (车次) references 列车信息 (车次),
	constraint CK__列车详情__里程 check (里程>=0),
	constraint CK__列车详情__价格 check (一等座价格>=0 or 二等座价格>=0)

alter table 座位信息
add constraint CK__座位信息__座位类型 check (座位类型='一等座' or 座位类型='二等座'),
	constraint FK__座位信息__车次  foreign key (车次) references 列车信息 (车次)

alter table 人员信息
add constraint CK__人员信息__权限 check (权限=1 or 权限=0)

alter table 购买信息
add constraint FK__购买信息__身份证号 foreign key (身份证号) references 人员信息 (身份证号),
	constraint FK__购买信息__车次 foreign key (车次) references 列车信息 (车次),
	constraint CK__购买信息__购买日期 default getdate() for 购买日期

alter table 剩余座位信息
add constraint CK__剩余座位信息__空余座位 check (一等座空余>=0 or 二等座空余>=0)

create nonclustered index Index__列车信息__类型 on 列车信息(类型)
create nonclustered index Index__列车详情__车站名称 on 列车详情(车站名称)
create nonclustered index Index__购买信息__发车日期 on 购买信息(发车日期)
create nonclustered index Index__购买信息__出发站 on 购买信息(出发站)
create nonclustered index Index__购买信息__到达站 on 购买信息(到达站)
create nonclustered index Index__人员信息__姓名 on 人员信息(姓名)

--查询列车信息(出发站目的地)
if exists (select name from sysobjects where name='getTrain_dist' and type='P')
	drop procedure getTrain_dist
go
create procedure getTrain_dist
	@start nvarchar(255),@end nvarchar(255)
as
	if OBJECT_ID('tempdb..#temp') is not null
		drop table #temp
	set nocount on 
	create table #temp(车次 nvarchar(255),类型 nvarchar(255),发站 nvarchar(255),到站 nvarchar(255),发车时间 time(0),到达时间 time(0),运行时间 int,一等座价格 float,二等座价格 float)
	declare num cursor for
	(select distinct 车次 
	from 列车详情
	where 车站名称 in (@start) 
		and 车次 in (select distinct 车次 
					from 列车详情
					where 车站名称 in (@end))
	)
	open num
	declare @t nvarchar(255)
	fetch next from num into @t	
	while @@fetch_status=0
	begin
		if((select distinct 站次 from 列车详情 where 车次 = @t and 车站名称 = @start)< 
			(select distinct 站次 from 列车详情 where 车次 = @t and 车站名称 = @end))
		begin
			insert #temp
			select 列车详情.车次,类型,@start as 发站,@end as 到站,
			(select 到达时间 from 列车详情 where 车次=@t and 车站名称=@start) as 发车时间,
			(select 到达时间 from 列车详情 where 车次=@t and 车站名称=@end) as 到达时间,
			datediff(minute,(select 到达时间 from 列车详情 where 车次=@t and 车站名称=@start),
				(select 到达时间 from 列车详情 where 车次=@t and 车站名称=@end)) as 运行时间,
			(select 一等座价格 from 列车详情 where 车次=@t and 车站名称=@end)-(select 一等座价格 from 列车详情 where 车次=@t and 车站名称=@start) as 一等座价格,
			(select 二等座价格 from 列车详情 where 车次=@t and 车站名称=@end)-(select 二等座价格 from 列车详情 where 车次=@t and 车站名称=@start) as 二等座价格
			from 列车详情 inner join 列车信息 on 列车详情.车次=列车信息.车次
			group by 列车详情.车次,类型
			having 列车详情.车次=@t		
		end
		fetch next from num into @t
	end 
	close num
	deallocate num
	select * from #temp
	truncate table #temp
	drop table #temp
	
--execute getTrain_dist @start='上海',@end='昆山南'

--查询空余座位（出发地目的地)
if exists (select name from sysobjects where name='getTrain_freeseat' and type='P')
	drop procedure getTrain_freeseat
go
create procedure getTrain_freeseat
	@start nvarchar(255),@end nvarchar(255),@starttime nvarchar(255)
as
	if OBJECT_ID('tempdb..#temp') is not null
		drop table #temp
	set nocount on 
	create table #temp(车次 nvarchar(255),发车日期 date,一等座空余 int,二等座空余 int)
	declare num1 cursor for
	(select distinct 车次 
	from 列车详情
	where 车站名称 in (@start) 
		and 车次 in (select distinct 车次 
					from 列车详情
					where 车站名称 in (@end))
	)
	open num1
	declare @t nvarchar(255)
	fetch next from num1 into @t	
	while @@fetch_status=0
	begin
		insert #temp
		select distinct 车次,发车日期,min(一等座空余) as 一等座剩余,min(二等座空余) as 二等座剩余
		from 剩余座位信息
		group by 车次,发车日期,站次
		having 站次>=(select 站次 from 列车详情 where 车站名称=@start and 车次=@t)
			and 站次<(select 站次 from 列车详情 where 车站名称=@end and 车次=@t)
			and 车次=@t	
			and 发车日期=@starttime
		fetch next from num1 into @t
	end 
	close num1
	deallocate num1
	select 车次,发车日期,min(一等座空余) as 一等座空余,min(二等座空余) as 二等座空余 from #temp
	group by 车次,发车日期
	truncate table #temp
	drop table #temp
	
--execute getTrain_freeseat @start='上海',@end='昆山南',@starttime='2015.6.1'


--订票
if exists (select name from sysobjects where name='setTrain_ticket' and type='P')
	drop procedure setTrain_ticket
go
create procedure setTrain_ticket
	@num nvarchar(55),@starttime date,@buystation nvarchar(255),
	@id nvarchar(55),@start nvarchar(255),@end nvarchar(255),@type nvarchar(255),@flag int output
as
	if (select count(*) from 购买信息 where 车次=@num and 发车日期=@starttime and 身份证号=@id)<1
	begin
		if OBJECT_ID('tempdb..#temp') is not null
		drop table #temp
		set nocount on 
		create table #temp(数量 int)
		declare @carriagenum int,@seatnum nvarchar(55),@price money,@price_start money,@price_end money,@t int
		if @type='一等座'
		begin
			select @price_start=一等座价格 from 列车详情 where 车次=@num and 车站名称=@start
			select @price_end=一等座价格 from 列车详情 where 车次=@num and 车站名称=@end
			set @price = @price_end - @price_start
			insert #temp
			select 一等座空余
			from 剩余座位信息
			group by 车次,发车日期,站次,一等座空余
			having 站次>=(select 站次 from 列车详情 where 车站名称=@start and 车次=@num)
			and 站次<(select 站次 from 列车详情 where 车站名称=@end and 车次=@num)
			and 车次=@num
			and 发车日期=@starttime
		end
		else
		begin
			
			select @price_start=二等座价格 from 列车详情 where 车次=@num and 车站名称=@start
			select @price_end=二等座价格 from 列车详情 where 车次=@num and 车站名称=@end
			set @price = @price_end - @price_start
			insert #temp
			select 二等座空余
			from 剩余座位信息
			group by 车次,发车日期,站次,二等座空余
			having 站次>=(select 站次 from 列车详情 where 车站名称=@start and 车次=@num)
			and 站次<(select 站次 from 列车详情 where 车站名称=@end and 车次=@num)
			and 车次=@num
			and 发车日期=@starttime
		end
		select @carriagenum=车厢号,@seatnum=座位号
		from 座位信息
		where 车次=@num 
			and 座位类型=@type
			and (车厢号 not in (select 车厢号 from 购买信息 where 车次=@num and 发车日期=@starttime)
				or 座位号 not in (select 座位号 from 购买信息 where 车次=@num and 发车日期=@starttime))
		select @t=min(数量) from #temp
		truncate table #temp
		drop table #temp
		if(@t>0)
		begin
			insert
			into 购买信息
			values (@num,@starttime,@carriagenum,@seatnum,getdate(),@buystation,@id,@start,@end,@price)
			set @flag=1
		end
		else
		set @flag=2
	end
	else
	set @flag=0
execute setTrain_ticket @num='D282',@starttime='2015.6.2',@type='一等座',
		@buystation='网络',@id='1',@start='上海虹桥',@end='常州北'

--退票
if exists (select name from sysobjects where name='setTrain_ticket_off' and type='P')
	drop procedure setTrain_ticket_off
go
create procedure setTrain_ticket_off
	@num nvarchar(55),@starttime date,@id nvarchar(55),@flag int output
as
	set @flag=0
	delete
	from 购买信息
	where 车次=@num and 发车日期=@starttime and 身份证号=@id
	set @flag=1

execute setTrain_ticket_off @num='D282',@starttime='2015.6.1',@id='310135199412094566'

--订票触发器
if exists (select name from sysobjects where name='tr_freeseat' and type='TR')
	drop trigger tr_freeseat
go
create trigger tr_freeseat
	on 购买信息
	for insert
as
	begin transaction
		declare @num nvarchar(55),@carriagenum int,@seatnum nvarchar(55),@starttime date,
				@start nvarchar(255),@end nvarchar(255),@level nvarchar(255)
		select @start=出发站,@end=到达站,@num=车次,@carriagenum=车厢号,@seatnum=座位号,@starttime=发车日期
		from inserted
		select @level=座位类型
		from 座位信息
		where 车次=@num and 车厢号=@carriagenum and 座位号=@seatnum
		if @level='一等座'
		begin
			update 剩余座位信息 set 一等座空余=一等座空余-1
			where 站次>=(select 站次 from 列车详情 where 车站名称=@start and 车次=@num)
				and 站次<(select 站次 from 列车详情 where 车站名称=@end and 车次=@num)
				and 车次=@num
				and 发车日期=@starttime
		end
		else 
		begin
			update 剩余座位信息 set 二等座空余=二等座空余-1
			where 站次>=(select 站次 from 列车详情 where 车站名称=@start and 车次=@num)
				and 站次<(select 站次 from 列车详情 where 车站名称=@end and 车次=@num)
				and 车次=@num
				and 发车日期=@starttime
		end
	commit tran

--退票触发器
if exists (select name from sysobjects where name='tr_freeseat_off' and type='TR')
	drop trigger tr_freeseat_off
go
create trigger tr_freeseat_off
	on 购买信息
	for delete
as
	begin transaction
		declare @num nvarchar(55),@carriagenum int,@seatnum nvarchar(55),@starttime date,
				@start nvarchar(255),@end nvarchar(255),@level nvarchar(255)
		select @start=出发站,@end=到达站,@num=车次,@carriagenum=车厢号,@seatnum=座位号,@starttime=发车日期
		from deleted
		select @level=座位类型
		from 座位信息
		where 车次=@num and 车厢号=@carriagenum and 座位号=@seatnum
		if @level='一等座'
		begin
			update 剩余座位信息 set 一等座空余=一等座空余+1
			where 站次>=(select 站次 from 列车详情 where 车站名称=@start and 车次=@num)
				and 站次<(select 站次 from 列车详情 where 车站名称=@end and 车次=@num)
				and 车次=@num
				and 发车日期=@starttime
		end
		else 
		begin
			update 剩余座位信息 set 二等座空余=二等座空余+1
			where 站次>=(select 站次 from 列车详情 where 车站名称=@start and 车次=@num)
				and 站次<(select 站次 from 列车详情 where 车站名称=@end and 车次=@num)
				and 车次=@num
				and 发车日期=@starttime
		end
	commit tran

--增加用户和管理员信息
if exists (select name from sysobjects where name='setPerson_info' and type='P')
	drop procedure setPerson_infot
go
create procedure setPerson_info
	@id nvarchar(55),@name nvarchar(255),@level int
as
	insert
	into 人员信息
	values (@name,@id,@level)

execute setPerson_info @name='李四',@id='310100197002012345',@level=0

--车票视图
create view s_buyinfo
as 
	select 姓名,人员信息.身份证号,车次,发车日期,车厢号,座位号,出发站,到达站
	from 购买信息 right join 人员信息 on 购买信息.身份证号=人员信息.身份证号

--按车站查询
if exists (select name from sysobjects where name='getTrain_station' and type='P')
	drop procedure getTrain_station
go
create procedure getTrain_station
	@station nvarchar(255)
as
	if OBJECT_ID('tempdb..#temp') is not null
		drop table #temp
	set nocount on 
	create table #temp(车次 nvarchar(255),始发站 nvarchar(255),终点站 nvarchar(255),里程 int,到达时间 time(0),停留时间 time(0),运行时间 time(0),一等座价格 money,二等座价格 money)
	declare num2 cursor for
	(select distinct 车次 from 列车详情 where 车站名称=@station)
	open num2
	declare @t nvarchar(255)
	fetch next from num2 into @t	
	while @@fetch_status=0
	begin
		insert #temp
		select 列车详情.车次,始发站,终点站,里程,列车详情.到达时间,停留时间,运行时间,一等座价格,二等座价格
		from 列车详情 inner join 列车信息 on 列车详情.车次=列车信息.车次
		group by 列车详情.车次,车站名称,始发站,终点站,里程,列车详情.到达时间,停留时间,运行时间,一等座价格,二等座价格
		having 列车详情.车次=@t and 车站名称=@station
		fetch next from num2 into @t
	end
	close num2
	deallocate num2
	select * from #temp
	truncate table #temp
	drop table #temp

execute getTrain_station @station='上海'


create datebase ticket