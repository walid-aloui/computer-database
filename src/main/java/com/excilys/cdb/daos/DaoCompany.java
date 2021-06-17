package com.excilys.cdb.daos;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.mapper.RowMapperCompany;
import com.excilys.cdb.model.Company;

@Repository
public class DaoCompany {

	private static final String SELECT_ALL = 
			"SELECT id,name FROM company";
	
	private static final String SELECT_BY_ID = 
			SELECT_ALL + " WHERE id = :id";
	
	private static final String DELETE_BY_ID = 
			"DELETE FROM company WHERE id = :id";

	private static final Logger LOGGER = LoggerFactory.getLogger(DaoCompany.class);

	private DatabaseConnection dbConnection;
	private RowMapperCompany rowMapperCompany;
	private DaoComputer daoComputer;

	public DaoCompany(DatabaseConnection dbConnection, RowMapperCompany rowMapperCompany, DaoComputer daoComputer) {
		this.dbConnection = dbConnection;
		this.rowMapperCompany = rowMapperCompany;
		this.daoComputer = daoComputer;
	}

	public List<Company> selectAllCompanies() throws ExecuteQueryException {
		try {
			JdbcTemplate vJdbcTemplate = new JdbcTemplate(dbConnection.getDs());
			return vJdbcTemplate.query(SELECT_ALL, rowMapperCompany);
		} catch (DataAccessException e) {
			LOGGER.error("Echec selectAllCompanies", e);
			throw new ExecuteQueryException();
		}
	}

	public Optional<Company> selectCompanyById(int id) throws ExecuteQueryException {
		try {
			NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(dbConnection.getDs());
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("id", id);
			List<Company> companyList = vJdbcTemplate.query(SELECT_BY_ID, namedParameters, rowMapperCompany);
			if (companyList.isEmpty()) {
				return Optional.empty();
			} else {
				return Optional.of(companyList.get(0));
			}
		} catch (DataAccessException e) {
			LOGGER.error("Echec selectCompanyById", e);
			throw new ExecuteQueryException();
		}
	}

	@Transactional
	public int deleteCompanyById(int id) {
		daoComputer.deleteComputersByCompanyId(id);
		try {
			NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(dbConnection.getDs());
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("id", id);
			return vJdbcTemplate.update(DELETE_BY_ID, namedParameters);
		} catch (DataAccessException e) {
			LOGGER.error("Echec deleteCompanyById", e);
			return 0;
		}
	}

}