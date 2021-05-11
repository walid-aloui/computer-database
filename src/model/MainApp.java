package model;

import java.sql.SQLException;

import exception.InconsistentStateException;
import ui.CommandLineInterface;

public class MainApp {

	public static void main(String[] args) throws InconsistentStateException {
		try {
			CommandLineInterface cli = new CommandLineInterface();
			cli.ShowMenu();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}