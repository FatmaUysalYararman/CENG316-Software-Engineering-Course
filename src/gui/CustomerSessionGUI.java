package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import database.DatabaseConnection;

public class CustomerSessionGUI extends SessionGUI {
	

	public CustomerSessionGUI(String userType,Integer personId) {
		super(userType, personId);
	}

	
	public void setNavigator() {
		navigator = new JPanel();
		navigator.setSize(1366,100);
		
		Action showSessions = new ShowSessions("Show Sessions");
		JButton showSessionBtn = new JButton(showSessions);
		navigator.add(showSessionBtn);
		
		Action showMyTickets = new ShowMyTickets("Show My Tickets");
		JButton showMyTicketsBtn = new JButton(showMyTickets);
		navigator.add(showMyTicketsBtn);
		
		Action contact = new Contact("Contact");
		JButton contactBtn = new JButton(contact);
		navigator.add(contactBtn);
	}

	public void setContent(int id) throws SQLException {
		content = new JPanel();
	    content.setLayout(new BorderLayout());
		pageContent = new JPanel();
		switch (id) {
		case 1:
			case1(); // Show all sessions
			case4(); // Buy ticket
	        break;
		case 2:
			case2(); // Show my tickets
			break;
		case 3:
			case3(); // Contact
			break;
		}
	}
	
	private void case1() throws SQLException {
		DatabaseConnection.disconnect();
		DatabaseConnection.connectt();
		String[] columns = new String[] {
				"SessionId","Fullness","Size","Aircraft","Date","Time","From","To"
		};
		List<List<Object>> rows = new ArrayList<>();
		ResultSet result = null;
		result = DatabaseConnection.query("SELECT * FROM Sessions");
		while(result.next()) {
			List<Object> row = new ArrayList<Object>();
			row.add(result.getInt("SessionId"));
			int aircraftId = result.getInt("AircraftId");
			
			int routeId = result.getInt("RouteId");

			row.add(result.getInt("RegisteredCustomerAmount"));
			ResultSet aircraftQ = DatabaseConnection.query("SELECT * FROM Aircrafts WHERE Id="+aircraftId+";");
			while(aircraftQ.next()) {
				row.add(aircraftQ.getInt("maximumSeatAmount"));
				row.add(aircraftQ.getString("planeModel"));
			}
			row.add(result.getDate("Date"));
			row.add(result.getString("Time"));
			aircraftQ.close();
 
			
			
			ResultSet routeQ = DatabaseConnection.query("SELECT * FROM Routes WHERE RouteId="+routeId+";");
			int cityId1 = 0;
			int cityId2 = 0;
			while(routeQ.next()) {
				 cityId1 = routeQ.getInt("FromCityId");
				 cityId2 = routeQ.getInt("TargetCityId");
			}
			routeQ.close();
 

			ResultSet cityQ = DatabaseConnection.query("SELECT * FROM Cities WHERE CityId="+cityId1+";");
			while(cityQ.next()) {
				row.add(cityQ.getString("City"));
			}
			cityQ.close();
 

			ResultSet cityQ2 = DatabaseConnection.query("SELECT * FROM Cities WHERE CityId="+cityId2+";");
			while(cityQ2.next()) {
				row.add(cityQ2.getString("City"));
			}
			cityQ2.close();
 

			rows.add(row);
		}
		result.close();

		
		Object[][] data = new Object[rows.size()][];
		for(int i = 0; i < rows.size(); i++) {
			ArrayList<Object> row = (ArrayList<Object>) rows.get(i);
			data[i] = row.toArray(new Object[row.size()]);
		}
		JTable table = new JTable(data,columns);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		JLabel lblHeading = new JLabel("Sessions");
        lblHeading.setFont(new Font("Arial",Font.TRUETYPE_FONT,24));
        
		JPanel buyTicketPanel = new JPanel();
        JButton buyTicketBtn = new JButton("Buy Ticket");
		buyTicketBtn.setMaximumSize(buyTicketBtn.getPreferredSize());
		buyTicketPanel.add(buyTicketBtn);
		buyTicketBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame extraFrame = new JFrame();
				JPanel innerPanel = new JPanel();
				int customerId = personId;
				
				JLabel sessionLbl = new JLabel("Session Id : ");
				JTextField sessionField = new JTextField();
				sessionField.setColumns(10);
				
				JLabel quantityLbl = new JLabel("Quantity : ");
				JTextField quantityField = new JTextField();
				quantityField.setColumns(10);
				
				JPanel firstRow = new JPanel();
				firstRow.add(sessionLbl);
				firstRow.add(sessionField);
				firstRow.setMaximumSize(firstRow.getPreferredSize());
				
				JPanel secondRow = new JPanel();
				secondRow.add(quantityLbl);
				secondRow.add(quantityField);
				secondRow.setMaximumSize(secondRow.getPreferredSize());
				
				JButton submitBtn = new JButton("Submit");
				submitBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Integer sessionId = Integer.parseInt(sessionField.getText());
						Integer quantity = Integer.parseInt(quantityField.getText());
						for (int i = 0; i < quantity.intValue(); i++) {
							try {
								DatabaseConnection.updateQuery("INSERT INTO `tickets` (`ticketId`, `customerId`, `sessionId`) VALUES (NULL, '"+customerId+"','"+sessionId+"');");
							} catch (SQLException e1) {
								JOptionPane.showMessageDialog(null, "There is something wrong try again please!");
								break;
							}
						}
						JOptionPane.showMessageDialog(null, "Transaction completed!");
						extraFrame.setVisible(false);
						extraFrame.dispose();
						return;
					}			
				});
				submitBtn.setMaximumSize(submitBtn.getPreferredSize());
				
				innerPanel.add(firstRow);
				innerPanel.add(secondRow);
				innerPanel.add(submitBtn);
				innerPanel.setLayout(new BoxLayout(innerPanel,BoxLayout.Y_AXIS));
				
				innerPanel.setMaximumSize(innerPanel.getPreferredSize());
				
				extraFrame.setContentPane(innerPanel);
				extraFrame.setSize(300,300);
				extraFrame.setVisible(true);
			}
		});
		
        
        pageContent.setLayout(new BorderLayout());
        
        pageContent.add(buyTicketPanel, BorderLayout.LINE_END);
        pageContent.add(lblHeading,BorderLayout.PAGE_START);
        pageContent.add(scrollPane,BorderLayout.CENTER);
        pageContent.setSize(550, 200);

        content.add(navigator,BorderLayout.PAGE_START);
        content.add(pageContent,BorderLayout.CENTER);
        
        frame.setContentPane(content);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
	}
	
	private void case2() throws SQLException {
		DatabaseConnection.disconnect();
		DatabaseConnection.connectt();
		String[] columns = new String[] {
				"TicketId","SessionId","Fullness","Size","Aircraft","Date","Time","From","To"

		};
		List<List<Object>> rows = new ArrayList<>();
		List<Integer> sessionIds = new ArrayList<>();
		List<Integer> ticketIds = new ArrayList<>();
		ResultSet result = null;
		result = DatabaseConnection.query("SELECT * FROM Tickets WHERE customerId="+personId+";");
		while(result.next()) {
			sessionIds.add(result.getInt("sessionId"));
			ticketIds.add(result.getInt("ticketId"));
		}
		for(int i = 0; i < sessionIds.size() ; i++) {
			result = null;
			result = DatabaseConnection.query("SELECT * FROM Sessions WHERE sessionId="+sessionIds.get(i)+";");
			while(result.next()) {
				List<Object> row = new ArrayList<Object>();
				row.add(ticketIds.get(i));
				row.add(result.getInt("SessionId"));
				int aircraftId = result.getInt("AircraftId");
				
				int routeId = result.getInt("RouteId");

				row.add(result.getInt("RegisteredCustomerAmount"));
				ResultSet aircraftQ = DatabaseConnection.query("SELECT * FROM Aircrafts WHERE Id="+aircraftId+";");
				while(aircraftQ.next()) {
					row.add(aircraftQ.getInt("maximumSeatAmount"));
					row.add(aircraftQ.getString("planeModel"));
				}
				aircraftQ.close();
	 

				row.add(result.getDate("Date"));
				row.add(result.getString("Time"));
				
				
				ResultSet routeQ = DatabaseConnection.query("SELECT * FROM Routes WHERE RouteId="+routeId+";");
				int cityId1 = 0;
				int cityId2 = 0;
				while(routeQ.next()) {
					 cityId1 = routeQ.getInt("FromCityId");
					 cityId2 = routeQ.getInt("TargetCityId");
				}
				routeQ.close();
	 

				ResultSet cityQ = DatabaseConnection.query("SELECT * FROM Cities WHERE CityId="+cityId1+";");
				while(cityQ.next()) {
					row.add(cityQ.getString("City"));
				}
				cityQ.close();
	 

				ResultSet cityQ2 = DatabaseConnection.query("SELECT * FROM Cities WHERE CityId="+cityId2+";");
				while(cityQ2.next()) {
					row.add(cityQ2.getString("City"));
				}
				cityQ2.close();
	 

				rows.add(row);
			}
		}
		result.close();

		Object[][] data = new Object[rows.size()][];
		for(int i = 0; i < rows.size(); i++) {
			ArrayList<Object> row = (ArrayList<Object>) rows.get(i);
			data[i] = row.toArray(new Object[row.size()]);
		}
		JTable table = new JTable(data,columns);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		JLabel lblHeading = new JLabel("My Tickets");
        lblHeading.setFont(new Font("Arial",Font.TRUETYPE_FONT,24));
        pageContent.setLayout(new BorderLayout());
        
        pageContent.add(lblHeading,BorderLayout.PAGE_START);
        pageContent.add(scrollPane,BorderLayout.CENTER);
        pageContent.setSize(550, 200);

       
        content.add(navigator,BorderLayout.PAGE_START);
        content.add(pageContent,BorderLayout.CENTER);
        
        frame.setContentPane(content);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}

	private void case3() throws SQLException {
		DatabaseConnection.disconnect();
		DatabaseConnection.connectt();
		JLabel emailLbl = new JLabel("Email :");
		JTextField emailTextField = new JTextField();
		emailTextField.setColumns(20);
		JLabel subjectLbl = new JLabel("Subject :");
		JTextField subjectTextField = new JTextField();
		subjectTextField.setColumns(50);
		JLabel messageLbl = new JLabel("Message :");
		JTextArea message = new JTextArea();
		message.setColumns(25);
		message.setRows(25);
		message.setLineWrap(true);
		message.setWrapStyleWord(true);
		JButton sendBtn = new JButton("Send");
		sendBtn.setMaximumSize(sendBtn.getPreferredSize());
		
		JPanel firstRow = new JPanel();
		firstRow.setLayout(new FlowLayout());
		firstRow.add(emailLbl);
		firstRow.add(emailTextField);
		firstRow.setMaximumSize(firstRow.getPreferredSize());
		
		JPanel secondRow = new JPanel();
		secondRow.setLayout(new FlowLayout());
		secondRow.add(subjectLbl);
		secondRow.add(subjectTextField);
		secondRow.setMaximumSize(secondRow.getPreferredSize());
		
		JPanel thirdRow = new JPanel();
		thirdRow.setLayout(new FlowLayout());
		thirdRow.add(messageLbl);
		thirdRow.add(message);
		JScrollPane scrollPane = new JScrollPane(message);
		thirdRow.add(scrollPane);
		thirdRow.setMaximumSize(thirdRow.getPreferredSize());
	
		pageContent.add(firstRow);
		pageContent.add(secondRow);
		pageContent.add(thirdRow);
		pageContent.add(sendBtn);
		
		pageContent.setLayout(new BoxLayout(pageContent,BoxLayout.PAGE_AXIS));
		
		content.add(navigator,BorderLayout.PAGE_START);
        content.add(pageContent,BorderLayout.CENTER);

        frame.setContentPane(content);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}
	
	private void case4() throws SQLException{
		
		
	}

	class ShowSessions extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ShowSessions(String text) {super(text);}

		public void actionPerformed(ActionEvent arg0) {
			try {
				setContent(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	class ShowMyTickets extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public ShowMyTickets(String text) {super(text);}
		public void actionPerformed(ActionEvent arg0) {
			try {
				setContent(2);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	class Contact extends AbstractAction{
		private static final long serialVersionUID = 1L;
		public Contact(String text) {super(text);}
		public void actionPerformed(ActionEvent arg0) {
			try {
				setContent(3);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
