package com.excilys.cdb.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Optional;

import com.excilys.cdb.exception.InconsistentStateException;
import com.excilys.cdb.mapper.MapperCompany;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.Database;

public class DaoCompany {

	private static DaoCompany daoCompany;

	public static DaoCompany getInstance() {
		if (daoCompany == null)
			daoCompany = new DaoCompany();
		return daoCompany;
	}

	private DaoCompany() {
	}

	public LinkedList<Company> getAllCompanies() throws SQLException {
		Database db = Database.getInstance();
		Connection con = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
		Statement statement = con.createStatement();
		String query = "select id,name from company";
		ResultSet resultSet = statement.executeQuery(query);
		LinkedList<Company> allCompanies = MapperCompany.map(resultSet);
		statement.close();
		con.close();
		return allCompanies;
	}

	public Optional<Company> getCompanyById(int id) throws SQLException, InconsistentStateException {
		Database db = Database.getInstance();
		Connection con = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
		Statement statement = con.createStatement();
		String query = "select id,name from company where id = " + id;
		ResultSet resultSet = statement.executeQuery(query);
		LinkedList<Company> company = MapperCompany.map(resultSet);
		statement.close();
		con.close();
		if (company.isEmpty())
			return Optional.empty();
		return Optional.of(company.getFirst());
	}

	public boolean isCompany(int id) throws SQLException, InconsistentStateException {
		return getCompanyById(id).isPresent();
	}

}