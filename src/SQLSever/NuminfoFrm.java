package SQLSever;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
public class NuminfoFrm extends JFrame implements ActionListener{
	JButton btn_del=new JButton("�ر�");
	JButton btn_OK=new JButton("��ѯ");
	JLabel lbl_num =new JLabel("���Σ�");
	JTextField txt_num=new JTextField("",10);
	private JTable table = null;
	private String[] cols = { "����","վ��","��վ����","���","����ʱ��","ͣ��ʱ��","����ʱ��","һ�����۸�","�������۸�"};
	private  TicketDao ticketDao = new TicketDao();
	private void initTable() {
		String[][] rows = {{"","","","","","","","",""}};
		table = new JTable(rows, cols);
	}
	private void updateTable(String num) {
		String[][] rows = ticketDao.queryNum(num);
		table.setModel(new DefaultTableModel(rows, cols));
	}
	public NuminfoFrm() {
		JPanel jp = (JPanel) this.getContentPane();
		initTable();
		JScrollPane jsp_table = new JScrollPane(table);
		table.setAutoResizeMode(1);
		updateTable("");
		jp.add(jsp_table);
		JPanel jp_top = new JPanel();
		jp_top.add(lbl_num);jp_top.add(txt_num);
		jp_top.add(btn_OK);jp_top.add(btn_del);
		jp.add(jp_top, BorderLayout.NORTH);
		btn_del.addActionListener(this);
		btn_OK.addActionListener(this);
		this.setTitle("���β�ѯ");
		this.setSize(900, 400);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String num;
		if(e.getSource()==btn_del){
			dispose();
		}
		if(e.getSource()==btn_OK){
			num = txt_num.getText();
			updateTable(num);
			table.setAutoResizeMode(1);
		}
	}
	public static void main(String[] args) {
		new NuminfoFrm();
	}
}
