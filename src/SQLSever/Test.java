package SQLSever;
import java.sql.*;
public class Test {
public static void main(String[] srg) {
	String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";  //加载JDBC驱动
	String dbURL = "jdbc:sqlserver://localhost:1433; DatabaseName=ticket";  //连接服务器和数据库test
	String userName = "sa";  //默认用户名
	String userPwd = "291910";  //密码
	try {
		Class.forName(driverName);
		Connection conn = DriverManager.getConnection(dbURL, userName, userPwd);
		CallableStatement c=conn.prepareCall("{call getTrain_dist(?,?)}");
		c.setString("start","上海");
		c.setString("end","昆山南");
		ResultSet rs=c.executeQuery();
        while(rs.next())
        {
        	String num=rs.getString("车次");
        	String type=rs.getString("类型");
        	String start=rs.getString("发站");
        	String end=rs.getString("到站");
        	String stime=rs.getString("发车时间");
        	String etime=rs.getString("到达时间");
        	String rtime=rs.getString("运行时间");
        	String oneprice=rs.getString("一等座价格");
        	String twoprice=rs.getString("二等座价格");
        	System.out.print(num+' ');
        	System.out.print(type+' ');
        	System.out.print(start+' ');
        	System.out.print(end+' ');
        	System.out.print(stime+' ');
        	System.out.print(etime+' ');
        	System.out.print(rtime+' ');
        	System.out.print(oneprice+' ');
        	System.out.print(twoprice+' ');
        	System.out.println();
        }
        c.close();
        conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}