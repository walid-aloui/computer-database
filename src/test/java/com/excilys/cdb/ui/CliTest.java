package com.excilys.cdb.ui;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.excilys.cdb.ConfigTest;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Company.CompanyBuilder;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Computer.ComputerBuilder;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

@SpringJUnitConfig(ConfigTest.class)
class CliTest {
	
	@Mock
	CompanyService mockCompanyService;
	@Mock
	ComputerService mockComputerService;

	private Cli cli;
	@InjectMocks
	private ControllerCli controllerCli;
	
	@Autowired
	public CliTest(Cli cli) {
		this.cli = cli;
		this.controllerCli = cli.getControllerCli();
	}
	
	@BeforeEach
	void setUp() throws Exception {
		 MockitoAnnotations.initMocks(this);
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
			listComputers.add(new ComputerBuilder()
					.withId(k)
					.withName("computer_" + k)
					.build());
		}
		return listComputers;
	}

	@Test
	void testRunCliShouldQuitApplication() {
		String choiceMenu = "8";
		changeNextLineCli(choiceMenu);
		
		cli.runCli();

		// TODO : Verifier la sortit sur console
	}

	@Test
	void testRunCliShouldDisplayListOfCompanies() {
		String choiceMenu = "1\n8";
		changeNextLineCli(choiceMenu);

		LinkedList<Company> allCompanies = new LinkedList<Company>();
		allCompanies.add(new CompanyBuilder()
				.withId(1)
				.withName("Company_one")
				.build());
		allCompanies.add(new CompanyBuilder()
				.withId(2)
				.withName("Company_two")
				.build());
		allCompanies.add(new CompanyBuilder()
				.withId(3)
				.withName("Company_three")
				.build());
		when(mockCompanyService.selectAllCompanies()).thenReturn(allCompanies);
		cli.runCli();
		verify(mockCompanyService, times(1)).selectAllCompanies();

		// TODO : Verifier la sortit sur console
	}

	@Test
	void testRunCliShouldDisplayListOfComputers() {
		String choiceMenu = "2\n8";
		changeNextLineCli(choiceMenu);
		String userInput = "q";
		changeNextLineController(userInput);

		final long numElements = Page.getDefaultNumElement();
		final int offset = 0;
		LinkedList<Computer> listComputers = createListOfTenComputers();
		when(mockComputerService.selectPartOfComputers(numElements, offset)).thenReturn(listComputers);
		when(mockComputerService.selectNumberOfComputer()).thenReturn(numElements);
		cli.runCli();
		verify(mockComputerService, times(1)).selectPartOfComputers(numElements, offset);
		verify(mockComputerService, times(1)).selectNumberOfComputer();

		// TODO : Verifier la sortit sur console
	}

	@Test
	void testRunCliShouldDisplayDetailsOfComputer() {
		String choiceMenu = "3\n8";
		changeNextLineCli(choiceMenu);
		int computerId = 1;
		changeNextLineController(String.valueOf(computerId));
		
		Computer computer = new ComputerBuilder()
				.withId(computerId)
				.withName("Computer_WR7")
				.build();
		when(mockComputerService.selectComputerById(computerId)).thenReturn(Optional.of(computer));
		cli.runCli();
		verify(mockComputerService, times(1)).selectComputerById(computerId);

		// TODO : Verifier la sortit sur console
	}

	@Test
	void testRunCliShouldDisplayIdDoesntExist() {
		String choiceMenu = "3\n8";
		changeNextLineCli(choiceMenu);
		int computerId = 999;
		changeNextLineController(String.valueOf(computerId));

		when(mockComputerService.selectComputerById(computerId)).thenReturn(Optional.empty());
		cli.runCli();
		verify(mockComputerService, times(1)).selectComputerById(computerId);

		// TODO : Verifier la sortit sur console
	}

	@Test
	void testRunCliWithUpdateChoice() {
		String choiceMenu = "4\n8";
		changeNextLineCli(choiceMenu);

		int computerId = 1;
		String name = "test";
		String introduced = "";
		String discontinued = "";
		String company_id = "";
		String userInputs = computerId + "\n" + name + "\n" + introduced + "\n" + discontinued + "\n" + company_id
				+ "\n";
		changeNextLineController(userInputs);

		Computer computer = new ComputerBuilder()
				.withId(computerId)
				.withName(name)
				.build();
		when(mockComputerService.updateComputer(computer)).thenReturn(1L);
		cli.runCli();
		verify(mockComputerService, times(1)).updateComputer(computer);

		// TODO : Verifier la sortit sur console
	}

	@Test
	void testRunCliWithInsertChoice() {
		String choiceMenu = "5\n8";
		changeNextLineCli(choiceMenu);

		String name = "test";
		String introduced = "";
		String discontinued = "";
		String company_id = "";
		String userInputs = name + "\n" + introduced + "\n" + discontinued + "\n" + company_id + "\n";
		changeNextLineController(userInputs);

		Computer computer = new ComputerBuilder()
				.withName(name)
				.build();
		cli.runCli();
		verify(mockComputerService, times(1)).insertComputer(computer);

		// TODO : Verifier la sortit sur console
	}

	@Test
	void testRunCliWithDeleteComputerChoice() {
		String choiceMenu = "6\n8";
		changeNextLineCli(choiceMenu);

		int computerId = 1;
		changeNextLineController(String.valueOf(computerId));

		when(mockComputerService.deleteComputerById(computerId)).thenReturn(1L);
		cli.runCli();
		verify(mockComputerService, times(1)).deleteComputerById(computerId);

		// TODO : Verifier la sortit sur console
	}
	
	@Test
	void testRunCliWithDeleteCompanyChoice() {
		String choiceMenu = "7\n8";
		changeNextLineCli(choiceMenu);

		int companyId = 1;
		changeNextLineController(String.valueOf(companyId));

		when(mockCompanyService.deleteCompanyById(companyId)).thenReturn(1L);
		cli.runCli();
		verify(mockCompanyService, times(1)).deleteCompanyById(companyId);

		// TODO : Verifier la sortit sur console
	}
}
