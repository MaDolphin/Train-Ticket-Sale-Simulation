package SQLSever;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;
import java.util.*;
public class DeleteFrm extends JFrame implements ActionListener{
	TicketDao ticketDao = new TicketDao();
	JLabel lbl_num =new JLabel("车次：");
	JLabel lbl_starttime =new JLabel("发车日期：");
	JLabel lbl_id =new JLabel("身份证号：");
	JLabel lbl_start =new JLabel("起始站：");
	JLabel lbl_end =new JLabel("到达站：");
	JLabel lbl_price =new JLabel("票价：");
	JTextField txt_num=new JTextField();
	JTextField txt_starttime=new JTextField();
	JTextField txt_id=new JTextField();
	JTextField txt_start=new JTextField();
	JTextField txt_end=new JTextField();
	JTextField txt_price=new JTextField();
	JButton btn_OK=new JButton("确定");
	JButton btn_Cancel=new JButton("取消");
	public DeleteFrm(String num,String starttime,String id,String start,String end,String price){
		txt_num.setText(num);
		txt_starttime.setText(starttime);
		txt_id.setText(id);
		txt_start.setText(start);
		txt_end.setText(end);
		txt_price.setText(price);
		JPanel jp=(JPanel)this.getContentPane();
		jp.setLayout(new GridLayout(7,2,10,5));
		txt_num.setEditable(false);
		txt_starttime.setEditable(false);
		txt_id.setEditable(false);
		txt_start.setEditable(false);
		txt_end.setEditable(false);
		txt_price.setEditable(false);
		jp.add(lbl_num);jp.add(txt_num);
		jp.add(lbl_starttime);jp.add(txt_starttime);
		jp.add(lbl_id);jp.add(txt_id);
		jp.add(lbl_start);jp.add(txt_start);
		jp.add(lbl_end);jp.add(txt_end);
		jp.add(lbl_price);jp.add(txt_price);
		jp.add(btn_OK);jp.add(btn_Cancel);
		this.setTitle("退票信息");
		btn_OK.addActionListener(this);
		btn_Cancel.addActionListener(this);
		this.setSize(400, 260);
		this.setVisible(true);
		this.setResizable(false);
	}
	public void actionPerformed(ActionEvent e){
		String num=txt_num.getText();
		String starttime=txt_starttime.getText();
		String start=txt_start.getText();
		String end=txt_end.getText();
		String id=txt_id.getText();
		Date dt=new Date();
	    SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
	    String ttime= matter1.format(dt);
	    long longstr1 = Long.valueOf(starttime.replaceAll("[-\\s:]",""));
	    long longstr2 = Long.valueOf(ttime.replaceAll("[-\\s:]",""));
		if(e.getSource() == btn_OK){
			if (longstr1>longstr2) {
				if(ticketDao.delTicket(num,starttime,id)){
					JOptionPane.showMessageDialog(this, "退票成功！");
					dispose();
					new BuyinfoFrm_del(id);
				}
				else JOptionPane.showMessageDialog(this, "失败！");
			}
			else
				JOptionPane.showMessageDialog(this, "车票已过期，无法退票！");
		}
		if (e.getSource() == btn_Cancel) {
			dispose();
		}
	 }
}