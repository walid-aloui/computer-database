package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Company.CompanyBuilder;

public class MapperCompany {

	private static MapperCompany mapperCompany;
	private static final Logger LOGGER = LoggerFactory.getLogger(MapperCompany.class);

	public static MapperCompany getInstance() {
		if (mapperCompany == null) {
			mapperCompany = new MapperCompany();
		}
		return mapperCompany;
	}

	private MapperCompany() {
		super();
	}

	public LinkedList<Company> fromResultSetToCompany(ResultSet resultSet) throws MapperException {
		LinkedList<Company> company = new LinkedList<Company>();
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				company.add(new CompanyBuilder().withId(id).withName(name).build());
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

}