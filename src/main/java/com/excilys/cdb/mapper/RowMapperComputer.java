package com.excilys.cdb.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Company.CompanyBuilder;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Computer.ComputerBuilder;

@Component
public class RowMapperComputer implements RowMapper<Computer> {
	
	private static final String COLUMN_COMPUTER_ID = "computer.id";
	private static final String COLUMN_COMPUTER_NAME = "computer.name";
	private static final String COLUMN_COMPUTER_INTRODUCED = "introduced";
	private static final String COLUMN_COMPUTER_DISCONTINUED = "discontinued";
	
	private static final String COLUMN_COMPANY_ID = "company.id";
	private static final String COLUMN_COMPANY_NAME = "company.name";

	@Override
	public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
		LocalDate introduced = null;
		LocalDate discontinued = null;
		Company company = null;
		
		Date intro = rs.getDate(COLUMN_COMPUTER_INTRODUCED);
		if(intro != null) {
			introduced = intro.toLocalDate();
		}
		
		Date discon = rs.getDate(COLUMN_COMPUTER_DISCONTINUED);
		if(discon != null) {
			discontinued = discon.toLocalDate();
		}
		
		int id = rs.getInt(COLUMN_COMPANY_ID);
		if(id != 0) {
			company = new CompanyBuilder()
					.withId(id)
					.withName(rs.getString(COLUMN_COMPANY_NAME))
					.build();
		}
		
		return new ComputerBuilder()
				.withId(rs.getInt(COLUMN_COMPUTER_ID))
				.withName(rs.getString(COLUMN_COMPUTER_NAME))
				.withIntroduced(introduced)
				.withDiscontinued(discontinued)
				.withCompany(company)
				.build();
	}

}
