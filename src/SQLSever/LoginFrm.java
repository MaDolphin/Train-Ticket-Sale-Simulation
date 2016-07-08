package SQLSever;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class LoginFrm extends JFrame implements ActionListener{
	TicketDao ticketDao = new TicketDao();
	JLabel lbl_name =new JLabel("������");
	JLabel lbl_id =new JLabel("���֤�ţ�");
	JLabel lbl_pwd =new JLabel("���룺");
	JLabel lb_type=new JLabel("���ͣ�");
	JTextField txt_name=new JTextField();
	JTextField txt_id=new JTextField();
	JPasswordField txt_pwd=new JPasswordField();
	JComboBox cmb_type=new JComboBox();
	JButton btn_OK=new JButton("��½");
	JButton btn_new=new JButton("ע��");
	public LoginFrm(){
		JPanel jp=(JPanel)this.getContentPane();
		jp.setLayout(new GridLayout(5,2,10,5));
		jp.add(lbl_name);jp.add(txt_name);
		jp.add(lbl_id);jp.add(txt_id);
		jp.add(lbl_pwd);jp.add(txt_pwd);
		jp.add(lb_type);jp.add(cmb_type);
		cmb_type.addItem("��ͨ�û�");
		cmb_type.addItem("����Ա");
		jp.add(btn_OK);jp.add(btn_new);
		this.setTitle("��½");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		btn_OK.addActionListener(this);
		btn_new.addActionListener(this);
		this.setSize(400, 260);
		this.setVisible(true);
		this.setResizable(false);
	}
	public void actionPerformed(ActionEvent e){
		String name=txt_name.getText();
		String id=txt_id.getText();
		String pwd=txt_pwd.getText();
		int level;
		if (cmb_type.getSelectedItem()=="��ͨ�û�")
			level=0;
		else level=1;
		if (e.getSource() == btn_OK) {
			if(level==0){
				if(ticketDao.isExistPerson(id,pwd,level)){
					new MainFrm_user(id);
					dispose();
				}
				else JOptionPane.showMessageDialog(this, "error��");
			}
			else{
				if(ticketDao.isExistPerson(id,pwd,level)){
					new MainFrm_admin(id);
					dispose();
				}
				else JOptionPane.showMessageDialog(this, "error��");
			}
				
		}
		if (e.getSource() == btn_new) {
			if(level==0){
				if(ticketDao.addPerson(name,id,level,pwd)){
					JOptionPane.showMessageDialog(this, "ע��ɹ���");
					dispose();
					new LoginFrm();
				}
				else JOptionPane.showMessageDialog(this, "���û��Ѵ��ڣ�");
			}else JOptionPane.showMessageDialog(this, "���޷�ע��Ϊ����Ա��");
		}
		
	 }
	public static void main(String[] args) {
		new LoginFrm();
	}
}
