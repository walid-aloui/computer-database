package com.excilys.cdb.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.mapper.MapperCompany;
import com.excilys.cdb.model.Company;

@Repository
public class DaoCompany {

	private CompanyQueryBuilder companyQueryBuilder;
	private ComputerQueryBuilder computerQueryBuilder;
	private MapperCompany mapperCompany;
	private DatabaseConnection dbConnection;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DaoCompany.class);

	public DaoCompany(CompanyQueryBuilder companyQueryBuilder, ComputerQueryBuilder computerQueryBuilder,
			MapperCompany mapperCompany, DatabaseConnection dbConnection) {
		this.companyQueryBuilder = companyQueryBuilder;
		this.computerQueryBuilder = computerQueryBuilder;
		this.mapperCompany = mapperCompany;
		this.dbConnection = dbConnection;
	}

	public LinkedList<Company> getAllCompanies() throws OpenException, MapperException, ExecuteQueryException {
		String query = companyQueryBuilder.selectAllCompanies();
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
		try (Connection con = dbConnection.openConnection()) {
			con.setAutoCommit(false);
			deleteComputersByCompanyId(id, con);
			try (PreparedStatement preparedStatement = con.prepareStatement(query);) {
				int numDelete = preparedStatement.executeUpdate();
				con.commit();
				return numDelete;
			} catch (SQLException e) {
				LOGGER.error("Echec deleteCompanyById", e);
				try {
					con.rollback();
				} catch (SQLException e1) {
					LOGGER.error("Echec deleteCompanyById : rollback", e1);
				}
				throw new ExecuteQueryException();
			}
		} catch (SQLException e2) {
			LOGGER.error("Echec deleteCompanyById : openConnection", e2);
			throw new OpenException();
		}
	}
	
	private int deleteComputersByCompanyId(int id, Connection con) throws ExecuteQueryException {
		String query = computerQueryBuilder.deleteComputerByCompanyId(id);
		try {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Echec deleteComputersByCompanyId durant transaction", e);
			throw new ExecuteQueryException();
		}
		
	}

}