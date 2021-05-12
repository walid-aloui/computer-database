package model;

import java.text.ParseException;

import exception.InconsistentStateException;
import ui.CommandLineInterface;

public class MainApp {

	public static void main(String[] args) throws ParseException {
		CommandLineInterface cli = new CommandLineInterface();
		try {
			cli.showMenu();
		} catch (InconsistentStateException e) {
			System.out.println("Erreur main " + e);
		}
	}

}