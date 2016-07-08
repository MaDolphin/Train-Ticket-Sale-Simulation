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
public class SeatinfoFrm extends JFrame implements ActionListener{
	int i = 0;
	JButton btn_OK=new JButton("��ѯ");
	JLabel lbl_num =new JLabel("���Σ�");
	JLabel lbl_type =new JLabel("��λ���ͣ�");
	JLabel lbl_all =new JLabel("��λ������");
	JComboBox cmb_type=new JComboBox();
	JTextField txt_num=new JTextField("",5);
	JTextField txt_all=new JTextField("",5);
	private JTable table = null;
	private String[] cols = { "����","�����","��λ��","��λ����"};
	private  TicketDao ticketDao = new TicketDao();
	private void initTable_station() {
		String[][] rows = {{"","","",""}};
		table = new JTable(rows, cols);
	}
	private void updateTable(String num,String type) {
		String[][] rows = ticketDao.querySeat(num,type);
		table.setModel(new DefaultTableModel(rows, cols));
		i=rows.length;
	}
	public SeatinfoFrm() {
		JPanel jp = (JPanel) this.getContentPane();
		initTable_station();
		JScrollPane jsp_table = new JScrollPane(table);
		table.setAutoResizeMode(1);
		updateTable("","");
		jp.add(jsp_table);
		JPanel jp_top = new JPanel();
		txt_all.setEditable(false);
		jp_top.add(lbl_num);jp_top.add(txt_num);
		jp_top.add(lbl_type);jp_top.add(cmb_type);
		cmb_type.addItem("һ����");
		cmb_type.addItem("������");
		jp_top.add(btn_OK);
		jp_top.add(lbl_all);jp_top.add(txt_all);
		jp.add(jp_top, BorderLayout.NORTH);

		btn_OK.addActionListener(this);
		this.setTitle("��λ��Ϣ");
		this.setSize(900, 400);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String num = txt_num.getText();
		String type = (String) cmb_type.getSelectedItem();
		if(e.getSource()==btn_OK){
			updateTable(num,type);
			table.setAutoResizeMode(1);
			txt_all.setText(String.valueOf(i));
		}
	}
	public static void main(String[] args) {
		new SeatinfoFrm();
	}
}
