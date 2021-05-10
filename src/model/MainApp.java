package model;

import java.sql.SQLException;

import ui.CommandLineInterface;

public class MainApp {

	public static void main(String[] args) {
		try {
			Database db = Database.create();
			CommandLineInterface.ShowMenu();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}