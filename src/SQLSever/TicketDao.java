package SQLSever;
import java.sql.*;
public class TicketDao {
	private String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";  //����JDBC����
	private String url = "jdbc:sqlserver://localhost:1433; DatabaseName=ticket";  //���ӷ����������ݿ�
	private String userName = "sa";  //Ĭ���û���
	private String userPwd = "291910";  //����
	private void executeUpdate(String sql) {
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, userName, userPwd);
			Statement cmd = con.createStatement();
			cmd.executeUpdate(sql);
			con.close(); 
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public boolean isExistPerson(String id,String pwd,int level){
		int flag=-1;
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, userName, userPwd);
			Statement cmd = con.createStatement();
			String sql = "select count(���֤��) from ��Ա��Ϣ  where ���֤�� = '"+id+"' and Ȩ��='"+level+"' and ����='"+pwd+"'";
			ResultSet rs = cmd.executeQuery(sql);
			rs.next();
			flag=rs.getInt(1);
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if(flag==1){
			return true ;
		}else return false;
	}
	
	public boolean addPerson(String name,String id,int level,String pwd){
		int flag=-1;
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, userName, userPwd);
			Statement cmd = con.createStatement();
			String sql = "select count(���֤��) from ��Ա��Ϣ  where ���֤�� = '"+id+"' ";
			ResultSet rs = cmd.executeQuery(sql);
			rs.next();
			flag=rs.getInt(1);
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if(flag==0){
			String sql = "insert into ��Ա��Ϣ(����,���֤��,Ȩ��,����) values('"+name+"','"+id+"','"+level+"','"+pwd+"')";
			executeUpdate(sql);
			return true ;
		}else return false;
	}
	
	public int addTicket(String num,String starttime,String buystation,String id,String start,String end,String type){
		int flag=-1;
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, userName, userPwd);
			CallableStatement c=conn.prepareCall("{call setTrain_ticket(?,?,?,?,?,?,?,?)}");
			c.setString("num",num);
			c.setString("starttime",starttime);
			c.setString("buystation",buystation);
			c.setString("id",id);
			c.setString("start",start);
			c.setString("end",end);
			c.setString("type",type);
			c.registerOutParameter(8,java.sql.Types.INTEGER);
			c.execute();
			flag=c.getInt(8);
			c.close();
	        conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return flag;
	}
	
	public boolean delTicket(String num,String starttime,String id){
		int flag=-1;
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, userName, userPwd);
			CallableStatement c=conn.prepareCall("{call setTrain_ticket_off(?,?,?,?)}");
			c.setString("num",num);
			c.setString("starttime",starttime);
			c.setString("id",id);
			c.registerOutParameter(4,java.sql.Types.INTEGER);
			c.execute();
			flag=c.getInt(4);
			c.close();
	        conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if(flag==1){
			return true ;
		}else return false;
	}
	
	public String[][] queryStartEnd(String start,String end,String starttime){
		String[][] rows = null;
		int rowcount=0;
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, userName, userPwd);
			CallableStatement c=con.prepareCall("{call getTrain_dist(?,?)}");
			c.setString("start",start);
			c.setString("end",end);
			ResultSet rs=c.executeQuery();
			while(rs.next())
				rowcount++;
			rows = new String[rowcount][12];
			c=con.prepareCall("{call getTrain_dist(?,?)}");
			c.setString("start",start);
			c.setString("end",end);
			rs=c.executeQuery();
			rowcount=0;
	        while(rs.next())
	        {
	        	rows[rowcount][0] = rs.getString(1);
				rows[rowcount][1] = rs.getString(2);
				rows[rowcount][3] = rs.getString(3);
				rows[rowcount][4] = rs.getString(4);
				rows[rowcount][5] = rs.getString(5);
				rows[rowcount][6] = rs.getString(6);
				rows[rowcount][7] = rs.getString(7);
				rows[rowcount][8] = rs.getString(8);
				rows[rowcount][9] = rs.getString(9);
				rowcount++;
	        }
	        rowcount=0;
	        c=con.prepareCall("{call getTrain_freeseat(?,?,?)}");
			c.setString("start",start);
			c.setString("end",end);
			c.setString("starttime",starttime);
			rs=c.executeQuery();
	        while(rs.next())
	        {
				rows[rowcount][2] = rs.getString(2);
				rows[rowcount][10] = rs.getString(3);
				rows[rowcount][11] = rs.getString(4);
				rowcount++;
	        }
	        c.close();
	        con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return rows;
	}
	
	public String[][] queryBuy(String id){
		String[][] rows = null;
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url,userName,userPwd);
			Statement cmd = con.createStatement();
			String sql = "select count(*) from ������Ϣ   where ���֤�� ='"+id+"'";
			ResultSet rs = cmd.executeQuery(sql);
			rs.next();
			int rowcount = rs.getInt(1);
			rows = new String[rowcount][10];
			rs = cmd.executeQuery("select * from ������Ϣ   where ���֤�� ='"+id+"' order by ��������   desc");
			rowcount = 0;
			while (rs.next()) {
				for (int i = 0; i < 10; i++)
					rows[rowcount][i] = rs.getString(i+1);
				rowcount++;
			}
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rows;
	}
	
	public String[][] queryStation(String station){
		String[][] rows = null;
		int rowcount=0;
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, userName, userPwd);
			CallableStatement c=con.prepareCall("{call getTrain_station(?)}");
			c.setString("station",station);
			ResultSet rs=c.executeQuery();
			while(rs.next())
				rowcount++;
			rows = new String[rowcount][9];
			c=con.prepareCall("{call getTrain_station(?)}");
			c.setString("station",station);
			rs=c.executeQuery();
			rowcount=0;
	        while(rs.next())
	        {
	        	for (int i = 0; i < 9; i++)
					rows[rowcount][i] = rs.getString(i+1);
				rowcount++;
	        }
	        c.close();
	        con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		return rows;
	}
	
	public String[][] queryNum(String num){
		String[][] rows = null;
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url,userName,userPwd);
			Statement cmd = con.createStatement();
			String sql = "select count(*) from �г�����     where ����='"+num+"'";
			ResultSet rs = cmd.executeQuery(sql);
			rs.next();
			int rowcount = rs.getInt(1);
			rows = new String[rowcount][9];
			rs = cmd.executeQuery("select * from �г�����     where ����='"+num+"'");
			rowcount = 0;
			while (rs.next()) {
				for (int i = 0; i < 9; i++)
					rows[rowcount][i] = rs.getString(i+1);
				rowcount++;
			}
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rows;
	}
	
	public String[][] querySeat(String num,String type){
		String[][] rows = null;
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url,userName,userPwd);
			Statement cmd = con.createStatement();
			String sql = "select count(*) from ��λ��Ϣ     where ����='"+num+"' and ��λ����='"+type+"'";
			ResultSet rs = cmd.executeQuery(sql);
			rs.next();
			int rowcount = rs.getInt(1);
			rows = new String[rowcount][4];
			rs = cmd.executeQuery("select * from ��λ��Ϣ     where ����='"+num+"' and ��λ����='"+type+"'");
			rowcount = 0;
			while (rs.next()) {
				for (int i = 0; i < 4; i++)
					rows[rowcount][i] = rs.getString(i+1);
				rowcount++;
			}
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rows;
	}
	
	public String[][] queryFreeTicket(String num,String starttime){
		String[][] rows = null;
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url,userName,userPwd);
			Statement cmd = con.createStatement();
			String sql = "select count(*) from ʣ����λ��Ϣ     where ����='"+num+"' and ��������='"+starttime+"'";
			ResultSet rs = cmd.executeQuery(sql);
			rs.next();
			int rowcount = rs.getInt(1);
			rows = new String[rowcount][5];
			rs = cmd.executeQuery("select * from ʣ����λ��Ϣ     where ����='"+num+"' and ��������='"+starttime+"'");
			rowcount = 0;
			while (rs.next()) {
				for (int i = 0; i < 5; i++)
					rows[rowcount][i] = rs.getString(i+1);
				rowcount++;
			}
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rows;
	}
	
	public float[] querySaleTicket(String num,String starttime){
		float[] rows = new float[2];
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url,userName,userPwd);
			Statement cmd = con.createStatement();
			String sql = "select count(*) from ������Ϣ      where ����='"+num+"' and ��������='"+starttime+"'";
			ResultSet rs = cmd.executeQuery(sql);
			rs.next();
			rows[0] = rs.getInt(1);
			rs = cmd.executeQuery("select sum(Ʊ��) from ������Ϣ     where ����='"+num+"' and ��������='"+starttime+"'");
			rs.next();
			String t=rs.getString(1);
			if(t==null)
				rows[1] = 0;
			else
				rows[1] = Float.parseFloat(t);
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rows;
	}
}
