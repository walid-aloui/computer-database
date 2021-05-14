package ui;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

import daos.DaoCompany;
import daos.DaoComputer;
import exception.InconsistentStateException;
import model.Company;
import model.Computer;
import utils.SecureInputs;

public class ControllerCli {

	// Attributs

	private ViewCli viewCli;
	private Scanner sc;

	// Constructeur

	public ControllerCli(Scanner sc) {
		this.viewCli = new ViewCli();
		this.sc = sc;
	}

	// Methode qui permet d'executer l'option selectionner

	void executeOption(int opt) throws SQLException, InconsistentStateException {
		ChoixMenu choice = ChoixMenu.values()[opt - 1];
		switch (choice) {
		case LIST_COMPANIES:
			executeListCompanies();
			break;

		case LIST_COMPUTERS:
			executeListComputers();
			break;

		case SHOW_DETAILS_COMPUTER:
			executeShowDetails();
			break;

		case UPDATE_COMPUTER:
			executeUpdateComputer();
			break;

		case INSERT_COMPUTER:
			executeInsertComputer();
			break;

		case DELETE_COMPUTER:
			executeDeleteComputer();
			break;

		default:
			break;
		}
	}

	// Methode qui permet d'executer l'option qui liste les fabricants

	private void executeListCompanies() throws SQLException {
		LinkedList<Company> allCompanies = DaoCompany.create().getAllCompanies();
		viewCli.showCompanies(allCompanies);
	}

	// Methode qui permet d'executer l'option qui liste les ordinateurs

	private void executeListComputers() throws SQLException {
		/*
		 * LinkedList<Computer> allComputers = DaoComputer.create().getAllComputers();
		 * viewCli.showComputers(allComputers);
		 */

		int numberOfComputer = DaoComputer.create().getNumberOfComputer();
		int totalPage = numberOfComputer / Page.getMAX_ELEMENT();
		if (numberOfComputer % Page.getMAX_ELEMENT() != 0)
			totalPage++;
		int numPage = 1;
		while (true) {
			Page p = new Page(numPage, totalPage);
			viewCli.showPage(p);
			String choice = askPage(numPage, p.getTotalPage());
			if (choice.equals("q"))
				break;
			else if (choice.equals("a"))
				numPage--;
			else
				numPage++;
		}
	}

	// Methode qui permet d'executer l'option qui affiche les details d'un
	// ordinateur

	private void executeShowDetails() throws SQLException, InconsistentStateException {
		int id = askComputerId();
		Computer c = DaoComputer.create().getComputerById(id);
		if (c == null)
			System.out.println("Id non existant !");
		else
			System.out.println(c);
	}

	// Methode qui permet d'executer l'option qui update un ordinateur

	private void executeUpdateComputer() throws SQLException, InconsistentStateException {
		int id = askComputerId();
		String newName = askComputerName();
		String newIntroduced = askComputerIntroduced();
		String newDiscontinued = askComputerDiscontinued(newIntroduced);
		String newCompanyId = askCompanyId();
		DaoComputer.create().updateComputerById(id, newName, newIntroduced, newDiscontinued, newCompanyId);
	}

	// Methode qui permet d'executer l'option qui insere un ordinateur

	private void executeInsertComputer() throws SQLException, InconsistentStateException {
		String name = askComputerName();
		String introduced = askComputerIntroduced();
		String discontinued = askComputerDiscontinued(introduced);
		String company_id = askCompanyId();
		DaoComputer.create().insertComputer(name, introduced, discontinued, company_id);
	}

	// Methode qui permet d'executer l'option qui supprime un ordinateur

	private void executeDeleteComputer() throws SQLException {
		int id = askComputerId();
		DaoComputer.create().deleteComputerById(id);
	}

	// Methode qui permet demander l'id de l'ordinateur a l'utilisateur

	private int askComputerId() {
		System.out.println("Veuillez entrer l'id de l'ordinateur");
		String input = sc.nextLine();
		if (!SecureInputs.isInteger(input)) {
			System.out.println("Veuillez entrer un entier !");
			return askComputerId();
		}
		return Integer.parseInt(input);
	}

	// Methode qui permet demander l'id du fabricant a l'utilisateur

	private String askCompanyId() throws SQLException, InconsistentStateException {
		System.out.println("Veuillez entrer l'id du fabricant");
		String input = sc.nextLine();
		if (input.equals(""))
			return input;
		if (!SecureInputs.isInteger(input)) {
			System.out.println("Veuillez entrer un entier !");
			return askCompanyId();
		}
		int id = Integer.parseInt(input);
		if (!DaoCompany.create().isCompany(id)) {
			System.out.println("Id de fabricant incorrect !");
			return askCompanyId();
		}
		return input;
	}

	// Methode qui permet demander le nom de l'ordinateur a l'utilisateur

	private String askComputerName() {
		System.out.println("Entrez le nom de l'ordinateur (obligatoire)");
		String input = sc.nextLine();
		if (input.equals("")) {
			System.out.println("Le nom est obligatoire !");
			return askComputerName();
		}
		return input;
	}

	// Methode qui permet demander la date de l'ordinateur a l'utilisateur

	private String askComputerDate() {
		String input = sc.nextLine();
		if (input.equals("") || SecureInputs.toDate(input) != null)
			return input;
		System.out.println("Format date invalide !");
		return askComputerDate();
	}

	// Methode qui permet de demander la date d'introduction

	private String askComputerIntroduced() {
		System.out.println("Veuillez entrer la date d'introduction");
		return askComputerDate();
	}

	// Methode qui permet de demander la date d'arret

	private String askComputerDiscontinued(String introduced) {
		System.out.println("Veuillez entrer la date d'arret");
		String discontinued = askComputerDate();
		if (introduced.equals("") || discontinued.equals(""))
			return discontinued;
		Date intro = SecureInputs.toDate(introduced);
		Date discon = SecureInputs.toDate(discontinued);
		if (discon.after(intro))
			return discontinued;
		System.out.println("La date d'arret doit etre plus grande que la date d'introduction !");
		return askComputerDiscontinued(introduced);
	}

	// Methode qui permet de demander si il veut aller a la page suivante ou a la
	// precedente

	public String askPage(int numPage, int totalPage) {
		if (numPage == 1)
			System.out.println("Taper z pour la page suivante\nq pour revenir au menu principal");
		else if (numPage == totalPage)
			System.out.println("Taper a pour la page précédente\nq pour revenir au menu principal");
		else
			System.out.println(
					"Taper a pour la page précédente et " + "z pour la suivante\nq pour revenir au menu principal");
		String choice = sc.nextLine();

		if (!SecureInputs.isValidPage(choice, numPage, totalPage)) {
			System.out.println("Erreur de saisie !");
			return askPage(numPage, totalPage);
		}

		return choice;
	}

	// Getter

	public ViewCli getView() {
		return viewCli;
	}

}
