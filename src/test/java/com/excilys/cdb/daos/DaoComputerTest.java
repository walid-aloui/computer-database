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
import com.excilys.cdb.model.Computer;
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

	private int createWr7Computer() throws OpenException {
		String name = "Computer_WR7";
		Optional<LocalDate> introduced = SecureInputs.toLocalDate(null);
		Optional<LocalDate> discontinued = SecureInputs.toLocalDate(null);
		Optional<String> company_id = Optional.empty();
		return DaoComputer.getInstance().insertComputer(name, introduced, discontinued, company_id);
	}

	private int createTestComputer() throws OpenException {
		String name = "Computer_test";
		Optional<LocalDate> introduced = SecureInputs.toLocalDate("2021-05-11");
		Optional<LocalDate> discontinued = SecureInputs.toLocalDate("2022-05-11");
		Optional<String> company_id = Optional.of("1");
		return DaoComputer.getInstance().insertComputer(name, introduced, discontinued, company_id);
	}

	private void createComputers() {
		DaoComputer daoComputer = DaoComputer.getInstance();
		try {
			for (int k = 0; k < 25; k++) {
				daoComputer.insertComputer("test" + k, Optional.empty(), Optional.empty(), Optional.empty());
			}

		} catch (OpenException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testGetAllComputersShouldReturnListOfComputers() {
		try {
			int numInsert1 = createWr7Computer();
			assertEquals(1, numInsert1);

			int numInsert2 = createTestComputer();
			assertEquals(1, numInsert2);

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
			createTestComputer();

			Optional<Computer> computer = DaoComputer.getInstance().getComputerById(2);
			if (computer.isPresent()) {
				Computer c = computer.get();
				assertEquals(2, c.getId());
				assertEquals(SecureInputs.toLocalDate("2021-05-11").get(), c.getIntroduced());
				assertEquals(SecureInputs.toLocalDate("2022-05-11").get(), c.getDiscontinued());
				assertEquals(1, c.getCompany().getId());
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

			Optional<Computer> computer = DaoComputer.getInstance().getComputerById(5);
			if (computer.isPresent()) {
				fail("Should be empty");
			}
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testDeleteComputerByIdShouldDeleteComputer() {
		try {
			createWr7Computer();
			createTestComputer();

			int numDelete = DaoComputer.getInstance().deleteComputerById(1);
			assertEquals(1, numDelete);

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

			int numDelete = DaoComputer.getInstance().deleteComputerById(5);
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
			Optional<LocalDate> introduced = SecureInputs.toLocalDate(null);
			Optional<LocalDate> discontinued = SecureInputs.toLocalDate(null);
			Optional<String> company_id = Optional.of("999");
			int numInsert = DaoComputer.getInstance().insertComputer(name, introduced, discontinued, company_id);
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
			createWr7Computer();
			String newName = "test";
			Optional<LocalDate> newIntroduced = SecureInputs.toLocalDate("2021-05-11");
			Optional<LocalDate> newDiscontinued = SecureInputs.toLocalDate("2022-05-11");
			Optional<String> newCompanyId = Optional.of("1");
			int numUpdate = DaoComputer.getInstance().updateComputerById(1, newName, newIntroduced, newDiscontinued,
					newCompanyId);
			assertEquals(1, numUpdate);

			Optional<Computer> computer = DaoComputer.getInstance().getComputerById(1);
			if (computer.isPresent()) {
				assertEquals(newName, computer.get().getName());
				assertEquals(newIntroduced.get(), computer.get().getIntroduced());
				assertEquals(newDiscontinued.get(), computer.get().getDiscontinued());
				assertEquals(1, computer.get().getCompany().getId());
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
			createTestComputer();
			int numUpdate = DaoComputer.getInstance().updateComputerById(5, "test", Optional.empty(), Optional.empty(),
					Optional.empty());
			assertEquals(0, numUpdate);

			int numUpdate2 = DaoComputer.getInstance().updateComputerById(1, "test", Optional.empty(), Optional.empty(),
					Optional.of("999"));
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
