package app;

import gui.*;

import java.sql.SQLException;

import bis.SystemData;
import controller.MainController;
import database.DatabaseConnection;


public class App {
	public static void main(String[] args) throws SQLException {
		new App();	
	}
	
	public App() {
		new DatabaseConnection();
		
		MainGUI 	view		 = new MainGUI();
		SystemData 	data		 = new SystemData();
		
		new MainController(data,view);
		
		view.setVisible(true);
	}
}
