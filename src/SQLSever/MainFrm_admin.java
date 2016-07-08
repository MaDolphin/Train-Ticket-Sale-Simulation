package SQLSever;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class MainFrm_admin extends JFrame {
	String id;
	private void initMenu() {
		JMenuBar bar = new JMenuBar();
		JMenu m1 = new JMenu("管理");
		JMenuItem m11 = new JMenuItem("指定车次座位信息");
		JMenuItem m12 = new JMenuItem("指定车次空余车票情况");
		JMenuItem m13 = new JMenuItem("指定车次车票销售情况");
		JMenuItem m14 = new JMenuItem("注销");
		m11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new SeatinfoFrm();
			}
		});
		m12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FreeTicketinfoFrm();
			}
		});
		m13.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new SaleTicketinfoFrm();
			}
		});
		m14.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new LoginFrm();
			}
		});
		m1.add(m11);
		m1.add(m12);
		m1.add(m13);
		m1.addSeparator();   
		m1.add(m14);
		bar.add(m1);
		this.setJMenuBar(bar);
	}

	public MainFrm_admin(String id) {
		this.id=id;
		JPanel jp = (JPanel) this.getContentPane();
		initMenu();
		this.setTitle("火车票订票系统——管理");
		this.setSize(1000, 600);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new MainFrm_admin("1");
	}
}
