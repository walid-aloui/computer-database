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

import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.model.Company;

class DaoCompanyTest {

	@BeforeEach
	void setUp() throws Exception {
		DatabaseConnection db = DatabaseConnection.getInstance();
		try (Connection con = db.openConnection();) {
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
			LinkedList<Company> allcompanies = DaoCompany.getInstance().getAllCompanies();
			assertEquals(numCompanies, allcompanies.size());
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testGetCompanyByIdShouldReturnCompany() {
		try {
			int id = 1;
			Optional<Company> allcompanies = DaoCompany.getInstance().getCompanyById(id);
			if (allcompanies.isPresent()) {
				assertEquals(id, allcompanies.get().getId());
			} else {
				fail("Should not be empty");
			}
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not return an exception");
		}
	}

	@Test
	void testGetCompanyByIdShouldNotReturnCompany() {
		try {
			int falseId = 999;
			Optional<Company> allcompanies = DaoCompany.getInstance().getCompanyById(falseId);
			if (allcompanies.isPresent()) {
				fail("Should be empty");
			}
		} catch (OpenException | ExecuteQueryException | MapperException e) {
			fail("Should not return an exception");
		}
	}
	
	@Test
	void testDeleteCompanyByIdShouldDeleteCompany() {
		try {
			int companyId = 1;
			int numDelete = DaoCompany.getInstance().deleteCompanyById(companyId);
			assertEquals(companyId, numDelete);
		} catch (OpenException | ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}
	
	@Test
	void testDeleteCompanyByIdShouldNotDeleteCompany() {
		try {
			int falseId = 999;
			int numDelete = DaoCompany.getInstance().deleteCompanyById(falseId);
			assertEquals(0, numDelete);
		} catch (OpenException | ExecuteQueryException e) {
			fail("Should not throw an exception");
		}
	}

}
