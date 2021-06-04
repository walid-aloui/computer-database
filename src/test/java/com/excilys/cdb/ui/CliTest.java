package com.excilys.cdb.ui;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.excilys.cdb.daos.DaoCompany;
import com.excilys.cdb.daos.DaoComputer;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Company.CompanyBuilder;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Computer.ComputerBuilder;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

class CliTest {

	private static Cli cli;
	private static ControllerCli controllerCli;
	private static InputStream oldInputStream;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		cli = new Cli();
		controllerCli = cli.getControllerCli();
		oldInputStream = System.in;
	}

	@AfterEach
	void tearDown() throws Exception {
		CompanyService.setCompanyService(null);
		System.setIn(oldInputStream);
		Scanner scanner = new Scanner(System.in);
		cli.setSc(scanner);
		controllerCli.setSc(scanner);
	}

	private void changeNextLineCli(String userInput) {
		ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
		System.setIn(bais);
		Scanner scanner = new Scanner(System.in);
		cli.setSc(scanner);
	}

	private void changeNextLineController(String userInput) {
		ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
		System.setIn(bais);
		Scanner scanner = new Scanner(System.in);
		controllerCli.setSc(scanner);
	}

	private LinkedList<Computer> createListOfTenComputers() {
		LinkedList<Computer> listComputers = new LinkedList<Computer>();
		for (int k = 0; k < 10; k++) {
			listComputers.add(new ComputerBuilder().withId(k).withName("computer_" + k).build());
		}
		return listComputers;
	}

	@Test
	void testRunCliShouldQuitApplication() {
		String choiceMenu = "7";
		changeNextLineCli(choiceMenu);
		try {
			cli.runCli();
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}

		// TODO : Verifier la sortit sur console
	}

	@Test
	void testRunCliShouldDisplayListOfCompanies() {
		String choiceMenu = "1\n7";
		changeNextLineCli(choiceMenu);

		CompanyService mockCompanyService = mock(CompanyService.class);
		LinkedList<Company> allCompanies = new LinkedList<Company>();
		allCompanies.add(new CompanyBuilder().withId(1).withName("Company_one").build());
		allCompanies.add(new CompanyBuilder().withId(2).withName("Company_two").build());
		allCompanies.add(new CompanyBuilder().withId(3).withName("Company_three").build());
		try {
			when(mockCompanyService.getAllCompanies()).thenReturn(allCompanies);
			CompanyService.setCompanyService(mockCompanyService);
			cli.runCli();
			verify(mockCompanyService, times(1)).getAllCompanies();
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}

		// TODO : Verifier la sortit sur console
	}

	@Test
	void testRunCliShouldDisplayListOfComputers() {
		String choiceMenu = "2\n7";
		changeNextLineCli(choiceMenu);
		String userInput = "q";
		changeNextLineController(userInput);

		final int numElements = Page.getDefaultNumElement();
		final int offset = 0;
		ComputerService mockComputerService = mock(ComputerService.class);
		LinkedList<Computer> listComputers = createListOfTenComputers();
		try {
			when(mockComputerService.getPartOfComputers(numElements, offset)).thenReturn(listComputers);
			when(mockComputerService.getNumberOfComputer()).thenReturn(numElements);
			ComputerService.setComputerService(mockComputerService);
			cli.runCli();
			verify(mockComputerService, times(1)).getPartOfComputers(numElements, offset);
			verify(mockComputerService, times(1)).getNumberOfComputer();
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}

		// TODO : Verifier la sortit sur console
	}

	@Test
	void testRunCliShouldDisplayDetailsOfComputer() {
		String choiceMenu = "3\n7";
		changeNextLineCli(choiceMenu);
		int computerId = 1;
		changeNextLineController(String.valueOf(computerId));

		ComputerService mockComputerService = mock(ComputerService.class);
		Computer computer = new ComputerBuilder().withId(computerId).withName("Computer_WR7").build();
		try {
			when(mockComputerService.getComputerById(computerId)).thenReturn(Optional.of(computer));
			ComputerService.setComputerService(mockComputerService);
			cli.runCli();
			verify(mockComputerService, times(1)).getComputerById(computerId);
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}

		// TODO : Verifier la sortit sur console
	}

	@Test
	void testRunCliShouldDisplayIdDoesntExist() {
		String choiceMenu = "3\n7";
		changeNextLineCli(choiceMenu);
		int computerId = 999;
		changeNextLineController(String.valueOf(computerId));

		ComputerService mockComputerService = mock(ComputerService.class);
		try {
			when(mockComputerService.getComputerById(computerId)).thenReturn(Optional.empty());
			ComputerService.setComputerService(mockComputerService);
			cli.runCli();
			verify(mockComputerService, times(1)).getComputerById(computerId);
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}

		// TODO : Verifier la sortit sur console
	}

	@Test
	void testRunCliWithUpdateChoice() {
		String choiceMenu = "4\n7";
		changeNextLineCli(choiceMenu);

		int computerId = 1;
		String name = "test";
		String introduced = "";
		String discontinued = "";
		String company_id = "";
		String userInputs = computerId + "\n" + name + "\n" + introduced + "\n" + discontinued + "\n" + company_id
				+ "\n";
		changeNextLineController(userInputs);
		
		Computer computer = new ComputerBuilder().withName(name).build();
		ComputerService mockComputerService = mock(ComputerService.class);
		try {
			when(mockComputerService.updateComputerById(computerId, computer)).thenReturn(1);
			ComputerService.setComputerService(mockComputerService);
			cli.runCli();
			verify(mockComputerService, times(1)).updateComputerById(computerId, computer);
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}

		// TODO : Verifier la sortit sur console
	}

	@Test
	void testRunCliWithInsertChoice() {
		String choiceMenu = "5\n7";
		changeNextLineCli(choiceMenu);

		String name = "test";
		String introduced = "";
		String discontinued = "";
		String company_id = "";
		String userInputs = name + "\n" + introduced + "\n" + discontinued + "\n" + company_id + "\n";
		changeNextLineController(userInputs);

		ComputerService mockComputerService = mock(ComputerService.class);
		try {
			Computer computer = new ComputerBuilder().withName(name).build();
			when(mockComputerService.insertComputer(computer)).thenReturn(1);
			ComputerService.setComputerService(mockComputerService);
			cli.runCli();
			verify(mockComputerService, times(1)).insertComputer(computer);
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}

		// TODO : Verifier la sortit sur console
	}

	@Test
	void testRunCliWithDeleteChoice() {
		String choiceMenu = "6\n7";
		changeNextLineCli(choiceMenu);

		int computerId = 1;
		changeNextLineController(String.valueOf(computerId));

		ComputerService mockComputerService = mock(ComputerService.class);
		try {
			when(mockComputerService.deleteComputerById(computerId)).thenReturn(1);
			ComputerService.setComputerService(mockComputerService);
			cli.runCli();
			verify(mockComputerService, times(1)).deleteComputerById(computerId);
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}

		// TODO : Verifier la sortit sur console
	}
}
