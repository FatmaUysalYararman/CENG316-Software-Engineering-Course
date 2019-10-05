package controller;

import gui.CustomerSessionGUI;

public class CustomerSessionController {

	public CustomerSessionController(String username,Integer id) {
		new CustomerSessionGUI(null, id);
	}

}
