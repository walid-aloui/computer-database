package com.excilys.cdb.daos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Company.CompanyBuilder;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Computer.ComputerBuilder;
import com.excilys.cdb.utils.SecureInputs;

class DaoComputerTest {

	@BeforeEach
	void setUp() throws Exception {
		Database db = Database.getInstance();
		try (Connection con = db.openConnection();) {
			ScriptRunner sr = new ScriptRunner(con);
			Reader reader = new BufferedReader(
					new FileReader("/home/aloui/Bureau/computer-database/src/test/resources/test-db.sql"));
			sr.runScript(reader);
		}
	}

	private Computer createWr7Computer() throws OpenException {
		int id = 1;
		String name = "Computer_WR7";
		Computer computer = new ComputerBuilder()
				.withName(name)
				.withId(id)
				.build();
		DaoComputer.getInstance().insertComputer(computer);
		return computer;
	}

	private Computer createTestComputer() throws OpenException {
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

		DaoComputer.getInstance().insertComputer(computer);

		return computer;
	}

	private void createComputers() {
		DaoComputer daoComputer = DaoComputer.getInstance();
		try {
			for (int k = 0; k < 25; k++) {
				Computer computer = new ComputerBuilder()
						.withName("test" + k)
						.build();
				daoComputer.insertComputer(computer);
			}
		} catch (OpenException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testGetAllComputersShouldReturnListOfComputers() {
		try {
			createWr7Computer();
			createTestComputer();

			LinkedList<Computer> allComputers = DaoComputer.getInstance().getAllComputers();
			assertEquals(2, allComputers.size());
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testGetAllComputersShouldReturnAnEmptyListOfComputers() {
		try {
			LinkedList<Computer> allComputers = DaoComputer.getInstance().getAllComputers();
			assertEquals(0, allComputers.size());
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testGetComputerByIdShouldReturnComputer() {
		try {
			createWr7Computer();
			Computer test = createTestComputer();

			Optional<Computer> computer = DaoComputer.getInstance().getComputerById(test.getId());
			if (computer.isPresent()) {
				Computer c = computer.get();
				assertEquals(test, c);
			} else {
				fail("Should not be empty");
			}
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testGetComputerByIdShouldNotReturnComputer() {
		try {
			createWr7Computer();
			createTestComputer();
			int falseId = 5;

			Optional<Computer> computer = DaoComputer.getInstance().getComputerById(falseId);
			if (computer.isPresent()) {
				fail("Should be empty");
			}
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testGetComputerByNameShouldReturnListOfComputer() {
		try {
			Computer wr7 = createWr7Computer();
			createTestComputer();
			String computerName = "Computer_WR7";

			LinkedList<Computer> computers = DaoComputer.getInstance().getComputerByName(computerName);
			assertEquals(1, computers.size());
			assertEquals(wr7, computers.getFirst());
		} catch (OpenException | MapperException | ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testGetComputerByNameShouldReturnAnEmptyListOfComputer() {
		try {
			createWr7Computer();
			createTestComputer();
			String computerName = "false_name";

			LinkedList<Computer> computers = DaoComputer.getInstance().getComputerByName(computerName);
			assertEquals(0, computers.size());
		} catch (OpenException | MapperException | ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testGetPartOfComputersByNameShouldReturnListOfComputers() {
		int n = 10;
		int offset = 0;
		try {
			Computer wr7 = createWr7Computer();
			createWr7Computer();
			createTestComputer();
			LinkedList<Computer> computers = DaoComputer.getInstance().getPartOfComputersByName(wr7.getName(), n,
					offset);
			assertEquals(2, computers.size());
		} catch (OpenException | MapperException | ExecuteQueryException e) {
			fail("Should not throw and exception");
		}
	}

	@Test
	void testGetPartOfComputersByNameShouldReturnAnEmptyListOfComputers() {
		String name = "false_name";
		int n = 10;
		int offset = 0;
		try {
			LinkedList<Computer> computers = DaoComputer.getInstance().getPartOfComputersByName(name, n, offset);
			assertEquals(0, computers.size());
		} catch (OpenException | MapperException | ExecuteQueryException e) {
			fail("Should not throw and exception");
		}

	}

	@Test
	void testDeleteComputerByIdShouldDeleteComputer() {
		try {
			createWr7Computer();
			createTestComputer();
			int id = 1;

			int numDelete = DaoComputer.getInstance().deleteComputerById(id);
			assertEquals(id, numDelete);

			LinkedList<Computer> allComputers = DaoComputer.getInstance().getAllComputers();
			assertEquals(1, allComputers.size());
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testDeleteComputerByIdShouldNotDeleteComputer() {
		try {
			createWr7Computer();
			createTestComputer();
			int falseId = 5;

			int numDelete = DaoComputer.getInstance().deleteComputerById(falseId);
			assertEquals(0, numDelete);

			LinkedList<Computer> allComputers = DaoComputer.getInstance().getAllComputers();
			assertEquals(2, allComputers.size());
		} catch (OpenException | ExecuteQueryException | MapperException e) {
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

			int numInsert = DaoComputer.getInstance().insertComputer(computer);
			assertEquals(0, numInsert);

			LinkedList<Computer> allComputers = DaoComputer.getInstance().getAllComputers();
			assertEquals(0, allComputers.size());
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testUpdateComputerByIdShouldUpdateComputer() {
		try {
			Computer wr7 = createWr7Computer();
			Computer test = createTestComputer();

			int numUpdate = DaoComputer.getInstance().updateComputerById(wr7.getId(), test);
			assertEquals(1, numUpdate);

			Optional<Computer> computer = DaoComputer.getInstance().getComputerById(wr7.getId());
			if (computer.isPresent()) {
				Computer computerUpdate = new ComputerBuilder().withId(wr7.getId()).withName(test.getName())
						.withIntroduced(test.getIntroduced()).withDiscontinued(test.getDiscontinued())
						.withCompany(test.getCompany()).build();

				assertEquals(computerUpdate, computer.get());
			} else {
				fail("Should not be empty");
			}
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testUpdateComputerByIdShouldNotUpdateComputer() {
		try {
			createWr7Computer();
			Computer test = createTestComputer();
			int falseId = 5;

			int numUpdate = DaoComputer.getInstance().updateComputerById(falseId, test);
			assertEquals(0, numUpdate);

			int falseCompanyId = 999;
			Company falseCompany = new CompanyBuilder().withId(falseCompanyId).build();
			Computer computer = new ComputerBuilder().withId(test.getId()).withName(test.getName())
					.withIntroduced(test.getIntroduced()).withDiscontinued(test.getDiscontinued())
					.withCompany(falseCompany).build();

			int numUpdate2 = DaoComputer.getInstance().updateComputerById(1, computer);
			assertEquals(0, numUpdate2);
		} catch (OpenException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testGetPartOfComputersShouldReturnListOfTenComputers() {
		try {
			createComputers();

			LinkedList<Computer> allComputers = DaoComputer.getInstance().getPartOfComputers(10, 0);
			assertEquals(10, allComputers.size());
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testGetPartOfComputersShouldReturnListOfTenComputersWithOffset10() {
		try {
			createComputers();

			LinkedList<Computer> allComputers = DaoComputer.getInstance().getPartOfComputers(10, 10);
			assertEquals(10, allComputers.size());
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testGetPartOfComputersShouldReturnListOfFiveComputersWithOffset10() {
		try {
			createComputers();

			LinkedList<Computer> allComputers = DaoComputer.getInstance().getPartOfComputers(10, 20);
			assertEquals(5, allComputers.size());
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testGetPartOfComputersShouldReturnListOfTwoComputers() {
		try {
			createWr7Computer();
			createTestComputer();

			LinkedList<Computer> allComputers = DaoComputer.getInstance().getPartOfComputers(10, 0);
			assertEquals(2, allComputers.size());
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testGetPartOfComputersShouldReturnAnEmptyListOfComputers() {
		try {
			LinkedList<Computer> allComputers = DaoComputer.getInstance().getPartOfComputers(10, 0);
			assertEquals(0, allComputers.size());
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testGetNumberOfComputerShouldReturn2() {
		try {
			createWr7Computer();
			createTestComputer();

			int numComputer = DaoComputer.getInstance().getNumberOfComputer();
			assertEquals(2, numComputer);
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testGetNumberOfComputerShouldReturn0() {
		try {
			int numComputer = DaoComputer.getInstance().getNumberOfComputer();
			assertEquals(0, numComputer);
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

}
