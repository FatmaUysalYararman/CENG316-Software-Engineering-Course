package gui;

import java.sql.ResultSet;
import java.sql.SQLException;

import controller.LoginController;
import database.DatabaseConnection;

public class PersonelLoginGUI extends LoginGUI {

	public PersonelLoginGUI(LoginController lc) {
		super(lc);
		setVisible(true);
	}

	public void checkLogin(String username,String password) throws SQLException {
		ResultSet result = null;
		try {
			result = DatabaseConnection.query("SELECT * FROM Employees WHERE employeeUsername LIKE '"+username+"' AND employeePassword LIKE'"+password+"';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String userType = null;
		Integer id = 0;
		while(result.next()) {
				userType = result.getString("employeeType");
				id = result.getInt("employeeId");
		}
		result.close();
		if(userType==null) {
			invalidLogin();
		} else {
			setVisible(false);
			dispose();
			lc.setUserType(userType);
			lc.setUsername(username);
			lc.setPersonId(id);
			lc.advance();
		}	
	}
}
