package SQLSever;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class MainFrm_user extends JFrame {
	String id;
	private void initMenu() {
		JMenuBar bar = new JMenuBar();
		JMenu m1 = new JMenu("��Ʊ");
		JMenu m2 = new JMenu("��Ʊ");
		JMenu m3 = new JMenu("�ҵĶ���");
		JMenuItem m11 = new JMenuItem("��������Ŀ�ĵ�");
		JMenuItem m12 = new JMenuItem("������");
		JMenuItem m13 = new JMenuItem("����վ");
		JMenuItem m21 = new JMenuItem("��Ʊ");
		JMenuItem m31 = new JMenuItem("�ҵĶ���");
		m11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new StartEndinfoFrm(id);
			}
		});
		m12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new NuminfoFrm();
			}
		});
		m13.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new StationinfoFrm();
			}
		});
		m21.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new BuyinfoFrm_del(id);
			}
		});
		m31.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new BuyinfoFrm(id);
			}
		});
		m1.add(m11);
		m1.add(m12);
		m1.add(m13);
		m2.add(m21);
		m3.add(m31);
		bar.add(m1);
		bar.add(m2);
		bar.add(m3);
		this.setJMenuBar(bar);
	}

	public MainFrm_user(String id) {
		this.id=id;
		JPanel jp = (JPanel) this.getContentPane();
		initMenu();
		this.setTitle("��Ʊ��Ʊϵͳ");
		this.setSize(1000, 600);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new MainFrm_user("1");
	}
}
