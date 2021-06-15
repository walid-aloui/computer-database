package com.excilys.cdb.daos;

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

import com.excilys.cdb.ConfigTest;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Company.CompanyBuilder;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Computer.ComputerBuilder;
import com.excilys.cdb.utils.SecureInputs;

@SpringJUnitConfig(ConfigTest.class)
class DaoComputerTest {

	private DatabaseConnection dbConnection;
	private DaoComputer daoComputer;

	@Autowired
	public DaoComputerTest(DatabaseConnection dbConnection, DaoComputer daoComputer) {
		this.dbConnection = dbConnection;
		this.daoComputer = daoComputer;
	}

	@BeforeEach
	void setUp() throws Exception {
		try (Connection con = dbConnection.openConnection();) {
			ScriptRunner sr = new ScriptRunner(con);
			Reader reader = new BufferedReader(
					new FileReader("/home/aloui/Bureau/computer-database/src/test/resources/test-db.sql"));
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

	private Computer createTestComputer() {
		int id = 2;
		String name = "Computer_test";
		LocalDate introduced = SecureInputs.toLocalDate("2021-05-11").orElse(null);
		LocalDate discontinued = SecureInputs.toLocalDate("2022-05-11").orElse(null);
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
		try {
			createWr7Computer();
			createTestComputer();
			List<Computer> allComputers = daoComputer.selectAllComputers();
			assertEquals(2, allComputers.size());
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testSelectAllComputersShouldReturnAnEmptyListOfComputers() {
		try {
			List<Computer> allComputers = daoComputer.selectAllComputers();
			assertEquals(0, allComputers.size());
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testSelectComputerByIdShouldReturnComputer() {
		try {
			createWr7Computer();
			Computer test = createTestComputer();
			Optional<Computer> computer = daoComputer.selectComputerById(test.getId());
			if (computer.isPresent()) {
				Computer c = computer.get();
				assertEquals(test, c);
			} else {
				fail("Should not be empty");
			}
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testSelectComputerByIdShouldNotReturnComputer() {
		try {
			createWr7Computer();
			createTestComputer();
			int falseId = 5;
			Optional<Computer> computer = daoComputer.selectComputerById(falseId);
			if (computer.isPresent()) {
				fail("Should be empty");
			}
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testSelectComputerByNameShouldReturnListOfComputer() {
		try {
			Computer wr7 = createWr7Computer();
			createTestComputer();
			String computerName = "Computer_WR7";
			List<Computer> computers = daoComputer.selectComputersByName(computerName);
			assertEquals(1, computers.size());
			assertEquals(wr7, computers.get(0));
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testSelectComputerByNameShouldReturnAnEmptyListOfComputer() {
		try {
			createWr7Computer();
			createTestComputer();
			String computerName = "false_name";
			List<Computer> computers = daoComputer.selectComputersByName(computerName);
			assertEquals(0, computers.size());
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testSelectPartOfComputersByNameShouldReturnListOfComputers() {
		try {
			int limit = 10;
			int offset = 0;
			Computer wr7 = createWr7Computer();
			createWr7Computer();
			createTestComputer();
			List<Computer> computers = daoComputer.selectPartOfComputersByName(wr7.getName(), limit, offset);
			assertEquals(2, computers.size());
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testSelectPartOfComputersByNameShouldReturnAnEmptyListOfComputers() {
		try {
			String name = "false_name";
			int limit = 10;
			int offset = 0;
			List<Computer> computers = daoComputer.selectPartOfComputersByName(name, limit, offset);
			assertEquals(0, computers.size());
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}

	}

	@Test
	void testDeleteComputerByIdShouldDeleteComputer() {
		try {
			createWr7Computer();
			createTestComputer();
			int id = 1;
			int numDelete = daoComputer.deleteComputerById(id);
			assertEquals(id, numDelete);
			List<Computer> allComputers = daoComputer.selectAllComputers();
			assertEquals(1, allComputers.size());
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testDeleteComputerByIdShouldNotDeleteComputer() {
		try {
			createWr7Computer();
			createTestComputer();
			int falseId = 5;
			int numDelete = daoComputer.deleteComputerById(falseId);
			assertEquals(0, numDelete);
			List<Computer> allComputers = daoComputer.selectAllComputers();
			assertEquals(2, allComputers.size());
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testDeleteComputersByCompanyIdShouldDeleteComputer() {
		try {
			createWr7Computer();
			Computer test = createTestComputer();
			int companyId = test.getCompany().getId();
			int numDelete = daoComputer.deleteComputersByCompanyId(companyId);
			assertEquals(companyId, numDelete);
			List<Computer> allComputers = daoComputer.selectAllComputers();
			assertEquals(1, allComputers.size());
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testDeleteComputersByCompanyIdShouldNotDeleteComputer() {
		try {
			createWr7Computer();
			createTestComputer();
			int falseId = 999;
			int numDelete = daoComputer.deleteComputersByCompanyId(falseId);
			assertEquals(0, numDelete);
			List<Computer> allComputers = daoComputer.selectAllComputers();
			assertEquals(2, allComputers.size());
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testInsertComputerShouldNotInsertComputer() {
		try {
			String name = "Computer_WR7";
			int company_id = 999;
			Company company = new CompanyBuilder().withId(company_id).build();
			Computer computer = new ComputerBuilder().withName(name).withCompany(company).build();
			int numInsert = daoComputer.insertComputer(computer);
			assertEquals(0, numInsert);
			List<Computer> allComputers = daoComputer.selectAllComputers();
			assertEquals(0, allComputers.size());
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testUpdateComputerShouldUpdateComputer() {
		try {
			Computer wr7 = createWr7Computer();
			Computer test = createTestComputer();
			Computer computerUpdate = new ComputerBuilder()
					.withId(wr7.getId())
					.withName(test.getName())
					.withIntroduced(test.getIntroduced())
					.withDiscontinued(test.getDiscontinued())
					.withCompany(test.getCompany())
					.build();
			int numUpdate = daoComputer.updateComputer(computerUpdate);
			assertEquals(1, numUpdate);
			Optional<Computer> computer = daoComputer.selectComputerById(wr7.getId());
			if (computer.isPresent()) {
				assertEquals(computerUpdate, computer.get());
			} else {
				fail("Should not be empty");
			}
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
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
		
		int numUpdate = daoComputer.updateComputer(falseComputer);
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
		int numUpdate2 = daoComputer.updateComputer(computer);
		assertEquals(0, numUpdate2);
	}

	@Test
	void testSelectPartOfComputersShouldReturnListOfTenComputers() {
		try {
			createComputers();
			List<Computer> allComputers = daoComputer.selectPartOfComputers(10, 0);
			assertEquals(10, allComputers.size());
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testSelectPartOfComputersShouldReturnListOfTenComputersWithOffset10() {
		try {
			createComputers();
			List<Computer> allComputers = daoComputer.selectPartOfComputers(10, 10);
			assertEquals(10, allComputers.size());
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testSelectPartOfComputersShouldReturnListOfFiveComputersWithOffset10() {
		try {
			createComputers();
			List<Computer> allComputers = daoComputer.selectPartOfComputers(10, 20);
			assertEquals(5, allComputers.size());
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testSelectPartOfComputersShouldReturnListOfTwoComputers() {
		try {
			createWr7Computer();
			createTestComputer();
			List<Computer> allComputers = daoComputer.selectPartOfComputers(10, 0);
			assertEquals(2, allComputers.size());
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testSelectPartOfComputersShouldReturnAnEmptyListOfComputers() {
		try {
			List<Computer> allComputers = daoComputer.selectPartOfComputers(10, 0);
			assertEquals(0, allComputers.size());
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testSelectNumberOfComputerShouldReturn2() {
		try {
			createWr7Computer();
			createTestComputer();
			int numComputer = daoComputer.selectNumberOfComputer();
			assertEquals(2, numComputer);
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testSelectNumberOfComputerShouldReturn0() {
		try {
			int numComputer = daoComputer.selectNumberOfComputer();
			assertEquals(0, numComputer);
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

}
