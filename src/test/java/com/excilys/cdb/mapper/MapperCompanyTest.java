package com.excilys.cdb.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.model.Company;

class MapperCompanyTest {

	@Test
	void testMapToCompanyShouldReturnListOfCompanies() {
		ResultSet mockResultSet = mock(ResultSet.class);
		try {
			when(mockResultSet.next()).thenReturn(true).thenReturn(false);
			when(mockResultSet.getInt("id")).thenReturn(1);
			when(mockResultSet.getString("name")).thenReturn("WR7");
			LinkedList<Company> company = MapperCompany.mapToCompany(mockResultSet);
			assertEquals(1, company.size());
			assertEquals(1, company.getFirst().getId());
			assertEquals("WR7", company.getFirst().getName());
		} catch (SQLException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testMapToCompanyShouldReturnAnEmptyListOfCompanies() {
		ResultSet mockResultSet = mock(ResultSet.class);
		try {
			when(mockResultSet.next()).thenReturn(false);
			LinkedList<Company> company = MapperCompany.mapToCompany(mockResultSet);
			assertEquals(0, company.size());
		} catch (SQLException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testMapToCompanyShouldThrowAnException() {
		assertThrows(MapperException.class, () -> {
			MapperCompany.mapToCompany(null);
		});
	}

}
