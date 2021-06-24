package com.excilys.cdb.binding.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.excilys.cdb.binding.ConfigTest;
import com.excilys.cdb.binding.dtos.CompanyDto;
import com.excilys.cdb.core.model.Company;
import com.excilys.cdb.core.model.Company.CompanyBuilder;

@SpringJUnitConfig(ConfigTest.class)
class MapperCompanyTest {

	private MapperCompany mapperCompany;

	@Autowired
	public MapperCompanyTest(MapperCompany mapperCompany) {
		this.mapperCompany = mapperCompany;
	}

	@Test
	void testFromCompanyListToCompanyDtoListShouldReturnListOfCompanyDto() {
		List<Company> companies = new ArrayList<>(2);
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

		List<CompanyDto> companiesDto = mapperCompany.fromCompanyListToCompanyDtoList(companies);
		assertEquals(2, companiesDto.size());
		CompanyDto companyDto = companiesDto.get(0);
		assertEquals(wr7Company.getId(), companyDto.getId());
		assertEquals(wr7Company.getName(), companyDto.getName());
	}

	@Test
	void testFromCompanyListToCompanyDtoListShouldReturnAnEmptyListOfCompanyDto() {
		List<Company> companies = new ArrayList<>();
		List<CompanyDto> companiesDto = mapperCompany.fromCompanyListToCompanyDtoList(companies);
		assertEquals(0, companiesDto.size());
	}

}
