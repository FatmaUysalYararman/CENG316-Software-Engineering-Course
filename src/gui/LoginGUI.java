package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import controller.LoginController;

public abstract class LoginGUI {
	private JFrame frame;
	private JTextField textField;
	private JPasswordField passwordField;
	private final Action action = new CheckLoginAction();
	private JLabel lblInvalidUsername;
	private JLabel lblInvalidPassword;
	public LoginController lc;
	public Statement statement;
	
	public LoginGUI(LoginController lc) {
		this.lc = lc;
		initialize();
	}
	
	public void setVisible(boolean b) {
		this.frame.setVisible(b);
	}
	
	public void dispose() {
		this.frame.dispose();
	}
	
	private void initialize() {
		frame = new JFrame(" Login");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		textField = new JTextField();
		textField.setColumns(10);

		JLabel lblUsername = new JLabel("username:");
		JLabel lblPassword = new JLabel("password:");
		passwordField = new JPasswordField();
		JButton btnLogin = new JButton("Login");
		btnLogin.setAction(action);

		lblInvalidUsername = new JLabel("invalid username");
		lblInvalidPassword = new JLabel("invalid password");
		lblInvalidUsername.setVisible(false);
		lblInvalidPassword.setVisible(false);

		JButton button = new JButton("Return");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(false);
				frame.dispose();
				MainGUI newView = new MainGUI();
				newView.setVisible(true);
			}
		});
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
						.createSequentialGroup().addGap(57)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblUsername)
								.addComponent(lblPassword))
						.addGap(37)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(btnLogin)
								.addGroup(groupLayout.createSequentialGroup()
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
												.addComponent(passwordField).addComponent(textField))
										.addGap(18)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(lblInvalidPassword).addComponent(lblInvalidUsername)))))
						.addGroup(groupLayout.createSequentialGroup().addGap(31).addComponent(button)))
				.addContainerGap(45, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addGap(63)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUsername).addComponent(lblInvalidUsername))
				.addGap(18)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblPassword)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblInvalidPassword))
				.addGap(18).addComponent(btnLogin).addGap(18).addComponent(button)
				.addContainerGap(42, Short.MAX_VALUE)));
		frame.getContentPane().setLayout(groupLayout);
	}
	
	public void invalidLogin() {
		lblInvalidUsername.setVisible(true);
		lblInvalidPassword.setVisible(true);
	}
	
	public abstract void checkLogin(String username,String password) throws SQLException;
	
	private class CheckLoginAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public CheckLoginAction() {
			putValue(NAME, "Login");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
			lblInvalidUsername.setVisible(false);
			lblInvalidPassword.setVisible(false);

			String text = textField.getText();
			@SuppressWarnings("deprecation")
			String password = passwordField.getText();
			try {
				checkLogin(text,password);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	
}
