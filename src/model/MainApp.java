package model;

import java.sql.SQLException;

import exception.InconsistentStateException;
import ui.Cli;

public class MainApp {

	public static void main(String[] args) {
		Cli cli = new Cli();
		try {
			cli.runCli();
		} catch (InconsistentStateException e) {
			System.out.println("Erreur main Database " + e);
		} catch (SQLException e) {
			System.out.println("Erreur main SQL " + e);
		}
	}

}