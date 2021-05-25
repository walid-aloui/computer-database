package com.excilys.cdb.daos;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.Optional;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.excilys.cdb.exception.CloseException;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.model.Company;

class DaoCompanyTest {

	@BeforeEach
	void setUp() throws Exception {
		Database db = Database.getInstance();
		Connection con = db.openConnection();
		ScriptRunner sr = new ScriptRunner(con);
		Reader reader = new BufferedReader(
				new FileReader("/home/aloui/Bureau/computer-database/src/test/resources/test-db.sql"));
		sr.runScript(reader);
		db.closeConnection(con);
	}

	@Test
	void testGetAllCompaniesShouldReturnListOfCompanies() {
		LinkedList<Company> allcompanies;
		try {
			allcompanies = DaoCompany.getInstance().getAllCompanies();
			assertEquals(42, allcompanies.size());
		} catch (OpenException | ExecuteQueryException | MapperException | CloseException e) {
			fail("Should not return an exception");
		}
	}

	@Test
	void testGetCompanyByIdShouldReturnCompany() {
		Optional<Company> allcompanies;
		try {
			allcompanies = DaoCompany.getInstance().getCompanyById(1);
			if (allcompanies.isPresent()) {
				assertEquals(1, allcompanies.get().getId());
			} else {
				fail("Should not be empty");
			}
		} catch (OpenException | ExecuteQueryException | MapperException | CloseException e) {
			fail("Should not return an exception");
		}
	}

	@Test
	void testGetCompanyByIdShouldNotReturnCompany() {
		Optional<Company> allcompanies;
		try {
			allcompanies = DaoCompany.getInstance().getCompanyById(999);
			if (allcompanies.isPresent()) {
				fail("Should be empty");
			}
		} catch (OpenException | ExecuteQueryException | MapperException | CloseException e) {
			fail("Should not return an exception");
		}
	}

}