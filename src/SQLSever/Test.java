package SQLSever;
import java.sql.*;
public class Test {
public static void main(String[] srg) {
	String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";  //����JDBC����
	String dbURL = "jdbc:sqlserver://localhost:1433; DatabaseName=ticket";  //���ӷ����������ݿ�test
	String userName = "sa";  //Ĭ���û���
	String userPwd = "291910";  //����
	try {
		Class.forName(driverName);
		Connection conn = DriverManager.getConnection(dbURL, userName, userPwd);
		CallableStatement c=conn.prepareCall("{call getTrain_dist(?,?)}");
		c.setString("start","�Ϻ�");
		c.setString("end","��ɽ��");
		ResultSet rs=c.executeQuery();
        while(rs.next())
        {
        	String num=rs.getString("����");
        	String type=rs.getString("����");
        	String start=rs.getString("��վ");
        	String end=rs.getString("��վ");
        	String stime=rs.getString("����ʱ��");
        	String etime=rs.getString("����ʱ��");
        	String rtime=rs.getString("����ʱ��");
        	String oneprice=rs.getString("һ�����۸�");
        	String twoprice=rs.getString("�������۸�");
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