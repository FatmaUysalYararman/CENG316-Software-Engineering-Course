package gui;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import controller.LoginController;
import database.DatabaseConnection;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MainGUI {
	
	private JLabel lblRegister;
	private JButton btnPersonelLogin;
	private JButton btnClientLogin;
	private JButton btnGuestLogin;
	private JFrame frame;

	public MainGUI() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		btnGuestLogin = new JButton("Guest Login");
		btnClientLogin = new JButton("Client Login");
		btnPersonelLogin = new JButton("Personel Login");
		lblRegister = new JLabel("Register");
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(133)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(btnPersonelLogin, GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
										.addComponent(btnGuestLogin, GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
										.addComponent(btnClientLogin, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(groupLayout.createSequentialGroup().addGap(157).addComponent(lblRegister,
								GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(156, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup().addGap(33)
						.addComponent(btnPersonelLogin, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
						.addGap(18).addComponent(btnClientLogin, GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
						.addGap(18)
						.addComponent(btnGuestLogin, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblRegister).addGap(23)));
		
		addPersonelLoginListener(new PersonelLoginAction());
		addClientLoginListener(new CustomerLoginAction());
		addGuestLoginListener(new GuestLoginAction());
		addRegisterListener(new RegisterAction());
		
		frame.getContentPane().setLayout(groupLayout);
	}
	
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
	public void dispose() {
		frame.dispose();
	}
	
	public void addPersonelLoginListener(Action a) {
		btnPersonelLogin.setAction(a);
	}
	public void addClientLoginListener(Action a) {
		btnClientLogin.setAction(a);
	}
	public void addGuestLoginListener(Action a) {
		btnGuestLogin.setAction(a);
	}
	public void addRegisterListener(MouseListener a) {
		lblRegister.addMouseListener(a);
	}
	
	private class RegisterAction implements MouseListener{
		public void mouseClicked(MouseEvent arg0) {
			JFrame extraFrame = new JFrame();
			extraFrame.setSize(700,200);
			extraFrame.setLayout(new BorderLayout());
			JPanel content = new JPanel();
			
			JLabel usernameLbl = new JLabel("Username : ");
			JLabel passwordLbl = new JLabel("Password : ");
			
			JTextField usernameField = new JTextField();
			JTextField passwordField = new JTextField();
			usernameField.setColumns(20);
			passwordField.setColumns(20);
			
			JLabel invalidUsername = new JLabel("Invalid Username ");
			JLabel invalidPassword = new JLabel("Invalid Password ");
			invalidUsername.setVisible(false);
			invalidPassword.setVisible(false);
			
			JLabel nonUniqueUsername = new JLabel("This username is already registered!");
			nonUniqueUsername.setVisible(false);
			
			JPanel usernamePnl = new JPanel();
			usernamePnl.setLayout(new FlowLayout());
			usernamePnl.add(usernameLbl);
			usernamePnl.add(usernameField);
			usernamePnl.add(invalidUsername);
			usernamePnl.add(nonUniqueUsername);
			
			JPanel passwordPnl = new JPanel();
			passwordPnl.setLayout(new FlowLayout());
			passwordPnl.add(passwordLbl);
			passwordPnl.add(passwordField);
			passwordPnl.add(invalidPassword);
			
			JButton submitBtn = new JButton("Submit");
			submitBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					nonUniqueUsername.setVisible(false);
					String username = usernameField.getText();
					String password = passwordField.getText();

					if(password!=null && password!="") {
						if(username!=null && username!="") {
							invalidUsername.setVisible(false);
							invalidPassword.setVisible(false);
							ResultSet check = null;
							try {
								DatabaseConnection.updateQuery("INSERT INTO `Customers` (`customerId`, `customerUsername`, `customerPassword`) VALUES (NULL, '"+username+"','"+password+"');");
								JOptionPane.showMessageDialog(null, "Succesfully registered!");
								extraFrame.setVisible(false);
								extraFrame.dispose();
								
							} catch (SQLException e) {
								invalidUsername.setVisible(true);
							}
							
						}
						else {
							invalidUsername.setVisible(true);
						}
					} 
					else {
						invalidPassword.setVisible(true);
					}
				}		
			});
			
			content.add(usernamePnl);
			content.add(passwordPnl);
			content.add(submitBtn);
			content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
			
			extraFrame.add(content,BorderLayout.WEST);
			extraFrame.setVisible(true);
			
			
		}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	}
	
	private class PersonelLoginAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public PersonelLoginAction() {
			putValue(NAME, "Personel Login");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			dispose();
			new LoginController("employee");
		}
	}

	private class CustomerLoginAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public CustomerLoginAction() {
			putValue(NAME, "Customer Login");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			dispose();
			new LoginController("customer");
		}
	}

	private class GuestLoginAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public GuestLoginAction() {
			putValue(NAME, "Guest Login");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			dispose();
			new LoginController("guest");
		}
	}
	
}
