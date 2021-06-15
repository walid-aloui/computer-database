package com.excilys.cdb.daos;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.dtos.ComputerDto;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;

@Repository
public class DaoComputer {

	private static final String SELECT_ALL = 
			"SELECT computer.id,computer.name,introduced,discontinued,company.id,company.name"
			+ " FROM computer"
			+ " LEFT JOIN company" 
			+ " ON (computer.company_id = company.id)";

	private static final String SELECT_BY_ID = 
			SELECT_ALL 
			+ " WHERE computer.id = :id";

	private static final String SELECT_BY_NAME = 
			SELECT_ALL 
			+ " WHERE computer.name = :name";

	private static final String SELECT_NUM_COMPUTER = 
			"SELECT COUNT(*)"
			+ " FROM computer";

	private static final String SELECT_WITH_LIMIT = 
			SELECT_ALL 
			+ " LIMIT :offset, :limit";

	private static final String SELECT_BY_NAME_WITH_LIMIT = 
			SELECT_ALL 
			+ " WHERE computer.name = :name"
			+ " LIMIT :offset, :limit";

	private static final String SELECT_BY_CRITERIA = 
			SELECT_ALL 
			+ " WHERE computer.name LIKE :computerName"
			+ " OR computer.company_id LIKE :company_id" 
			+ " ORDER BY :order :mode" 
			+ " LIMIT :offset, :limit";

	private static final String DELETE_BY_ID = 
			"DELETE"
			+ " FROM computer"
			+ " WHERE id = :id";

	private static final String DELETE_BY_COMPANY_ID = 
			"DELETE"
			+ " FROM computer"
			+ " WHERE company_id = :company_id";

	private static final String UPDATE_BY_ID = 
			"UPDATE computer SET" 
			+ " name = :name," 
			+ " introduced = :introduced,"
			+ " discontinued = :discontinued," 
			+ " company_id = :companyId" 
			+ " WHERE id = :id";

	private static final String INSERT = 
			"INSERT"
			+ " INTO computer(name,introduced,discontinued,company_id)"
			+ " VALUES (:name, :introduced, :discontinued, :companyId)";

	private static final String KEY_NAME = "computerName";
	private static final String KEY_ORDER = "orderBy";
	private static final String KEY_MODE = "mode";
	private static final String KEY_LIMIT = "limit";
	private static final String KEY_OFFSET = "offset";

	private static final Logger LOGGER = LoggerFactory.getLogger(DaoComputer.class);

	private DatabaseConnection dbConnection;
	private MapperComputer mapperComputer;
	private RowMapper<Computer> rowMapperComputer;

	public DaoComputer(DatabaseConnection dbConnection, MapperComputer mapperComputer,
			RowMapper<Computer> rowMapperComputer) {
		this.dbConnection = dbConnection;
		this.mapperComputer = mapperComputer;
		this.rowMapperComputer = rowMapperComputer;
	}

	public List<Computer> selectAllComputers() throws ExecuteQueryException {
		try {
			JdbcTemplate vJdbcTemplate = new JdbcTemplate(dbConnection.getDs());
			return vJdbcTemplate.query(SELECT_ALL, rowMapperComputer);
		} catch (DataAccessException e) {
			LOGGER.error("Echec getAllComputers", e);
			throw new ExecuteQueryException();
		}
	}

	public Optional<Computer> selectComputerById(int id) throws ExecuteQueryException {
		try {
			NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(dbConnection.getDs());
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("id", id);
			List<Computer> computerList = vJdbcTemplate.query(SELECT_BY_ID, namedParameters, rowMapperComputer);
			if (computerList.isEmpty()) {
				return Optional.empty();
			} else {
				return Optional.of(computerList.get(0));
			}
		} catch (DataAccessException e) {
			LOGGER.error("Echec getComputerById", e);
			throw new ExecuteQueryException();
		}
	}

	public List<Computer> selectComputersByName(String name) throws ExecuteQueryException {
		try {
			NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(dbConnection.getDs());
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("name", name);
			return vJdbcTemplate.query(SELECT_BY_NAME, namedParameters, rowMapperComputer);
		} catch (DataAccessException e) {
			LOGGER.error("Echec getComputersByName", e);
			throw new ExecuteQueryException();
		}
	}

	public List<Computer> selectPartOfComputers(int limit, int offset) throws ExecuteQueryException {
		try {
			NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(dbConnection.getDs());
			MapSqlParameterSource nameParameters = new MapSqlParameterSource();
			nameParameters.addValue("offset", offset);
			nameParameters.addValue("limit", limit);
			return vJdbcTemplate.query(SELECT_WITH_LIMIT, nameParameters, rowMapperComputer);
		} catch (DataAccessException e) {
			LOGGER.error("Echec getPartOfComputers", e);
			throw new ExecuteQueryException();
		}
	}

	public List<Computer> selectPartOfComputersByName(String name, int limit, int offset) throws ExecuteQueryException {
		try {
			NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(dbConnection.getDs());
			MapSqlParameterSource nameParameters = new MapSqlParameterSource();
			nameParameters.addValue("offset", offset);
			nameParameters.addValue("limit", limit);
			nameParameters.addValue("name", name);
			return vJdbcTemplate.query(SELECT_BY_NAME_WITH_LIMIT, nameParameters, rowMapperComputer);
		} catch (DataAccessException e) {
			LOGGER.error("Echec getPartOfComputersByName", e);
			throw new ExecuteQueryException();
		}
	}

	public List<Computer> selectComputersByCriteria(Map<String, String> criteria) throws ExecuteQueryException {
		try {
			NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(dbConnection.getDs());
			MapSqlParameterSource nameParameters = new MapSqlParameterSource();
			nameParameters.addValue("name", criteria.get(KEY_NAME));
			nameParameters.addValue("order", criteria.get(KEY_ORDER));
			nameParameters.addValue("mode", criteria.get(KEY_MODE));
			nameParameters.addValue("offset", criteria.get(KEY_OFFSET));
			nameParameters.addValue("limit", criteria.get(KEY_LIMIT));
			return vJdbcTemplate.query(SELECT_BY_CRITERIA, nameParameters, rowMapperComputer);
		} catch (DataAccessException e) {
			LOGGER.error("Echec getComputersByCriteria", e);
			throw new ExecuteQueryException();
		}
	}

	public int selectNumberOfComputer() throws ExecuteQueryException {
		try {
			JdbcTemplate vJdbcTemplate = new JdbcTemplate(dbConnection.getDs());
			return vJdbcTemplate.queryForObject(SELECT_NUM_COMPUTER, Integer.class);
		} catch (DataAccessException e) {
			LOGGER.error("Echec getNumberOfComputer", e);
			throw new ExecuteQueryException();
		}
	}

	public int deleteComputerById(int id) {
		try {
			NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(dbConnection.getDs());
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("id", id);
			return vJdbcTemplate.update(DELETE_BY_ID, namedParameters);
		} catch (DataAccessException e) {
			LOGGER.error("Echec deleteComputerById", e);
			return 0;
		}
	}

	public int deleteComputersByCompanyId(int id) {
		try {
			NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(dbConnection.getDs());
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("company_id", id);
			return vJdbcTemplate.update(DELETE_BY_COMPANY_ID, namedParameters);
		} catch (DataAccessException e) {
			LOGGER.error("Echec deleteComputersByCompanyId", e);
			return 0;
		}
	}

	public int updateComputer(Computer computer) {
		try {
			NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(dbConnection.getDs());
			ComputerDto computerDto = mapperComputer.fromComputerToComputerDto(computer);
			SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(computerDto);
			return vJdbcTemplate.update(UPDATE_BY_ID, namedParameters);
		} catch (DataAccessException e) {
			LOGGER.error("Echec updateComputerById", e);
			return 0;
		}
	}

	public int insertComputer(Computer computer) {
		try {
			NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(dbConnection.getDs());
			ComputerDto computerDto = mapperComputer.fromComputerToComputerDto(computer);
			SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(computerDto);
			return vJdbcTemplate.update(INSERT, namedParameters);
		} catch (DataAccessException e) {
			LOGGER.error("Echec insertComputer", e);
			return 0;
		}
	}

}