package SQLSever;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
public class SaleTicketinfoFrm extends JFrame implements ActionListener{
	int i = 0;
	JLabel lbl_num =new JLabel("���Σ�");
	JLabel lbl_money =new JLabel("�����ܽ�");
	JLabel lbl_starttime =new JLabel("�������ڣ�");
	JLabel lbl_count =new JLabel("���۳�Ʊ������");
	JTextField txt_num=new JTextField("",10);
	JTextField txt_starttime=new JTextField("",10);
	JTextField txt_money=new JTextField("",10);
	JTextField txt_count=new JTextField("",10);
	JButton btn_OK=new JButton("��ѯ");
	JButton btn_Cancel=new JButton("ȡ��");
	public SaleTicketinfoFrm() {
		JPanel jp=(JPanel)this.getContentPane();
		jp.setLayout(new GridLayout(5,2,10,5));
		txt_count.setEditable(false);
		txt_money.setEditable(false);
		jp.add(lbl_num);jp.add(txt_num);
		jp.add(lbl_starttime);jp.add(txt_starttime);
		jp.add(lbl_money);jp.add(txt_money);
		jp.add(lbl_count);jp.add(txt_count);
		jp.add(btn_OK);jp.add(btn_Cancel);
		this.setTitle("������Ϣ");
		btn_OK.addActionListener(this);
		btn_Cancel.addActionListener(this);
		this.setSize(400, 260);
		this.setVisible(true);
		this.setResizable(false);
	}

	public void actionPerformed(ActionEvent e) {
		TicketDao ticketDao = new TicketDao();
		float[] temp = new float[2];
		String num = txt_num.getText();
		String starttime = txt_starttime.getText();
		if(e.getSource()==btn_OK){
			temp=ticketDao.querySaleTicket(num,starttime);
			txt_count.setText(String.valueOf((int)temp[0]));
			txt_money.setText(String.valueOf(temp[1]));
		}
		if(e.getSource()==btn_Cancel){
			dispose();
		}
	}
	public static void main(String[] args) {
		new SaleTicketinfoFrm();
	}
}
