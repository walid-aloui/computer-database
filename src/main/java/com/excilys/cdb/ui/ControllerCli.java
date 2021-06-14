package com.excilys.cdb.ui;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.stereotype.Controller;

import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Company.CompanyBuilder;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Computer.ComputerBuilder;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.model.Page.PageBuilder;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.utils.SecureInputs;

@Controller
public class ControllerCli {

	private Scanner sc;
	private ViewCli viewCli;
	private ComputerService computerService;
	private CompanyService companyService;

	public ControllerCli(ViewCli viewCli, ComputerService computerService, CompanyService companyService) {
		this.sc = new Scanner(System.in);
		this.viewCli = viewCli;
		this.computerService = computerService;
		this.companyService = companyService;
	}

	public void executeOption(int opt) throws OpenException, MapperException, ExecuteQueryException {
		ChoixMenu choice = ChoixMenu.toChoixMenu(opt);
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

		case DELETE_COMPANY:
			executeDeleteCompany();
			break;

		default:
			break;
		}
	}

	private void executeListCompanies() throws OpenException, MapperException, ExecuteQueryException {
		LinkedList<Company> allCompanies = companyService.getAllCompanies();
		viewCli.showCompanies(allCompanies);
	}

	private void executeListComputers() throws OpenException, MapperException, ExecuteQueryException {
		int numberOfComputer = computerService.getNumberOfComputer();
		int numComputerPerPage = Page.getDefaultNumElement();
		int totalPage = (int) Math.ceil((double) numberOfComputer / numComputerPerPage);
		int numPage = 1;
		Page p = new PageBuilder().withNumPage(numPage).withTotalPage(totalPage).build();
		while (true) {
			LinkedList<Computer> contenue = computerService.getPartOfComputers(Page.getDefaultNumElement(),
					(p.getNumPage() - 1) * numComputerPerPage);
			p.setContenue(contenue);
			viewCli.showPage(p);
			String choice = askPage(p.getNumPage(), p.getTotalPage());
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
		Optional<Computer> c = computerService.getComputerById(id);
		if (c.isPresent()) {
			System.out.println(c.get());
		} else {
			System.out.println("Id non existant !");
		}
	}

	private void executeUpdateComputer() throws OpenException {
		int id = askComputerId();
		String newName = askComputerName();
		LocalDate newIntroduced = askComputerIntroduced().orElse(null);
		LocalDate newDiscontinued = askComputerDiscontinued(newIntroduced).orElse(null);
		int newCompanyId = askCompanyId();

		Company company = null;
		if (newCompanyId != 0) {
			company = new CompanyBuilder().withId(newCompanyId).build();
		}
		Computer computer = new ComputerBuilder().withName(newName).withIntroduced(newIntroduced)
				.withDiscontinued(newDiscontinued).withCompany(company).build();

		int numUpdate = computerService.updateComputerById(id, computer);
		System.out.println("Nombre de update : " + numUpdate);
	}

	private void executeInsertComputer() throws OpenException {
		String name = askComputerName();
		LocalDate introduced = askComputerIntroduced().orElse(null);
		LocalDate discontinued = askComputerDiscontinued(introduced).orElse(null);
		int company_id = askCompanyId();

		Company company = null;
		if (company_id != 0) {
			company = new CompanyBuilder().withId(company_id).build();
		}
		Computer computer = new ComputerBuilder().withName(name).withIntroduced(introduced)
				.withDiscontinued(discontinued).withCompany(company).build();

		int numInsert = computerService.insertComputer(computer);
		if (numInsert == 1) {
			System.out.println("Insertion Reussie");
		} else {
			System.out.println("Echec Insertion");
		}
	}

	private void executeDeleteComputer() throws OpenException, ExecuteQueryException {
		int id = askComputerId();
		int numDelete = computerService.deleteComputerById(id);
		System.out.println("Nombre de suppression : " + numDelete);
	}

	private void executeDeleteCompany() throws OpenException, ExecuteQueryException {
		int id = askCompanyId();
		int numDelete = companyService.deleteCompanyById(id);
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

	private int askCompanyId() {
		System.out.println("Veuillez entrer l'id du fabricant");
		String input = sc.nextLine();
		if ("".equals(input)) {
			return 0;
		}
		if (!SecureInputs.isInteger(input)) {
			System.out.println("Veuillez entrer un entier !");
			return askCompanyId();
		}
		return Integer.parseInt(input);
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

	private Optional<LocalDate> askComputerDiscontinued(LocalDate introduced) {
		System.out.println("Veuillez entrer la date d'arret");
		Optional<LocalDate> discontinued = askComputerDate();
		if (introduced == null || !discontinued.isPresent() || discontinued.get().isAfter(introduced)) {
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

	public ViewCli getViewCli() {
		return viewCli;
	}

	public void setSc(Scanner sc) {
		this.sc = sc;
	}

}