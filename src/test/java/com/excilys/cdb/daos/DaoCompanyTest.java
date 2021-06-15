package com.excilys.cdb.daos;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
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

@SpringJUnitConfig(ConfigTest.class)
class DaoCompanyTest {

	private DatabaseConnection dbConnection;
	private DaoCompany daoCompany;

	@Autowired
	public DaoCompanyTest(DatabaseConnection db, DaoCompany daoCompany) {
		this.dbConnection = db;
		this.daoCompany = daoCompany;
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

	@Test
	void testGetAllCompaniesShouldReturnListOfCompanies() {
		try {
			int numCompanies = 42;
			List<Company> allcompanies = daoCompany.selectAllCompanies();
			assertEquals(numCompanies, allcompanies.size());
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testGetCompanyByIdShouldReturnCompany() {
		try {
			int id = 1;
			Optional<Company> allcompanies = daoCompany.selectCompanyById(id);
			if (allcompanies.isPresent()) {
				assertEquals(id, allcompanies.get().getId());
			} else {
				fail("Should not be empty");
			}
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testGetCompanyByIdShouldNotReturnCompany() {
		try {
			int falseId = 999;
			Optional<Company> allcompanies = daoCompany.selectCompanyById(falseId);
			if (allcompanies.isPresent()) {
				fail("Should be empty");
			}
		} catch (ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testDeleteCompanyByIdShouldDeleteCompany() {
		int companyId = 1;
		int numDelete = daoCompany.deleteCompanyById(companyId);
		assertEquals(companyId, numDelete);
	}

	@Test
	void testDeleteCompanyByIdShouldNotDeleteCompany() {
		int falseId = 999;
		int numDelete = daoCompany.deleteCompanyById(falseId);
		assertEquals(0, numDelete);
	}

}
