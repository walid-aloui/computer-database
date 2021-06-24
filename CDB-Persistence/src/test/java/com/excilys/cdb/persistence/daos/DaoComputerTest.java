package com.excilys.cdb.persistence.daos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.excilys.cdb.core.model.Company;
import com.excilys.cdb.core.model.Company.CompanyBuilder;
import com.excilys.cdb.core.model.Computer;
import com.excilys.cdb.core.model.Computer.ComputerBuilder;
import com.excilys.cdb.persistence.ConfigTest;
import com.zaxxer.hikari.HikariDataSource;

@SpringJUnitConfig(ConfigTest.class)
class DaoComputerTest {

	private DaoComputer daoComputer;
	private HikariDataSource ds;

	@Autowired
	public DaoComputerTest(DaoComputer daoComputer, HikariDataSource ds) {
		this.daoComputer = daoComputer;
		this.ds = ds;
	}
	
	@BeforeEach
	void setUp() throws Exception {
		try (Connection con = ds.getConnection()) {
			ScriptRunner sr = new ScriptRunner(con);
			Reader reader = new BufferedReader(
					new FileReader("src/test/resources/test-db.sql"));
			sr.runScript(reader);
		}
	}

	private Computer createWr7Computer() {
		int id = 1;
		String name = "Computer_WR7";
		Computer computer = new ComputerBuilder()
				.withName(name)
				.withId(id)
				.build();
		daoComputer.insertComputer(computer);
		return computer;
	}
	
	private Computer createAnotherWr7Computer() {
		String name = "Computer_WR7";
		Computer computer = new ComputerBuilder()
				.withName(name)
				.build();
		daoComputer.insertComputer(computer);
		return computer;
	}

	private Computer createTestComputer() {
		int id = 2;
		String name = "Computer_test";
		LocalDate introduced = LocalDate.of(2021, 05, 11);
		LocalDate discontinued = LocalDate.of(2022, 05, 11);
		int company_id = 1;
		String companyName = "Apple Inc.";

		Company company = new CompanyBuilder()
				.withId(company_id)
				.withName(companyName)
				.build();
		Computer computer = new ComputerBuilder()
				.withId(id)
				.withName(name)
				.withIntroduced(introduced)
				.withDiscontinued(discontinued)
				.withCompany(company)
				.build();

		daoComputer.insertComputer(computer);
		return computer;
	}

	private void createComputers() {
		for (int k = 0; k < 25; k++) {
			Computer computer = new ComputerBuilder().withName("test" + k).build();
			daoComputer.insertComputer(computer);
		}
	}

	@Test
	void testSelectAllComputersShouldReturnListOfComputers() {
		createWr7Computer();
		createTestComputer();
		List<Computer> allComputers = daoComputer.selectAllComputers();
		assertEquals(2, allComputers.size());
	}

	@Test
	void testSelectAllComputersShouldReturnAnEmptyListOfComputers() {
		List<Computer> allComputers = daoComputer.selectAllComputers();
		assertEquals(0, allComputers.size());
	}

	@Test
	void testSelectComputerByIdShouldReturnComputer() {
		createWr7Computer();
		Computer test = createTestComputer();
		Optional<Computer> computer = daoComputer.selectComputerById(test.getId());
		if (computer.isPresent()) {
			Computer c = computer.get();
			assertEquals(test, c);
		} else {
			fail("Should not be empty");
		}
	}

	@Test
	void testSelectComputerByIdShouldNotReturnComputer() {
		createWr7Computer();
		createTestComputer();
		int falseId = 5;
		Optional<Computer> computer = daoComputer.selectComputerById(falseId);
		if (computer.isPresent()) {
			fail("Should be empty");
		}
	}

	@Test
	void testSelectPartOfComputersByNameShouldReturnListOfComputers() {
		int limit = 10;
		int offset = 0;
		Computer wr7 = createWr7Computer();
		createTestComputer();
		createAnotherWr7Computer();
		List<Computer> computers = daoComputer.selectPartOfComputersBySearch(wr7.getName(), limit, offset);
		assertEquals(2, computers.size());
	}

	@Test
	void testSelectPartOfComputersByNameShouldReturnAnEmptyListOfComputers() {
		String name = "false_name";
		int limit = 10;
		int offset = 0;
		List<Computer> computers = daoComputer.selectPartOfComputersBySearch(name, limit, offset);
		assertEquals(0, computers.size());

	}

	@Test
	void testDeleteComputerByIdShouldDeleteComputer() {
		createWr7Computer();
		createTestComputer();
		int id = 1;
		long numDelete = daoComputer.deleteComputerById(id);
		assertEquals(id, numDelete);
		List<Computer> allComputers = daoComputer.selectAllComputers();
		assertEquals(1, allComputers.size());
	}

	@Test
	void testDeleteComputerByIdShouldNotDeleteComputer() {
		createWr7Computer();
		createTestComputer();
		int falseId = 5;
		long numDelete = daoComputer.deleteComputerById(falseId);
		assertEquals(0, numDelete);
		List<Computer> allComputers = daoComputer.selectAllComputers();
		assertEquals(2, allComputers.size());
	}

	@Test
	void testDeleteComputersByCompanyIdShouldDeleteComputer() {
		createWr7Computer();
		Computer test = createTestComputer();
		int companyId = test.getCompany().getId();
		long numDelete = daoComputer.deleteComputersByCompanyId(companyId);
		assertEquals(companyId, numDelete);
		List<Computer> allComputers = daoComputer.selectAllComputers();
		assertEquals(1, allComputers.size());
	}

	@Test
	void testDeleteComputersByCompanyIdShouldNotDeleteComputer() {
		createWr7Computer();
		createTestComputer();
		int falseId = 999;
		long numDelete = daoComputer.deleteComputersByCompanyId(falseId);
		assertEquals(0, numDelete);
		List<Computer> allComputers = daoComputer.selectAllComputers();
		assertEquals(2, allComputers.size());
	}

	@Test
	void testInsertComputerShouldNotInsertComputer() {
		String name = "Computer_WR7";
		int company_id = 999;
		Company company = new CompanyBuilder().withId(company_id).build();
		Computer computer = new ComputerBuilder().withName(name).withCompany(company).build();
		boolean numInsert = daoComputer.insertComputer(computer);
		assertEquals(false, numInsert);
		List<Computer> allComputers = daoComputer.selectAllComputers();
		assertEquals(0, allComputers.size());
	}

	@Test
	void testUpdateComputerShouldUpdateComputer() {
		Computer wr7 = createWr7Computer();
		Computer test = createTestComputer();
		Computer computerUpdate = new ComputerBuilder()
				.withId(wr7.getId())
				.withName(test.getName())
				.withIntroduced(test.getIntroduced())
				.withDiscontinued(test.getDiscontinued())
				.withCompany(test.getCompany())
				.build();
		long numUpdate = daoComputer.updateComputer(computerUpdate);
		assertEquals(1, numUpdate);
		Optional<Computer> computer = daoComputer.selectComputerById(wr7.getId());
		if (computer.isPresent()) {
			assertEquals(computerUpdate, computer.get());
		} else {
			fail("Should not be empty");
		}
	}

	@Test
	void testupdateComputerShouldNotUpdateComputer() {
		createWr7Computer();
		Computer test = createTestComputer();
		int falseId = 5;
		String name = "test";
		
		Computer falseComputer = new ComputerBuilder()
				.withId(falseId)
				.withName(name)
				.build();
		
		long numUpdate = daoComputer.updateComputer(falseComputer);
		assertEquals(0, numUpdate);
		
		int falseCompanyId = 999;
		Company falseCompany = new CompanyBuilder()
				.withId(falseCompanyId)
				.build();
		Computer computer = new ComputerBuilder()
				.withId(test.getId())
				.withName(test.getName())
				.withIntroduced(test.getIntroduced())
				.withDiscontinued(test.getDiscontinued())
				.withCompany(falseCompany)
				.build();
		long numUpdate2 = daoComputer.updateComputer(computer);
		assertEquals(0, numUpdate2);
	}

	@Test
	void testSelectPartOfComputersShouldReturnListOfTenComputers() {
		createComputers();
		List<Computer> allComputers = daoComputer.selectPartOfComputers(10, 0);
		assertEquals(10, allComputers.size());
	}

	@Test
	void testSelectPartOfComputersShouldReturnListOfTenComputersWithOffset10() {
		createComputers();
		List<Computer> allComputers = daoComputer.selectPartOfComputers(10, 10);
		assertEquals(10, allComputers.size());
	}

	@Test
	void testSelectPartOfComputersShouldReturnListOfFiveComputersWithOffset10() {
		createComputers();
		List<Computer> allComputers = daoComputer.selectPartOfComputers(10, 20);
		assertEquals(5, allComputers.size());
	}

	@Test
	void testSelectPartOfComputersShouldReturnListOfTwoComputers() {
		createWr7Computer();
		createTestComputer();
		List<Computer> allComputers = daoComputer.selectPartOfComputers(10, 0);
		assertEquals(2, allComputers.size());
	}

	@Test
	void testSelectPartOfComputersShouldReturnAnEmptyListOfComputers() {
		List<Computer> allComputers = daoComputer.selectPartOfComputers(10, 0);
		assertEquals(0, allComputers.size());
	}

	@Test
	void testSelectNumberOfComputerShouldReturn2() {
		createWr7Computer();
		createTestComputer();
		long numComputer = daoComputer.selectNumberOfComputer();
		assertEquals(2, numComputer);
	}

	@Test
	void testSelectNumberOfComputerShouldReturn0() {
		long numComputer = daoComputer.selectNumberOfComputer();
		assertEquals(0, numComputer);
	}

}
