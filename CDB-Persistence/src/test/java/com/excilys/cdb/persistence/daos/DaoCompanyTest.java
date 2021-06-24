package com.excilys.cdb.persistence.daos;

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

import com.excilys.cdb.core.model.Company;
import com.excilys.cdb.persistence.ConfigTest;
import com.zaxxer.hikari.HikariDataSource;

@SpringJUnitConfig(ConfigTest.class)
class DaoCompanyTest {

	private DaoCompany daoCompany;
	private HikariDataSource ds;

	@Autowired
	public DaoCompanyTest(DaoCompany daoCompany, HikariDataSource ds) {
		this.daoCompany = daoCompany;
		this.ds = ds;
	}
	
	@BeforeEach
	void setUp() throws Exception {
		try (Connection con = ds.getConnection();) {
			ScriptRunner sr = new ScriptRunner(con);
			Reader reader = new BufferedReader(
					new FileReader("src/test/resources/test-db.sql"));
			sr.runScript(reader);
		}
	}
	
	@Test
	void testSelectAllCompaniesShouldReturnListOfCompanies() {
		int numCompanies = 42;
		List<Company> allcompanies = daoCompany.selectAllCompanies();
		assertEquals(numCompanies, allcompanies.size());
	}

	@Test
	void testSelectCompanyByIdShouldReturnCompany() {
		int id = 1;
		Optional<Company> allcompanies = daoCompany.selectCompanyById(id);
		if (allcompanies.isPresent()) {
			assertEquals(id, allcompanies.get().getId());
		} else {
			fail("Should not be empty");
		}
	}

	@Test
	void testSelectCompanyByIdShouldNotReturnCompany() {
		int falseId = 999;
		Optional<Company> allcompanies = daoCompany.selectCompanyById(falseId);
		if (allcompanies.isPresent()) {
			fail("Should be empty");
		}
	}

	@Test
	void testDeleteCompanyByIdShouldDeleteCompany() {
		int companyId = 1;
		long numDelete = daoCompany.deleteCompanyById(companyId);
		assertEquals(1, numDelete);
	}

	@Test
	void testDeleteCompanyByIdShouldNotDeleteCompany() {
		int falseId = 999;
		long numDelete = daoCompany.deleteCompanyById(falseId);
		assertEquals(0, numDelete);
	}

}
