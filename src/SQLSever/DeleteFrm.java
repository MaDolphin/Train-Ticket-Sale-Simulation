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
	JLabel lbl_num =new JLabel("���Σ�");
	JLabel lbl_starttime =new JLabel("�������ڣ�");
	JLabel lbl_id =new JLabel("���֤�ţ�");
	JLabel lbl_start =new JLabel("��ʼվ��");
	JLabel lbl_end =new JLabel("����վ��");
	JLabel lbl_price =new JLabel("Ʊ�ۣ�");
	JTextField txt_num=new JTextField();
	JTextField txt_starttime=new JTextField();
	JTextField txt_id=new JTextField();
	JTextField txt_start=new JTextField();
	JTextField txt_end=new JTextField();
	JTextField txt_price=new JTextField();
	JButton btn_OK=new JButton("ȷ��");
	JButton btn_Cancel=new JButton("ȡ��");
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
		this.setTitle("��Ʊ��Ϣ");
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
					JOptionPane.showMessageDialog(this, "��Ʊ�ɹ���");
					dispose();
					new BuyinfoFrm_del(id);
				}
				else JOptionPane.showMessageDialog(this, "ʧ�ܣ�");
			}
			else
				JOptionPane.showMessageDialog(this, "��Ʊ�ѹ��ڣ��޷���Ʊ��");
		}
		if (e.getSource() == btn_Cancel) {
			dispose();
		}
	 }
}