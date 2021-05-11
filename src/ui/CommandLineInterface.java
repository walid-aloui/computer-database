package ui;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Scanner;

import daos.DaoCompany;
import daos.DaoComputer;
import exception.InconsistentStateException;
import model.Company;
import model.Computer;
import utils.SecureInputs;

public class CommandLineInterface {

	// Methode qui permet d'afficher le menu

	public void ShowMenu() throws SQLException, InconsistentStateException {
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
				if (sc.hasNextShort()) {
					opt = sc.nextShort();
				} else {
					System.out.println("Veuillez entrez un chiffre !");
					ShowMenu();
				}
				if (opt < 1 || opt > 7)
					System.out.println("Option invalide !");
			} while (opt < 1 || opt > 7);

			executeOption(opt);
		}
		sc.close();
	}

	// Methode qui permet d'executer l'option selectionner

	private void executeOption(short opt) throws SQLException, InconsistentStateException {
		switch (opt) {
		case 1:
			LinkedList<Company> allCompanies = DaoCompany.create().getAllCompanies();
			showCompanies(allCompanies);
			break;

		case 2:
			LinkedList<Computer> allComputers = DaoComputer.create().getAllComputers();
			showComputers(allComputers);
			break;

		case 3:
			int id = getComputerId();
			Computer c = DaoComputer.create().getComputerById(id);
			System.out.println(c);
			break;

		case 4:
			int id2 = getComputerId();
			String newName = getComputerName();
			System.out.println("Veuillez entrer la nouvelle date d'introduction");
			String newIntroduced = getComputerDate();
			System.out.println("Veuillez entrer la nouvelle date d'arret");
			String newDiscontinued = getComputerDate();
			String newCompanyId = getCompanyId();
			DaoComputer.create().updateComputerById(id2, newName, newIntroduced, newDiscontinued, newCompanyId);
			break;

		case 5:
			String name = getComputerName();
			System.out.println("Veuillez entrer la date d'introduction");
			String introduced = getComputerDate();
			System.out.println("Veuillez entrer la date d'arret");
			String discontinued = getComputerDate();
			String company_id = getCompanyId();
			DaoComputer.create().insertComputer(name, introduced, discontinued, company_id);
			break;

		case 6:
			int id3 = getComputerId();
			DaoComputer.create().deleteComputerById(id3);
			break;

		default:
			break;
		}
	}

	// Methode qui permet d'afficher une liste de fabricant

	private void showCompanies(LinkedList<Company> companies) {
		for (Company c : companies) {
			System.out.println(c);
		}
	}

	// Methode qui permet d'afficher une liste d'ordinateur

	private void showComputers(LinkedList<Computer> computers) {
		for (Computer c : computers) {
			System.out.println(c);
		}
	}

	private int getComputerId() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Veuillez entrer l'id de l'ordinateur");
		String input = sc.nextLine();
		if (SecureInputs.isInteger(input))
			return Integer.parseInt(input);
		System.out.println("Veuillez entrer un entier !");
		return getComputerId();
	}

	private String getCompanyId() throws SQLException, InconsistentStateException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Veuillez entrer l'id du fabricant");
		String input = sc.nextLine();
		if (input.equals(""))
			return input;
		if (!SecureInputs.isInteger(input)) {
			System.out.println("Veuillez entrer un entier !");
			return getCompanyId();
		}
		int id = Integer.parseInt(input);
		if (!SecureInputs.isCompany(id)) {
			System.out.println("Id de fabricant incorrect !");
			return getCompanyId();
		}
		return input;
	}

	private String getComputerName() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Entrez le nom de l'ordinateur (obligatoire)");
		String input = sc.nextLine();
		if (input.equals("")) {
			System.out.println("Le nom est obligatoire !");
			return getComputerName();
		}
		return input;
	}

	private String getComputerDate() {
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();
		if (input.equals("") || SecureInputs.isDate(input))
			return input;
		System.out.println("Format date invalide !");
		return getComputerDate();
	}

}