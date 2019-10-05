package controller;

import java.sql.Statement;

import gui.CustomerLoginGUI;
import gui.PersonelLoginGUI;

public class LoginController {
	
	private Statement statement;
	private String username;
	private String usertype;
	private String specialUserType;
	private Integer personId;
	
	public LoginController(String userType) {
		this.usertype = userType;
		this.username = "";
		
		switch (usertype) {
		case "employee":
			personelLogin();
			break;
		case "customer":
			customerLogin();
			break;
		case "guest":
			guestLogin();
			break;
		}	
	}
	
	public void setUsername(String name) {
		this.username = name;
	}
	
	public void setUserType(String userType) {
		this.specialUserType = userType;
	}
	
	public void setPersonId(Integer id) {
		this.personId = id;
	}
	
	public void advance() {
		switch (usertype) {
		case "employee":
			new EmployeeSessionController(username,specialUserType,personId);
			break;
		case "customer":
			new CustomerSessionController(username,personId);
			break;
		case "guest":
			new GuestSessionController();
			break;
		}
	}
	
	private void personelLogin() {
		new PersonelLoginGUI(this);
	}
	
	private void customerLogin() {
		new CustomerLoginGUI(statement,this);
	}
	
	private void guestLogin() {
		advance();
	}


}
