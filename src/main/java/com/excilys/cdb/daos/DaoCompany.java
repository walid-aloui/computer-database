package com.excilys.cdb.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Optional;

import com.excilys.cdb.exception.CloseException;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.mapper.MapperCompany;
import com.excilys.cdb.model.Company;

public class DaoCompany {

	private static DaoCompany daoCompany;

	public static DaoCompany getInstance() {
		if (daoCompany == null) {
			daoCompany = new DaoCompany();
		}
		return daoCompany;
	}

	private DaoCompany() {
	}

	public LinkedList<Company> getAllCompanies()
			throws OpenException, ExecuteQueryException, MapperException, CloseException {
		Database db = Database.getInstance();
		Connection con = db.openConnection();
		Statement statement = db.openStatement(con);
		String query = "select id,name from company";
		ResultSet resultSet = db.executeQuery(statement, query);
		LinkedList<Company> allCompanies = MapperCompany.mapToCompany(resultSet);
		db.closeStatement(statement);
		db.closeConnection(con);
		return allCompanies;
	}

	public Optional<Company> getCompanyById(int id)
			throws OpenException, ExecuteQueryException, MapperException, CloseException {
		Database db = Database.getInstance();
		Connection con = db.openConnection();
		Statement statement = db.openStatement(con);
		String query = "select id,name from company where id = " + id;
		ResultSet resultSet = db.executeQuery(statement, query);
		LinkedList<Company> company = MapperCompany.mapToCompany(resultSet);
		db.closeStatement(statement);
		db.closeConnection(con);
		if (company.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(company.getFirst());
	}

}