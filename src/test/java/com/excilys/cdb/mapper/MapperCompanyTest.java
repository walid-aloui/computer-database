package com.excilys.cdb.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import com.excilys.cdb.dtos.CompanyDto;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Company.CompanyBuilder;

class MapperCompanyTest {

	@Test
	void testMapToCompanyShouldReturnListOfCompanies() {
		ResultSet mockResultSet = mock(ResultSet.class);
		try {
			when(mockResultSet.next()).thenReturn(true).thenReturn(false);
			when(mockResultSet.getInt("id")).thenReturn(1);
			when(mockResultSet.getString("name")).thenReturn("WR7");
			LinkedList<Company> company = MapperCompany.getInstance().fromResultSetToCompany(mockResultSet);
			assertEquals(1, company.size());
			assertEquals(1, company.getFirst().getId());
			assertEquals("WR7", company.getFirst().getName());
			verify(mockResultSet, times(2)).next();
			verify(mockResultSet, times(1)).getInt("id");
			verify(mockResultSet, times(1)).getString("name");
		} catch (SQLException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testMapToCompanyShouldReturnAnEmptyListOfCompanies() {
		ResultSet mockResultSet = mock(ResultSet.class);
		try {
			when(mockResultSet.next()).thenReturn(false);
			LinkedList<Company> company = MapperCompany.getInstance().fromResultSetToCompany(mockResultSet);
			assertEquals(0, company.size());
			verify(mockResultSet, times(1)).next();
		} catch (SQLException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testMapToCompanyShouldThrowAnException() {
		assertThrows(MapperException.class, () -> {
			MapperCompany.getInstance().fromResultSetToCompany(null);
		});
	}

	@Test
	void testFromCompanyListToCompanyDtoListShouldReturnListOfCompanyDto() {
		LinkedList<Company> companies = new LinkedList<>();
		Company wr7Company = new CompanyBuilder()
				.withId(1)
				.withName("WR7-Company")
				.build();
		Company testCompany = new CompanyBuilder()
				.withId(2)
				.withName("test")
				.build();
		companies.add(wr7Company);
		companies.add(testCompany);
		LinkedList<CompanyDto> companiesDto = MapperCompany.getInstance().fromCompanyListToCompanyDtoList(companies);
		assertEquals(2, companiesDto.size());
		CompanyDto companyDto = companiesDto.getFirst();
		assertEquals(wr7Company.getId(), companyDto.getId());
		assertEquals(wr7Company.getName(), companyDto.getName());
	}

	@Test
	void testFromCompanyListToCompanyDtoListShouldReturnAnEmptyListOfCompanyDto() {
		LinkedList<Company> companies = new LinkedList<>();
		LinkedList<CompanyDto> companiesDto = MapperCompany.getInstance().fromCompanyListToCompanyDtoList(companies);
		assertEquals(0, companiesDto.size());
	}

}
