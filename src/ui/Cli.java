package ui;

import java.sql.SQLException;
import java.util.Scanner;

import exception.InconsistentStateException;
import utils.SecureInputs;

public class Cli {

	private ControllerCli controllerCli;
	private Scanner sc;

	// Constructeur

	public Cli() {
		sc = new Scanner(System.in);
		this.controllerCli = new ControllerCli(sc);
	}

	// Methode qui permet d'afficher le menu

	public void runCli() throws SQLException, InconsistentStateException {
		int opt;
		do {
			opt = askChoice();
			controllerCli.executeOption(opt);
		} while (opt != ChoixMenu.QUIT.getNumber());
	}

	private int askChoice() {
		controllerCli.getView().showMenu();
		String input = sc.nextLine();
		if (!SecureInputs.isInteger(input)) {
			System.out.println("Veuillez entrez un chiffre !");
			return askChoice();
		}
		int opt = Integer.parseInt(input);
		if (!ChoixMenu.isChoixMenu(opt)) {
			System.out.println("Option invalide !");
			return askChoice();
		}
		return opt;
	}

}