package SQLSever;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
public class BookFrm extends JFrame implements ActionListener{
	TicketDao ticketDao = new TicketDao();
	JLabel lbl_num =new JLabel("车次：");
	JLabel lbl_starttime =new JLabel("发车日期：");
	JLabel lbl_id =new JLabel("身份证号：");
	JLabel lbl_start =new JLabel("起始站：");
	JLabel lbl_end =new JLabel("到达站：");
	JLabel lbl_type =new JLabel("座位类型：");
	JTextField txt_num=new JTextField();
	JTextField txt_starttime=new JTextField();
	JTextField txt_id=new JTextField();
	JTextField txt_start=new JTextField();
	JTextField txt_end=new JTextField();
	JComboBox cmb_type=new JComboBox();
	JButton btn_OK=new JButton("确定");
	JButton btn_Cancel=new JButton("取消");
	public BookFrm(String num,String starttime,String id,String start,String end){
		txt_num.setText(num);
		txt_starttime.setText(starttime);
		txt_id.setText(id);
		txt_start.setText(start);
		txt_end.setText(end);
		JPanel jp=(JPanel)this.getContentPane();
		jp.setLayout(new GridLayout(7,2,10,5));
		txt_num.setEditable(false);
		txt_starttime.setEditable(false);
		txt_id.setEditable(false);
		txt_start.setEditable(false);
		txt_end.setEditable(false);
		jp.add(lbl_num);jp.add(txt_num);
		jp.add(lbl_starttime);jp.add(txt_starttime);
		jp.add(lbl_id);jp.add(txt_id);
		jp.add(lbl_start);jp.add(txt_start);
		jp.add(lbl_end);jp.add(txt_end);
		jp.add(lbl_type);jp.add(cmb_type);
		cmb_type.addItem("二等座");
		cmb_type.addItem("一等座");
		jp.add(btn_OK);jp.add(btn_Cancel);
		this.setTitle("订票信息");
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
		String type=(String) cmb_type.getSelectedItem();

		if (e.getSource() == btn_OK) {
			int flag=ticketDao.addTicket(num,starttime,"网络",id,start,end,type);
			switch(flag){
				case 1:JOptionPane.showMessageDialog(this, "订票成功！");
						dispose();break;
				case 0:JOptionPane.showMessageDialog(this, "您已经购买过这车票！");break;
				case 2:JOptionPane.showMessageDialog(this, "此车票已买完！");break;
			}
		}
		if (e.getSource() == btn_Cancel) {
			dispose();
		}
	 }
}