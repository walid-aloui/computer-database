package ui;

import java.sql.SQLException;
import java.util.Scanner;

import exception.InconsistentStateException;
import utils.SecureInputs;

public class CommandLineInterface {

	private Controller controller;

	// Constructeur

	public CommandLineInterface() {
		this.controller = new Controller();
	}

	// Methode qui permet d'afficher le menu

	public void runCli() throws SQLException, InconsistentStateException {
		Scanner sc = new Scanner(System.in);
		int opt = 0;
		while (opt != ChoixMenu.QUIT.getNumber()) {
			do {
				controller.getView().showMenu();
				String input = sc.nextLine();
				if (!SecureInputs.isInteger(input)) {
					System.out.println("Veuillez entrez un chiffre !");
					runCli();
				}
				opt = Integer.parseInt(input);
				if (!SecureInputs.isChoixMenu(opt))
					System.out.println("Option invalide !");
			} while (!SecureInputs.isChoixMenu(opt));

			controller.executeOption(opt);
		}
		sc.close();
	}

}