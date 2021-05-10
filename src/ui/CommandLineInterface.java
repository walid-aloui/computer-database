package ui;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import model.Database;

public class CommandLineInterface {

	// Methode qui permet d'afficher le menu

	public static void ShowMenu() throws SQLException {
		Scanner sc = new Scanner(System.in);
		short opt = 0;
		while (opt != 7) {
			do {
				System.out.println("\nVeuillez selectionner l'option souhaite");
				System.out.println("1- Afficher la liste des fabriquants");
				System.out.println("2- Afficher la liste des ordinateurs");
				System.out.println("3- Afficher les details d'un ordinateur");
				System.out.println("4- Mettre a jour un ordinateur");
				System.out.println("5- Creer un ordinateur");
				System.out.println("6- Supprimer un ordinateur");
				System.out.println("7- Quitter\n");
				try {
					opt = sc.nextShort();
				} catch (InputMismatchException e) {
					System.out.println("Veuillez entrez un chiffre !");
					ShowMenu();
				}
				if (opt < 1 || opt > 7)
					System.out.println("Option invalide !");
			} while (opt < 1 || opt > 7);

			CommandLineInterface.executeOption(opt);
		}
	}

	// Methode qui permet d'executer l'option selectionner

	private static void executeOption(short opt) throws SQLException {
		switch (opt) {
		case 1:
			Database.db.showCompanies();
			break;

		case 2:
			Database.db.showComputers();
			break;

		default:
			break;
		}
	}

}