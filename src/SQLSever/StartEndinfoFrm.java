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
public class StartEndinfoFrm extends JFrame implements ActionListener{
	String id;
	JButton btn_book=new JButton("��Ʊ");
	JButton btn_OK=new JButton("��ѯ");
	JLabel lbl_start =new JLabel("����վ��");
	JLabel lbl_end =new JLabel("����վ��");
	JLabel lbl_time =new JLabel("���ڣ�");
	JTextField txt_start=new JTextField("",10);
	JTextField txt_end=new JTextField("",10);
	JTextField txt_time=new JTextField("",10);
	private JTable table = null;
	private String[] cols_station = { "����","����","��������","����վ","����վ","����ʱ��","��վʱ��","��ʱ/����","һ����Ʊ��","������Ʊ��","һ��������","����������"};
	private  TicketDao ticketDao = new TicketDao();
	private void initTable_station() {
		String[][] rows = {{"","","","","","","","","","","",""}};
		table = new JTable(rows, cols_station);
	}
	private void updateTable(String start,String end,String starttime) {
		String[][] rows = ticketDao.queryStartEnd(start,end,starttime);
		table.setModel(new DefaultTableModel(rows, cols_station));
	}
	public StartEndinfoFrm(String id) {
		this.id=id;
		JPanel jp = (JPanel) this.getContentPane();
		initTable_station();
		JScrollPane jsp_table = new JScrollPane(table);
		table.setAutoResizeMode(1);
		updateTable("","","");
		jp.add(jsp_table);
		JPanel jp_top = new JPanel();
		jp_top.add(lbl_start);jp_top.add(txt_start);
		jp_top.add(lbl_end);jp_top.add(txt_end);
		jp_top.add(lbl_time);jp_top.add(txt_time);
		jp_top.add(btn_OK);jp_top.add(btn_book);
		jp.add(jp_top, BorderLayout.NORTH);
		btn_book.addActionListener(this);
		btn_OK.addActionListener(this);
		this.setTitle("���������ѯ");
		this.setSize(900, 400);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String start0 = txt_start.getText();
		String end0 = txt_end.getText();
		String starttime0 = txt_time.getText();

		if(e.getSource()==btn_book){
			if(table.getSelectedRow()!=-1){
				if(starttime0!=""){
					if(table.getValueAt(table.getSelectedRow(), 2)!=""){
						String num=(String) table.getValueAt(table.getSelectedRow(), 0);
						String starttime=(String) table.getValueAt(table.getSelectedRow(), 2);
						String start=(String) table.getValueAt(table.getSelectedRow(), 3);
						String end=(String) table.getValueAt(table.getSelectedRow(), 4);
						new BookFrm(num,starttime,id,start,end);
					}
				}
			}
		}
		if(e.getSource()==btn_OK){
			start0 = txt_start.getText();
			end0 = txt_end.getText();
			starttime0 = txt_time.getText();
			updateTable(start0,end0,starttime0);
			table.setAutoResizeMode(1);
		}
	}
	public static void main(String[] args) {
		new StartEndinfoFrm("1");
	}
}
