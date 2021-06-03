package com.excilys.cdb.ui;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Scanner;

import com.excilys.cdb.daos.DaoCompany;
import com.excilys.cdb.daos.DaoComputer;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.model.Page.PageBuilder;
import com.excilys.cdb.utils.SecureInputs;

public class ControllerCli {

	private ViewCli viewCli;
	private Scanner sc;

	public ControllerCli(Scanner sc) {
		this.viewCli = new ViewCli();
		this.sc = sc;
	}

	public void executeOption(int opt) throws OpenException, MapperException, ExecuteQueryException {
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

	private void executeListCompanies() throws OpenException, MapperException, ExecuteQueryException {
		LinkedList<Company> allCompanies = DaoCompany.getInstance().getAllCompanies();
		viewCli.showCompanies(allCompanies);
	}

	private void executeListComputers() throws OpenException, MapperException, ExecuteQueryException {
		DaoComputer daoComputer = DaoComputer.getInstance();
		int numberOfComputer = daoComputer.getNumberOfComputer();
		int totalPage = (int) Math.ceil((double) numberOfComputer / Page.getDefaultNumElement());
		int numPage = 1;
		Page p = new PageBuilder().withNumPage(numPage).withTotalPage(totalPage).build();
		while (true) {
			LinkedList<Computer> contenue = daoComputer.getPartOfComputers(Page.getDefaultNumElement(),
					(p.getNumPage() - 1) * 10);
			p.setContenue(contenue);
			viewCli.showPage(p);
			String choice = askPage(numPage, p.getTotalPage());
			if ("q".equals(choice)) {
				break;
			} else if ("a".equals(choice)) {
				p.setNumPage(p.getNumPage() - 1);
			} else {
				p.setNumPage(p.getNumPage() + 1);
			}
		}
	}

	private void executeShowDetails() throws OpenException, MapperException, ExecuteQueryException {
		int id = askComputerId();
		Optional<Computer> c = DaoComputer.getInstance().getComputerById(id);
		if (c.isPresent()) {
			System.out.println(c.get());
		} else {
			System.out.println("Id non existant !");
		}
	}

	private void executeUpdateComputer() throws OpenException {
		int id = askComputerId();
		String newName = askComputerName();
		Optional<LocalDate> newIntroduced = askComputerIntroduced();
		Optional<LocalDate> newDiscontinued = askComputerDiscontinued(newIntroduced);
		Optional<String> newCompanyId = askCompanyId();
		int numUpdate = DaoComputer.getInstance().updateComputerById(id, newName, newIntroduced, newDiscontinued,
				newCompanyId);
		System.out.println("Nombre de update : " + numUpdate);
	}

	private void executeInsertComputer() throws OpenException {
		String name = askComputerName();
		Optional<LocalDate> introduced = askComputerIntroduced();
		Optional<LocalDate> discontinued = askComputerDiscontinued(introduced);
		Optional<String> company_id = askCompanyId();
		int numInsert = DaoComputer.getInstance().insertComputer(name, introduced, discontinued, company_id);
		if (numInsert == 1) {
			System.out.println("Insertion Reussie");
		} else {
			System.out.println("Echec Insertion");
		}
	}

	private void executeDeleteComputer() throws OpenException, ExecuteQueryException {
		int id = askComputerId();
		int numDelete = DaoComputer.getInstance().deleteComputerById(id);
		System.out.println("Nombre de suppression : " + numDelete);
	}

	private int askComputerId() {
		System.out.println("Veuillez entrer l'id de l'ordinateur");
		String input = sc.nextLine();
		if (!SecureInputs.isInteger(input)) {
			System.out.println("Veuillez entrer un entier !");
			return askComputerId();
		}
		return Integer.parseInt(input);
	}

	private Optional<String> askCompanyId() {
		System.out.println("Veuillez entrer l'id du fabricant");
		String input = sc.nextLine();
		if ("".equals(input)) {
			return Optional.empty();
		}
		if (!SecureInputs.isInteger(input)) {
			System.out.println("Veuillez entrer un entier !");
			return askCompanyId();
		}
		return Optional.of(input);
	}

	private String askComputerName() {
		System.out.println("Entrez le nom de l'ordinateur (obligatoire)");
		String input = sc.nextLine();
		if ("".equals(input)) {
			System.out.println("Le nom est obligatoire !");
			return askComputerName();
		}
		return input;
	}

	private Optional<LocalDate> askComputerDate() {
		String input = sc.nextLine();
		if ("".equals(input)) {
			return Optional.empty();
		}
		Optional<LocalDate> date = SecureInputs.toLocalDate(input);
		if (date.isPresent()) {
			return date;
		}
		System.out.println("Format date invalide !");
		return askComputerDate();
	}

	private Optional<LocalDate> askComputerIntroduced() {
		System.out.println("Veuillez entrer la date d'introduction");
		return askComputerDate();
	}

	private Optional<LocalDate> askComputerDiscontinued(Optional<LocalDate> introduced) {
		System.out.println("Veuillez entrer la date d'arret");
		Optional<LocalDate> discontinued = askComputerDate();
		if (!introduced.isPresent() || !discontinued.isPresent() || discontinued.get().isAfter(introduced.get())) {
			return discontinued;
		}
		System.out.println("La date d'arret doit etre plus grande que la date d'introduction !");
		return askComputerDiscontinued(introduced);
	}

	private String askPage(int numPage, int totalPage) {
		if (numPage == 1) {
			System.out.println("Taper z pour la page suivante\nq pour revenir au menu principal");
		} else if (numPage == totalPage) {
			System.out.println("Taper a pour la page précédente\nq pour revenir au menu principal");
		} else {
			System.out.println(
					"Taper a pour la page précédente et " + "z pour la suivante\nq pour revenir au menu principal");
		}
		String choice = sc.nextLine();
		if (!SecureInputs.isValidPage(choice, numPage, totalPage)) {
			System.out.println("Erreur de saisie !");
			return askPage(numPage, totalPage);
		}
		return choice;
	}

	public ViewCli getView() {
		return viewCli;
	}

	public void setSc(Scanner sc) {
		this.sc = sc;
	}

}