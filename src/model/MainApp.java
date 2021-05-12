package model;

import exception.InconsistentStateException;
import ui.CommandLineInterface;

public class MainApp {

	public static void main(String[] args) {
		CommandLineInterface cli = new CommandLineInterface();
		try {
			cli.runCli();
		} catch (InconsistentStateException e) {
			System.out.println("Erreur main " + e);
		}
	}

}