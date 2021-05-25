package com.excilys.cdb.daos;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.excilys.cdb.exception.CloseException;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.model.Company;

class DaoCompanyTest {

	@Test
	void testGetAllCompaniesShouldReturnAnEmptyListOfCompanies() {
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

}
