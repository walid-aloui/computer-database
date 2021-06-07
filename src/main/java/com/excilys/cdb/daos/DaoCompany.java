package com.excilys.cdb.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.mapper.MapperCompany;
import com.excilys.cdb.model.Company;

public class DaoCompany {

	private static DaoCompany daoCompany;
	private static final Logger LOGGER = LoggerFactory.getLogger(DaoCompany.class);
	private static final String GET_ALL = "select id,name from company";
	private static final String GET_BY_ID = "select id,name from company where id = ?";
	private static final String DELETE_BY_ID = "delete from company where id = ?";

	public static DaoCompany getInstance() {
		if (daoCompany == null) {
			daoCompany = new DaoCompany();
		}
		return daoCompany;
	}

	private DaoCompany() {
	}

	public LinkedList<Company> getAllCompanies() throws OpenException, MapperException, ExecuteQueryException {
		Database db = Database.getInstance();
		try (Connection con = db.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(GET_ALL);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			LinkedList<Company> allCompanies = MapperCompany.getInstance().fromResultSetToCompany(resultSet);
			return allCompanies;
		} catch (SQLException e) {
			LOGGER.error("Echec getAllCompanies", e);
			throw new ExecuteQueryException();
		}
	}

	public Optional<Company> getCompanyById(int id) throws OpenException, MapperException, ExecuteQueryException {
		Database db = Database.getInstance();
		try (Connection con = db.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(GET_BY_ID);) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			LinkedList<Company> company = MapperCompany.getInstance().fromResultSetToCompany(resultSet);
			if (company.isEmpty()) {
				return Optional.empty();
			}
			return Optional.of(company.getFirst());
		} catch (SQLException e) {
			LOGGER.error("Echec getCompanyById", e);
			throw new ExecuteQueryException();
		}
	}

	public int deleteCompanyById(int id) throws OpenException, ExecuteQueryException {
		DaoComputer.getInstance().deleteComputersByCompanyId(id);
		Database db = Database.getInstance();
		try(Connection con = db.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(DELETE_BY_ID);){
			preparedStatement.setInt(1, id);
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Echec deleteCompanyById", e);
			throw new ExecuteQueryException();
		}
	}

}