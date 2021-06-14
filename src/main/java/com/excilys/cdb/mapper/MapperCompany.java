package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dtos.CompanyDto;
import com.excilys.cdb.dtos.CompanyDto.CompanyDtoBuilder;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Company.CompanyBuilder;

@Component
public class MapperCompany {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapperCompany.class);

	public LinkedList<Company> fromResultSetToCompany(ResultSet resultSet) throws MapperException {
		LinkedList<Company> company = new LinkedList<Company>();
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				company.add(new CompanyBuilder()
						.withId(id)
						.withName(name)
						.build());
			}
		} catch (SQLException e) {
			LOGGER.error("Echec fromResultSetToCompany", e);
			throw new MapperException();
		} catch (NullPointerException e) {
			LOGGER.error("Echec fromResultSetToCompany : NullPointerException");
			throw new MapperException();
		}
		return company;
	}

	private CompanyDto fromCompanyToCompanyDto(Company company) {
		return new CompanyDtoBuilder()
				.withId(company.getId())
				.withName(company.getName())
				.build();
	}

	public LinkedList<CompanyDto> fromCompanyListToCompanyDtoList(LinkedList<Company> companies) {
		return companies
				.stream()
				.map(company -> fromCompanyToCompanyDto(company))
				.collect(Collectors.toCollection(LinkedList::new));
	}

}