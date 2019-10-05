package gui;


import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import app.App;

import java.sql.SQLException;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;


public abstract class SessionGUI {
	
	protected JFrame frame;
	protected JPanel navigator;
	protected String userType;
	protected Integer personId;
	protected JPanel content;
	protected JPanel pageContent;
	
	public SessionGUI(String userType,Integer personId) {
		this.userType = userType;
		this.personId = personId;
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(1366,768));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new FlowLayout());
		frame.setLayout(new BorderLayout());
		
		navigator = new JPanel();
		navigator.setPreferredSize(new Dimension(1366,100));
		setNavigator();
		setLogOut();
		frame.add(navigator,BorderLayout.PAGE_START);
		frame.pack();
		frame.setVisible(true);
	}

	public abstract void setNavigator();
	
	public abstract void setContent(int id) throws SQLException;
	
	public void setLogOut() {
		Action logout = new Logout("Logout");
		JButton logoutBtn = new JButton(logout);
		navigator.add(logoutBtn);
	}
	
	private class Logout extends AbstractAction{
		private static final long serialVersionUID = 1L;
		private Logout(String text) {super(text);}
		public void actionPerformed(ActionEvent e) {
			frame.setVisible(false);
			frame.dispose();
			new App();
		}
	}
}
