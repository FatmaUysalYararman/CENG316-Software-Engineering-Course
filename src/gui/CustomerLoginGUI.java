package gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import controller.LoginController;
import database.DatabaseConnection;

public class CustomerLoginGUI extends LoginGUI {
	
	public CustomerLoginGUI(Statement statement,LoginController lc) {
		super(lc);
		setVisible(true);
	}

	public void checkLogin(String username, String password) throws SQLException {
		ResultSet result = null;
		result = DatabaseConnection.query("SELECT * FROM Customers WHERE customerUsername LIKE '"+username+"' AND customerPassword LIKE'"+password+"';");
		String userName = null;
		Integer id = 0;
		while(result.next()) {
			userName = result.getString("customerUsername");
			id = result.getInt("customerId");
		}
		result.close();
		if(userName==null) {
			invalidLogin();
		} else {
			setVisible(false);
			dispose();
			lc.setPersonId(id);
			lc.setUsername(userName);
			lc.advance();
		}
	}
	
}
