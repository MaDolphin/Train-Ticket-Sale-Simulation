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
	JButton btn_del=new JButton("关闭");
	JButton btn_OK=new JButton("查询");
	JLabel lbl_num =new JLabel("车次：");
	JTextField txt_num=new JTextField("",10);
	private JTable table = null;
	private String[] cols = { "车次","站次","车站名称","里程","到达时间","停留时间","运行时间","一等座价格","二等座价格"};
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
		this.setTitle("车次查询");
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
