package com.excilys.cdb.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.excilys.cdb.dtos.CompanyDto;
import com.excilys.cdb.dtos.CompanyDto.CompanyDtoBuilder;
import com.excilys.cdb.model.Company;

@Component
public class MapperCompany {

	public CompanyDto fromCompanyToCompanyDto(Company company) {
		return new CompanyDtoBuilder()
				.withId(company.getId())
				.withName(company.getName())
				.build();
	}

	public List<CompanyDto> fromCompanyListToCompanyDtoList(List<Company> companies) {
		return companies
				.stream()
				.map(company -> fromCompanyToCompanyDto(company))
				.collect(Collectors.toList());
	}

}