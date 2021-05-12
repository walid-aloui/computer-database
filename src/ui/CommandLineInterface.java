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

	public void showMenu() throws SQLException, InconsistentStateException {
		Scanner sc = new Scanner(System.in);
		int opt = 0;
		while (opt != ChoixMenu.QUIT.getNumber()) {
			do {
				System.out.println("\nVeuillez selectionner l'option souhaite");
				System.out.println("1- Afficher la liste des fabriquants");
				System.out.println("2- Afficher la liste des ordinateurs");
				System.out.println("3- Afficher les details d'un ordinateur");
				System.out.println("4- Mettre a jour un ordinateur");
				System.out.println("5- Creer un ordinateur");
				System.out.println("6- Supprimer un ordinateur");
				System.out.println("7- Quitter\n");
				if (sc.hasNextInt()) {
					opt = sc.nextInt();
				} else {
					System.out.println("Veuillez entrez un chiffre !");
					showMenu();
				}
				if (!SecureInputs.isChoixMenu(opt))
					System.out.println("Option invalide !");
			} while (!SecureInputs.isChoixMenu(opt));

			executeOption(opt);
		}
		sc.close();
	}

	// Methode qui permet d'executer l'option selectionner

	private void executeOption(int opt) throws SQLException, InconsistentStateException {
		ChoixMenu choice = ChoixMenu.values()[opt - 1];
		switch (choice) {
		case LIST_COMPANIES:
			LinkedList<Company> allCompanies = DaoCompany.create().getAllCompanies();
			showCompanies(allCompanies);
			break;

		case LIST_COMPUTERS:
			LinkedList<Computer> allComputers = DaoComputer.create().getAllComputers();
			showComputers(allComputers);
			break;

		case SHOW_DETAILS_COMPUTER:
			int id = getComputerId();
			Computer c = DaoComputer.create().getComputerById(id);
			System.out.println(c);
			break;

		case UPDATE_COMPUTER:
			int id2 = getComputerId();
			String newName = getComputerName();
			System.out.println("Veuillez entrer la nouvelle date d'introduction");
			String newIntroduced = getComputerDate();
			System.out.println("Veuillez entrer la nouvelle date d'arret");
			String newDiscontinued = getComputerDate();
			String newCompanyId = getCompanyId();
			DaoComputer.create().updateComputerById(id2, newName, newIntroduced, newDiscontinued, newCompanyId);
			break;

		case INSERT_COMPUTER:
			String name = getComputerName();
			System.out.println("Veuillez entrer la date d'introduction");
			String introduced = getComputerDate();
			System.out.println("Veuillez entrer la date d'arret");
			String discontinued = getComputerDate();
			String company_id = getCompanyId();
			DaoComputer.create().insertComputer(name, introduced, discontinued, company_id);
			break;

		case DELETE_COMPUTER:
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

	// Methode qui permet demander l'id de l'ordinateur a l'utilisateur

	private int getComputerId() throws SQLException, InconsistentStateException {
		System.out.println("Veuillez entrer l'id de l'ordinateur");
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();
		if (!SecureInputs.isInteger(input)) {
			System.out.println("Veuillez entrer un entier !");
			return getComputerId();
		}
		int id = Integer.parseInt(input);
		if (!SecureInputs.isComputer(id)) {
			System.out.println("id de l'ordinateur incorrect !");
			return getComputerId();
		}
		return id;
	}

	// Methode qui permet demander l'id du fabricant a l'utilisateur

	private String getCompanyId() throws SQLException, InconsistentStateException {
		System.out.println("Veuillez entrer l'id du fabricant");
		Scanner sc = new Scanner(System.in);
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

	// Methode qui permet demander le nom de l'ordinateur a l'utilisateur

	private String getComputerName() {
		System.out.println("Entrez le nom de l'ordinateur (obligatoire)");
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();
		if (input.equals("")) {
			System.out.println("Le nom est obligatoire !");
			return getComputerName();
		}
		return input;
	}

	// Methode qui permet demander la date de l'ordinateur a l'utilisateur

	private String getComputerDate() {
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();
		if (input.equals("") || SecureInputs.isDate(input))
			return input;
		System.out.println("Format date invalide !");
		return getComputerDate();
	}

}