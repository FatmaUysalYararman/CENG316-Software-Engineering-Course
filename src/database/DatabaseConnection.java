package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.simple.parser.ParseException;

import dataaccess.MySQLSettings;

public class DatabaseConnection {
	
	private static String url;
	private static String username;
	private static String password;
	private static Statement statement;
	private static Connection connection;

	
	public DatabaseConnection() {
		mySQLConnect();
	}
	
	private void mySQLConnect() {
		try {
			MySQLSettings.parse();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		String[] infoArr = MySQLSettings.getInfo();
		try {
			statement = connect(infoArr[0], infoArr[1], infoArr[2]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Statement connect(String urll, String usernamee, String passwordd) throws SQLException {
		url = urll;
		username = usernamee;
		password = passwordd;
		System.out.println("Connecting database ...");
		connection = DriverManager.getConnection(url, username, password);
		System.out.println("Database connected!");
		Statement statement = connection.createStatement();
		return statement;
	}
	
	public static void connectt() throws SQLException {
		connection = DriverManager.getConnection(url,username,password);
	}
	
	public static void disconnect() throws SQLException {
		connection.close();
	}
	
	public static Statement getNewStatement() throws SQLException {
		Statement statement = connection.createStatement();
		return statement;
	}

	public static ResultSet query(String query) throws SQLException {
		return getNewStatement().executeQuery(query);
	}
	
	public static void updateQuery(String query) throws SQLException {
		getNewStatement().executeUpdate(query);
	}
			
	public Statement getStatement() {
		return statement;
	}
}
