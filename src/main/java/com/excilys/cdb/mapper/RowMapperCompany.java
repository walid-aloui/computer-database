package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Company.CompanyBuilder;

@Component
public class RowMapperCompany implements RowMapper<Company> {

	private static final String COLUMN_ID = "id";
	private static final String COLUMN_NAME = "name";

	@Override
	public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new CompanyBuilder()
				.withId(rs.getInt(COLUMN_ID))
				.withName(rs.getString(COLUMN_NAME))
				.build();
	}

}
