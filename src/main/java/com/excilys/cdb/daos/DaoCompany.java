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
	private CompanyQueryBuilder companyQueryBuilder;
	private MapperCompany mapperCompany;
	private static final Logger LOGGER = LoggerFactory.getLogger(DaoCompany.class);
	
	public static DaoCompany getInstance() {
		if (daoCompany == null) {
			daoCompany = new DaoCompany();
		}
		return daoCompany;
	}

	private DaoCompany() {
		companyQueryBuilder = CompanyQueryBuilder.getInstance();
		mapperCompany = MapperCompany.getInstance();
	}

	public LinkedList<Company> getAllCompanies() throws OpenException, MapperException, ExecuteQueryException {
		String query = companyQueryBuilder.selectAllCompanies();
		DatabaseConnection dbConnection = DatabaseConnection.getInstance();
		try (Connection con = dbConnection.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(query);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			return mapperCompany.fromResultSetToCompany(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getAllCompanies", e);
			throw new ExecuteQueryException();
		}
	}

	public Optional<Company> getCompanyById(int id) throws OpenException, MapperException, ExecuteQueryException {
		String query = companyQueryBuilder.selectCompanyById(id);
		DatabaseConnection dbConnection = DatabaseConnection.getInstance();
		try (Connection con = dbConnection.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(query);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			LinkedList<Company> company = mapperCompany.fromResultSetToCompany(resultSet);
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
		String query = companyQueryBuilder.deleteCompanyById(id);
		DaoComputer.getInstance().deleteComputersByCompanyId(id);
		DatabaseConnection dbConnection = DatabaseConnection.getInstance();
		try (Connection con = dbConnection.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(query);) {
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Echec deleteCompanyById", e);
			throw new ExecuteQueryException();
		}
	}

}