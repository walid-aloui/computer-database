package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.model.Company;

public class MapperCompany {

	public static LinkedList<Company> mapToCompany(ResultSet resultSet) throws MapperException {
		LinkedList<Company> company = new LinkedList<Company>();
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				company.add(new Company(id, name));
			}
		} catch (SQLException e) {
			System.out.println("Echec Mapper company " + e);
			throw new MapperException();
		}
		return company;
	}

}