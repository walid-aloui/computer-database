package com.excilys.cdb.binding.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.excilys.cdb.binding.dtos.CompanyDto;
import com.excilys.cdb.binding.dtos.CompanyDto.CompanyDtoBuilder;
import com.excilys.cdb.core.model.Company;

@Component
public class MapperCompany {
	
	public MapperCompany() {
		super();
	}

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