package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import database.DatabaseConnection;

public class PersonelSessionGUI extends SessionGUI {
	
	private JFrame extraFrame;
	private JLabel invalidDate;
	private JLabel invalidTime;
	
	public PersonelSessionGUI(String userType,Integer personelId) {
		super(userType,personelId);
	}

	public void setNavigator() {
		navigator = new JPanel();
		navigator.setSize(1366,100);
		
		switch (userType) {
		case "admin" :
			Action showSessions = new ShowSessions("Show Sessions");
			JButton showSessionBtn = new JButton(showSessions);
			navigator.add(showSessionBtn);
			Action showAircrafts = new ShowAircrafts("Show Aircrafts");
			JButton showAircraftsBtn = new JButton(showAircrafts);
			navigator.add(showAircraftsBtn);
			Action createSession = new CreateSession("Create Session");
			JButton createSessionBtn = new JButton(createSession);
			navigator.add(createSessionBtn);
			break;
		case "pilot" :
			Action showAssignedSessions = new ShowAssignedSessions("Show My Sessions");
			JButton showAssignedSessionsButton = new JButton(showAssignedSessions);
			navigator.add(showAssignedSessionsButton);
			break;
		case "flightattendant" : 
			showAssignedSessions = new ShowAssignedSessions("Show My Sessions");
			showAssignedSessionsButton = new JButton(showAssignedSessions);
			navigator.add(showAssignedSessionsButton);
			break;
		}
		
	}

	public void setContent(int id) throws SQLException {
		content = new JPanel();
	    content.setLayout(new BorderLayout());
		pageContent = new JPanel();
		switch (id) {
		case 1:
			case1(); // Show all sessions
	        break;
		case 2:
			case2(); // Show assigned sessions
			break;
		case 3:
			case3(); // Show all aircrafts
			break;
		case 4:
			case4(); // Create session
			break;
		}
		
	}
	
	private void case1() throws SQLException {
		DatabaseConnection.disconnect();
		DatabaseConnection.connectt();
		pageContent = new JPanel();
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
			ResultSet aircraftQ = DatabaseConnection.query( "SELECT * FROM Aircrafts WHERE Id="+aircraftId+";");
			while(aircraftQ.next()) {
				row.add(aircraftQ.getInt("maximumSeatAmount"));
				row.add(aircraftQ.getString("planeModel"));
			}
			aircraftQ.close();
			
			row.add(result.getDate("Date"));
			row.add(result.getString("Time"));
			
			
			ResultSet routeQ = DatabaseConnection.query( "SELECT * FROM Routes WHERE RouteId="+routeId+";");
			int cityId1 = 0;
			int cityId2 = 0;
			while(routeQ.next()) {
				 cityId1 = routeQ.getInt("FromCityId");
				 cityId2 = routeQ.getInt("TargetCityId");
			}
			routeQ.close();
			
			ResultSet cityQ = DatabaseConnection.query( "SELECT * FROM Cities WHERE CityId="+cityId1+";");
			while(cityQ.next()) {
				row.add(cityQ.getString("City"));
			}
			cityQ.close();
			
			ResultSet cityQ2 = DatabaseConnection.query( "SELECT * FROM Cities WHERE CityId="+cityId2+";");
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
	
	private void case2() throws SQLException {
		DatabaseConnection.disconnect();
		DatabaseConnection.connectt();
		pageContent = new JPanel();
		String[] columns = new String[] {
				"SessionId","Fullness","Size","Aircraft","Date","Time","From","To"
		};
		List<List<Object>> rows = new ArrayList<>();
		ResultSet result = null;
		result = DatabaseConnection.query( "SELECT s.* FROM Sessions s, FlightCrew f WHERE f.employeeId="+personId+";");
		while(result.next()) {
			List<Object> row = new ArrayList<Object>();
			row.add(result.getInt("SessionId"));
			int aircraftId = result.getInt("AircraftId");
			
			int routeId = result.getInt("RouteId");

			row.add(result.getInt("RegisteredCustomerAmount"));
			ResultSet aircraftQ = DatabaseConnection.query( "SELECT * FROM Aircrafts WHERE Id="+aircraftId+";");
			while(aircraftQ.next()) {
				row.add(aircraftQ.getInt("maximumSeatAmount"));
				row.add(aircraftQ.getString("planeModel"));
			}
			aircraftQ.close();
			row.add(result.getDate("Date"));
			row.add(result.getString("Time"));
			
			
			ResultSet routeQ = DatabaseConnection.query( "SELECT * FROM Routes WHERE RouteId="+routeId+";");
			int cityId1 = 0;
			int cityId2 = 0;
			while(routeQ.next()) {
				 cityId1 = routeQ.getInt("FromCityId");
				 cityId2 = routeQ.getInt("TargetCityId");
			}
			routeQ.close();
			ResultSet cityQ = DatabaseConnection.query( "SELECT * FROM Cities WHERE CityId="+cityId1+";");
			while(cityQ.next()) {
				row.add(cityQ.getString("City"));
			}
			cityQ.close();
			ResultSet cityQ2 = DatabaseConnection.query( "SELECT * FROM Cities WHERE CityId="+cityId2+";");
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
        pageContent.setLayout(new BorderLayout());
        
        pageContent.add(lblHeading,BorderLayout.PAGE_START);
        pageContent.add(scrollPane,BorderLayout.CENTER);
        pageContent.setSize(550, 200);
        
        content = new JPanel();
        content.setLayout(new BorderLayout());
        content.add(navigator,BorderLayout.PAGE_START);
        content.add(pageContent,BorderLayout.CENTER);
        
        frame.setContentPane(content);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}
	
	private void case3() throws SQLException {
		DatabaseConnection.disconnect();
		DatabaseConnection.connectt();
		String[] columns = new String[] {
				"AircraftId","Aircraft Model","Status","Seat Size"
		};
		List<List<Object>> rows = new ArrayList<>();
		ResultSet result = null;
		result = DatabaseConnection.query( "SELECT * FROM Aircrafts");
		while(result.next()) {
			List<Object> row = new ArrayList<>();
			row.add(result.getInt("Id"));
			row.add(result.getString("planeModel"));
			row.add(result.getString("status"));
			row.add(result.getInt("maximumSeatAmount"));
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
		JLabel lblHeading = new JLabel("Aircrafts");
        lblHeading.setFont(new Font("Arial",Font.TRUETYPE_FONT,24));
        pageContent.setLayout(new BorderLayout());
        
        pageContent.add(lblHeading,BorderLayout.PAGE_START);
        pageContent.add(scrollPane,BorderLayout.CENTER);
        pageContent.setSize(550, 200);

        content = new JPanel();
        content.setLayout(new BorderLayout());
        content.add(navigator,BorderLayout.PAGE_START);
        content.add(pageContent,BorderLayout.CENTER);
        
        frame.setContentPane(content);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}

	private void case4() throws SQLException {
		DatabaseConnection.disconnect();
		DatabaseConnection.connectt();
		extraFrame = new JFrame();
		extraFrame.setSize(700,400);
		
		extraFrame.getContentPane().setLayout(new FlowLayout());
		extraFrame.setLayout(new BorderLayout());
		
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());
		
		
		JPanel infoPanel = new JPanel();
		JComboBox routes = new JComboBox();
		ResultSet routeQ = DatabaseConnection.query( "SELECT * FROM Routes");
		while(routeQ.next()) {
			int routeId = routeQ.getInt("RouteId");
			int fromCityId = routeQ.getInt("FromCityId");
			String fromCity = "";
			ResultSet fromCityQ = DatabaseConnection.query( "SELECT * FROM Cities WHERE cityId="+fromCityId+";");
			while(fromCityQ.next()) {
				fromCity = fromCityQ.getString("City");
			}
			fromCityQ.close();
			int toCityId = routeQ.getInt("TargetCityId");
			String toCity = "";
			ResultSet toCityQ = DatabaseConnection.query( "SELECT * FROM Cities WHERE cityId="+toCityId+";");
			while(toCityQ.next()) {
				toCity = toCityQ.getString("City");
			}
			toCityQ.close();
			routes.addItem(routeId + " " + fromCity + " " + toCity);
		}
		routeQ.close();
		infoPanel.add(routes);
		
		JComboBox aircrafts = new JComboBox();
		ResultSet aircraftQ = DatabaseConnection.query( "SELECT * FROM `Aircrafts` WHERE `status` LIKE 'ready'");
		while(aircraftQ.next()) {
			int aircraftId = aircraftQ.getInt("Id");
			String planeModel = aircraftQ.getString("planeModel");
			int seatAmount = aircraftQ.getInt("maximumSeatAmount");
			aircrafts.addItem(aircraftId + " " + planeModel + " " + seatAmount);
		}
		aircraftQ.close();
		infoPanel.add(aircrafts);
		
		JPanel centerPanel = new JPanel();
		
		JPanel datePanel = new JPanel();
		JPanel timePanel = new JPanel();

		JLabel datelabel = new JLabel("Date [YYYY-MM-DD] :");
		JLabel timelabel = new JLabel("Time [HH:MM] :");
		
		invalidDate = new JLabel("Invalid Date Format ");
		invalidTime = new JLabel("Invalid Time Format ");
		invalidDate.setVisible(false);
		invalidTime.setVisible(false);
		
		JTextField dateinput = new JTextField();
		JTextField timeinput = new JTextField();
		dateinput.setColumns(10);
		timeinput.setColumns(5);
		
		
		datePanel.add(datelabel);
		datePanel.add(dateinput);
		datePanel.add(invalidDate);
		datePanel.setLayout(new FlowLayout());
		datePanel.setMaximumSize(datePanel.getPreferredSize());
		
		timePanel.add(timelabel);
		timePanel.add(timeinput);
		timePanel.add(invalidTime);
		timePanel.setLayout(new FlowLayout());
		timePanel.setMaximumSize(timePanel.getPreferredSize());
		
		centerPanel.add(datePanel);
		centerPanel.add(timePanel);
		
		JButton createBtn = new JButton("Create");
		createBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean condition1 = false;
				boolean condition2 = false;
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String dateText = dateinput.getText();
				try {
					format.parse(dateText);
					invalidDate.setVisible(false);
					condition1 = true;
				} catch (ParseException e1) {
					System.out.println("Hey");
					invalidDate.setVisible(true);
				}
				
				String timeRegex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
				Pattern pattern = Pattern.compile(timeRegex);
				
				String timeText = timeinput.getText();
				Matcher matcher = pattern.matcher(timeText);
				if(matcher.matches()) {
					invalidTime.setVisible(false);
					condition2 = true;
				} else {
					System.out.println("Hey time");
					invalidTime.setVisible(true);
				}
				
				if(condition1 && condition2) {
					String queryString = "INSERT INTO `Sessions` (`SessionId`, "
							+ "`AircraftId`, `RouteId`, `Date`, `Time`, "
							+ "`RegisteredCustomerAmount`) VALUES (NULL,'";
					String route = (String) routes.getSelectedItem();
					String aircraft = (String) aircrafts.getSelectedItem();
					String a = route.substring(0,1);
					String b = aircraft.substring(0,1);
					queryString += Integer.parseInt(a);
					queryString +="','";
					queryString += Integer.parseInt(b);
					queryString +="','";
					queryString += dateText;
					queryString += "','";
					queryString += timeText;
					queryString += "','0');";
					try {
						DatabaseConnection.updateQuery(queryString);
						extraFrame.setVisible(false);
						extraFrame.dispose();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		
		centerPanel.add(createBtn);
		
		centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.PAGE_AXIS));
		content.add(centerPanel,BorderLayout.CENTER);
		content.add(infoPanel,BorderLayout.WEST);
		extraFrame.add(content);
		extraFrame.setVisible(true);
		extraFrame.pack();
	}

	class ShowSessions extends AbstractAction{
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
	
	class ShowAssignedSessions extends AbstractAction{
		private static final long serialVersionUID = 1L;
		public ShowAssignedSessions(String text) {super(text);}
		public void actionPerformed(ActionEvent arg0) {
			try {
				setContent(2);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	class ShowAircrafts extends AbstractAction{
		private static final long serialVersionUID = 1L;
		public ShowAircrafts(String text) {super(text);}
		public void actionPerformed(ActionEvent e) {
			try {
				setContent(3);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	class CreateSession extends AbstractAction{
		private static final long serialVersionUID = 1L;
		public CreateSession(String text) {super(text);}
		public void actionPerformed(ActionEvent e) {
			try {
				setContent(4);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
}
