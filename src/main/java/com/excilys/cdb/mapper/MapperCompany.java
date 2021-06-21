package com.excilys.cdb.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.excilys.cdb.dtos.CompanyDto;
import com.excilys.cdb.dtos.CompanyDto.CompanyDtoBuilder;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Company.CompanyBuilder;
import com.excilys.cdb.model.QCompany;
import com.querydsl.core.Tuple;

@Component
public class MapperCompany {
	
	private static final QCompany qCompany = QCompany.company;
	
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
	
	public Company fromTupleToCompany(Tuple t) {
		return new CompanyBuilder()
				.withId(t.get(qCompany.id))
				.withName(t.get(qCompany.name))
				.build();
	}
	
	public List<Company> fromTupleListToCompanyList(List<Tuple> tupleList) {
		return tupleList
				.stream()
				.map(tuple -> fromTupleToCompany(tuple))
				.collect(Collectors.toList());
	}

}