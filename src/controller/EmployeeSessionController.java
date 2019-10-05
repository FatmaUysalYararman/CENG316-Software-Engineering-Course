package controller;

import gui.PersonelSessionGUI;

public class EmployeeSessionController {

	private String username;
	private String specialUserType;
	private Integer personelId;

	public EmployeeSessionController(String username, String specialUserType,Integer personelId) {
		this.username = username;
		this.specialUserType = specialUserType;
		this.personelId = personelId;
		new PersonelSessionGUI(specialUserType,personelId);
	}

}
