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
public class BuyinfoFrm_del extends JFrame implements ActionListener{
	String id;
	JButton btn_del=new JButton("��Ʊ");
	private JTable table = null;
	private String[] cols = { "����","��������","�����","��λ��","��������	","�����","���֤��","����վ","����վ","Ʊ��"};
	private  TicketDao ticketDao = new TicketDao();
	private void initTable_station(String id) {
		String[][] rows = ticketDao.queryBuy(id);
		table = new JTable(rows, cols);
	}
	public BuyinfoFrm_del(String id) {
		this.id=id;
		JPanel jp = (JPanel) this.getContentPane();
		initTable_station(id);
		JScrollPane jsp_table = new JScrollPane(table);
		table.setAutoResizeMode(1);
		jp.add(jsp_table);
		JPanel jp_top = new JPanel();
		jp_top.add(btn_del);
		jp.add(jp_top, BorderLayout.NORTH);
		btn_del.addActionListener(this);
		this.setTitle("��Ʊ��ѯ");
		this.setSize(900, 400);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btn_del){
			if(table.getSelectedRow()!=-1)
				if(table.getValueAt(table.getSelectedRow(), 0)!=""){
					String num=(String) table.getValueAt(table.getSelectedRow(), 0);
					String starttime=(String) table.getValueAt(table.getSelectedRow(), 1);
					String start=(String) table.getValueAt(table.getSelectedRow(), 7);
					String end=(String) table.getValueAt(table.getSelectedRow(), 8);
					String price=(String) table.getValueAt(table.getSelectedRow(), 9);
					new DeleteFrm(num,starttime,id,start,end,price);
					dispose();
			}
		}
	}
}
