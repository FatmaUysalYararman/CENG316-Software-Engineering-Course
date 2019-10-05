package controller;

import bis.SystemData;
import gui.MainGUI;

public class MainController {

	private SystemData data;
	private MainGUI view;

	public MainController(SystemData data,MainGUI view) {
		this.data = data;
		this.view = view;
	}
	
}
