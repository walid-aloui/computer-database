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

public class Controller {

	// Attributs

	private View view;

	// Constructeur

	public Controller() {
		this.view = new View();
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
		view.showCompanies(allCompanies);
	}

	// Methode qui permet d'executer l'option qui liste les ordinateurs

	private void executeListComputers() throws SQLException {
		LinkedList<Computer> allComputers = DaoComputer.create().getAllComputers();
		view.showComputers(allComputers);
	}

	// Methode qui permet d'executer l'option qui affiche les details d'un
	// ordinateur

	private void executeShowDetails() throws SQLException, InconsistentStateException {
		int id = getComputerId();
		Computer c = DaoComputer.create().getComputerById(id);
		if (c == null)
			System.out.println("Id non existant !");
		else
			System.out.println(c);
	}

	// Methode qui permet d'executer l'option qui update un ordinateur

	private void executeUpdateComputer() throws SQLException, InconsistentStateException {
		int id = getComputerId();
		String newName = getComputerName();
		String newIntroduced = getComputerIntroduced();
		String newDiscontinued = getComputerDiscontinued(newIntroduced);
		String newCompanyId = getCompanyId();
		DaoComputer.create().updateComputerById(id, newName, newIntroduced, newDiscontinued, newCompanyId);
	}

	// Methode qui permet d'executer l'option qui insere un ordinateur

	private void executeInsertComputer() throws SQLException, InconsistentStateException {
		String name = getComputerName();
		String introduced = getComputerIntroduced();
		String discontinued = getComputerDiscontinued(introduced);
		String company_id = getCompanyId();
		DaoComputer.create().insertComputer(name, introduced, discontinued, company_id);
	}

	// Methode qui permet d'executer l'option qui supprime un ordinateur

	private void executeDeleteComputer() throws SQLException {
		int id = getComputerId();
		DaoComputer.create().deleteComputerById(id);
	}

	// Methode qui permet demander l'id de l'ordinateur a l'utilisateur

	private int getComputerId() {
		System.out.println("Veuillez entrer l'id de l'ordinateur");
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();
		if (!SecureInputs.isInteger(input)) {
			System.out.println("Veuillez entrer un entier !");
			return getComputerId();
		}
		return Integer.parseInt(input);
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
		if (input.equals("") || SecureInputs.toDate(input) != null)
			return input;
		System.out.println("Format date invalide !");
		return getComputerDate();
	}

	// Methode qui permet de demander la date d'introduction

	private String getComputerIntroduced() {
		System.out.println("Veuillez entrer la date d'introduction");
		return getComputerDate();
	}

	// Methode qui permet de demander la date d'arret

	private String getComputerDiscontinued(String introduced) {
		System.out.println("Veuillez entrer la date d'arret");
		String discontinued = getComputerDate();
		if (introduced.equals("") || discontinued.equals(""))
			return discontinued;
		Date intro = SecureInputs.toDate(introduced);
		Date discon = SecureInputs.toDate(discontinued);
		if (discon.after(intro))
			return discontinued;
		System.out.println("La date d'arret doit etre plus grande que la date d'introduction !");
		return getComputerDiscontinued(introduced);
	}

	// Getter

	public View getView() {
		return view;
	}

}
