package SQLSever;
import java.awt.BorderLayout;
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
public class FreeTicketinfoFrm extends JFrame implements ActionListener{
	int i = 0;
	JButton btn_OK=new JButton("查询");
	JLabel lbl_num =new JLabel("车次：");
	JLabel lbl_starttime =new JLabel("发车日期：");
	JTextField txt_num=new JTextField("",10);
	JTextField txt_starttime=new JTextField("",10);
	private JTable table = null;
	private String[] cols = { "车次","发车日期","站次","一等座空余","二等座空余"};
	private  TicketDao ticketDao = new TicketDao();
	private void initTable_station() {
		String[][] rows = {{"","","","",""}};
		table = new JTable(rows, cols);
	}
	private void updateTable(String num,String starttime) {
		String[][] rows = ticketDao.queryFreeTicket(num,starttime);
		table.setModel(new DefaultTableModel(rows, cols));
		i=rows.length;
	}
	public FreeTicketinfoFrm() {
		JPanel jp = (JPanel) this.getContentPane();
		initTable_station();
		JScrollPane jsp_table = new JScrollPane(table);
		table.setAutoResizeMode(1);
		updateTable("","");
		jp.add(jsp_table);
		JPanel jp_top = new JPanel();
		jp_top.add(lbl_num);jp_top.add(txt_num);
		jp_top.add(lbl_starttime);jp_top.add(txt_starttime);
		jp_top.add(btn_OK);
		jp.add(jp_top, BorderLayout.NORTH);

		btn_OK.addActionListener(this);
		this.setTitle("座位信息");
		this.setSize(900, 400);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String num = txt_num.getText();
		String starttime = txt_starttime.getText();
		if(e.getSource()==btn_OK){
			updateTable(num,starttime);
			table.setAutoResizeMode(1);
		}
	}
	public static void main(String[] args) {
		new FreeTicketinfoFrm();
	}
}
