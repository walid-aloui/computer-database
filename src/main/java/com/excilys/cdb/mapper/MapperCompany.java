package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import com.excilys.cdb.model.Company;

public class MapperCompany {

	public static LinkedList<Company> map(ResultSet resultSet) throws SQLException {
		LinkedList<Company> company = new LinkedList<Company>();
		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String name = resultSet.getString("name");
			company.add(new Company(id, name));
		}
		return company;
	}

}