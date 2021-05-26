package com.excilys.cdb.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;

public class DaoComputer {

	private static DaoComputer daoComputer;
	private static final Logger LOGGER = LoggerFactory.getLogger(DaoComputer.class);
	private static final String GET_ALL = "select computer.id,computer.name,"
			+ "introduced,discontinued,company_id,company.name from computer "
			+ "left join company on computer.company_id = company.id";
	private static final String GET_BY_ID = "select computer.id,computer.name,"
			+ "introduced,discontinued,company_id,company.name from computer "
			+ "left join company on computer.company_id = company.id where computer.id = ?";
	private static final String DELETE_BY_ID = "delete from computer where id = ?";
	private static final String UPDATE_BY_ID = "update computer set name=?,"
			+ "introduced=?,discontinued=?,company_id=? where id = ?";
	private static final String INSERT = "insert into computer "
			+ "(name, introduced, discontinued, company_id) values (?,?,?,?)";
	private static final String GET_PART = "select computer.id,computer.name,"
			+ "introduced,discontinued,company_id,company.name from computer "
			+ "left join company on computer.company_id = company.id LIMIT ?,?";
	private static final String GET_NUMBER = "select count(*) as elements from computer";

	public static DaoComputer getInstance() {
		if (daoComputer == null) {
			daoComputer = new DaoComputer();
		}
		return daoComputer;
	}

	public static void setDaoComputer(DaoComputer daoComputer) {
		DaoComputer.daoComputer = daoComputer;
	}

	private DaoComputer() {
	}

	public LinkedList<Computer> getAllComputers() throws OpenException, MapperException, ExecuteQueryException {
		Database db = Database.getInstance();
		try (Connection con = db.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(GET_ALL);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			return MapperComputer.mapToComputer(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getAllComputers", e);
			throw new ExecuteQueryException();
		}

	}

	public Optional<Computer> getComputerById(int id) throws OpenException, MapperException, ExecuteQueryException {
		Database db = Database.getInstance();
		try (Connection con = db.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(GET_BY_ID);) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			LinkedList<Computer> computer = MapperComputer.mapToComputer(resultSet);
			return (computer.isEmpty()) ? Optional.empty() : Optional.of(computer.getFirst());
		} catch (SQLException e) {
			LOGGER.error("Echec getComputerById", e);
			throw new ExecuteQueryException();
		}
	}

	public int deleteComputerById(int id) throws OpenException, ExecuteQueryException {
		Database db = Database.getInstance();
		try (Connection con = db.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(DELETE_BY_ID);) {
			preparedStatement.setInt(1, id);
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Echec deleteComputerById", e);
			throw new ExecuteQueryException();
		}
	}

	public int updateComputerById(int id, String newName, Optional<LocalDate> newIntroduced,
			Optional<LocalDate> newDiscontinued, Optional<String> newCompanyId) throws OpenException {
		int numUpdate = 0;
		Database db = Database.getInstance();
		try (Connection con = db.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(UPDATE_BY_ID);) {
			preparedStatement.setString(1, newName);
			if (newIntroduced.isPresent()) {
				preparedStatement.setString(2, newIntroduced.get().toString());
			} else {
				preparedStatement.setString(2, null);
			}
			if (newDiscontinued.isPresent()) {
				preparedStatement.setString(3, newDiscontinued.get().toString());
			} else {
				preparedStatement.setString(3, null);
			}
			if (newCompanyId.isPresent()) {
				preparedStatement.setString(4, newCompanyId.get());
			} else {
				preparedStatement.setString(4, null);
			}
			preparedStatement.setInt(5, id);
			numUpdate = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Id du fabricant non existant !");
		}
		return numUpdate;
	}

	public int insertComputer(String name, Optional<LocalDate> introduced, Optional<LocalDate> discontinued,
			Optional<String> company_id) throws OpenException {
		int numInsert = 0;
		Database db = Database.getInstance();
		try (Connection con = db.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(INSERT);) {
			preparedStatement.setString(1, name);
			if (introduced.isPresent()) {
				preparedStatement.setString(2, introduced.get().toString());
			} else {
				preparedStatement.setString(2, null);
			}
			if (discontinued.isPresent()) {
				preparedStatement.setString(3, discontinued.get().toString());
			} else {
				preparedStatement.setString(3, null);
			}
			if (company_id.isPresent()) {
				preparedStatement.setString(4, company_id.get());
			} else {
				preparedStatement.setString(4, null);
			}
			numInsert = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Id du fabricant non existant !");
		}
		return numInsert;
	}

	public LinkedList<Computer> getPartOfComputers(int n, int offset)
			throws OpenException, MapperException, ExecuteQueryException {
		Database db = Database.getInstance();
		try (Connection con = db.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(GET_PART);) {
			preparedStatement.setInt(1, offset);
			preparedStatement.setInt(2, n);
			ResultSet resultSet = preparedStatement.executeQuery();
			return MapperComputer.mapToComputer(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getPartOfComputers", e);
			throw new ExecuteQueryException();
		}
	}

	public int getNumberOfComputer() throws OpenException, MapperException, ExecuteQueryException {
		Database db = Database.getInstance();
		try (Connection con = db.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(GET_NUMBER);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			return MapperComputer.mapToInt(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getNumberOfComputer", e);
			throw new ExecuteQueryException();
		}
	}

}