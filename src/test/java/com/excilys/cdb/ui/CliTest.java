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
import com.excilys.cdb.model.Computer;

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
		DaoCompany.setDaoCompany(null);
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
			listComputers.add(new Computer(k, "computer_" + k, null, null, null));
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

		DaoCompany mockDaoCompany = mock(DaoCompany.class);
		LinkedList<Company> allCompanies = new LinkedList<Company>();
		allCompanies.add(new Company(1, "company_one"));
		allCompanies.add(new Company(2, "company_two"));
		allCompanies.add(new Company(3, "company_three"));
		try {
			when(mockDaoCompany.getAllCompanies()).thenReturn(allCompanies);
			DaoCompany.setDaoCompany(mockDaoCompany);
			cli.runCli();
			verify(mockDaoCompany, times(1)).getAllCompanies();
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

		final int numElements = 10;
		final int offset = 0;
		DaoComputer mockDaoComputer = mock(DaoComputer.class);
		LinkedList<Computer> listComputers = createListOfTenComputers();
		try {
			when(mockDaoComputer.getPartOfComputers(numElements, offset)).thenReturn(listComputers);
			when(mockDaoComputer.getNumberOfComputer()).thenReturn(numElements);
			DaoComputer.setDaoComputer(mockDaoComputer);
			cli.runCli();
			verify(mockDaoComputer, times(1)).getPartOfComputers(numElements, offset);
			verify(mockDaoComputer, times(1)).getNumberOfComputer();
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

		DaoComputer mockDaoComputer = mock(DaoComputer.class);
		Computer computer = new Computer(computerId, "Computer_WR7", null, null, null);
		try {
			when(mockDaoComputer.getComputerById(computerId)).thenReturn(Optional.of(computer));
			DaoComputer.setDaoComputer(mockDaoComputer);
			cli.runCli();
			verify(mockDaoComputer, times(1)).getComputerById(computerId);
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

		DaoComputer mockDaoComputer = mock(DaoComputer.class);
		try {
			when(mockDaoComputer.getComputerById(computerId)).thenReturn(Optional.empty());
			DaoComputer.setDaoComputer(mockDaoComputer);
			cli.runCli();
			verify(mockDaoComputer, times(1)).getComputerById(computerId);
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

		DaoComputer mockDaoComputer = mock(DaoComputer.class);
		try {
			when(mockDaoComputer.updateComputerById(computerId, name, Optional.empty(), Optional.empty(),
					Optional.empty())).thenReturn(1);
			DaoComputer.setDaoComputer(mockDaoComputer);
			cli.runCli();
			verify(mockDaoComputer, times(1)).updateComputerById(computerId, name, Optional.empty(), Optional.empty(),
					Optional.empty());
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

		DaoComputer mockDaoComputer = mock(DaoComputer.class);
		try {
			when(mockDaoComputer.insertComputer(name, Optional.empty(), Optional.empty(), Optional.empty()))
					.thenReturn(1);
			DaoComputer.setDaoComputer(mockDaoComputer);
			cli.runCli();
			verify(mockDaoComputer, times(1)).insertComputer(name, Optional.empty(), Optional.empty(),
					Optional.empty());
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

		DaoComputer mockDaoComputer = mock(DaoComputer.class);
		try {
			when(mockDaoComputer.deleteComputerById(computerId)).thenReturn(1);
			DaoComputer.setDaoComputer(mockDaoComputer);
			cli.runCli();
			verify(mockDaoComputer, times(1)).deleteComputerById(computerId);
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}

		// TODO : Verifier la sortit sur console
	}
}
