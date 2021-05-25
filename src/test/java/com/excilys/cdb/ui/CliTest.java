package com.excilys.cdb.ui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import com.excilys.cdb.daos.DaoCompany;
import com.excilys.cdb.daos.DaoComputer;
import com.excilys.cdb.exception.CloseException;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

class CliTest {

	private void changeNextLine(Cli cli, String userInput) {
		ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
		System.setIn(bais);
		Scanner scanner = new Scanner(System.in);
		cli.setSc(scanner);
	}

	private void restoreScanner(Cli cli, InputStream inputStream) {
		System.setIn(inputStream);
		Scanner scanner3 = new Scanner(System.in);
		cli.setSc(scanner3);
	}

	@Test
	void testRunCliWithValidInput() throws OpenException, ExecuteQueryException, MapperException, CloseException {
		Cli cli = new Cli();
		InputStream oldInputStream = System.in;
		String userInput = "1\n7";
		changeNextLine(cli, userInput);

		DaoCompany daoCompany = mock(DaoCompany.class);
		LinkedList<Company> allCompanies = new LinkedList<Company>();
		allCompanies.add(new Company(1, "company_one"));
		allCompanies.add(new Company(2, "company_two"));
		allCompanies.add(new Company(3, "company_three"));
		when(daoCompany.getAllCompanies()).thenReturn(allCompanies);
		DaoCompany.setDaoCompany(daoCompany);

		cli.runCli();
		// Verifier la sortit sur console
		DaoCompany.setDaoCompany(null);
		restoreScanner(cli, oldInputStream);
	}

	@Test
	void test2RunCliWithValidInput() throws OpenException, ExecuteQueryException, MapperException, CloseException {
		Cli cli = new Cli();
		InputStream oldInputStream = System.in;
		String userInput = "2\nq\n7";
		changeNextLine(cli, userInput);

		DaoComputer daoComputer = mock(DaoComputer.class);
		LinkedList<Computer> computers = new LinkedList<Computer>();
		computers.add(new Computer(1, "computer_one", null, null, new Company(0, null)));
		computers.add(new Computer(2, "computer_two", null, null, new Company(0, null)));
		computers.add(new Computer(3, "computer_three", null, null, new Company(0, null)));
		when(daoComputer.getPartOfComputers(10, 0)).thenReturn(computers);
		when(daoComputer.getNumberOfComputer()).thenReturn(3);
		DaoComputer.setDaoComputer(daoComputer);

		cli.runCli();
		// Verifier la sortit sur console
		DaoComputer.setDaoComputer(null);
		restoreScanner(cli, oldInputStream);
	}

}
