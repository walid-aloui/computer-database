package com.excilys.cdb.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	private ComputerQueryBuilder computerQueryBuilder;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DaoComputer.class);
	
	public static DaoComputer getInstance() {
		if (daoComputer == null) {
			daoComputer = new DaoComputer();
		}
		return daoComputer;
	}

	private DaoComputer() {
		computerQueryBuilder = ComputerQueryBuilder.getInstance();
	}

	public LinkedList<Computer> getAllComputers() throws OpenException, MapperException, ExecuteQueryException {
		String query = computerQueryBuilder.selectAllComputer();
		DatabaseConnection dbConnection = DatabaseConnection.getInstance();
		try (Connection con = dbConnection.openConnection(); PreparedStatement preparedStatement = con.prepareStatement(query);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			return MapperComputer.getInstance().fromResultSetToComputerList(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getAllComputers", e);
			throw new ExecuteQueryException();
		}

	}

	public Optional<Computer> getComputerById(int id) throws OpenException, MapperException, ExecuteQueryException {
		String query = computerQueryBuilder.selectComputerById(id);
		DatabaseConnection dbConnection = DatabaseConnection.getInstance();
		try (Connection con = dbConnection.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(query);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			LinkedList<Computer> computer = MapperComputer.getInstance().fromResultSetToComputerList(resultSet);
			return (computer.isEmpty()) ? Optional.empty() : Optional.of(computer.getFirst());
		} catch (SQLException e) {
			LOGGER.error("Echec getComputerById", e);
			throw new ExecuteQueryException();
		}
	}

	public LinkedList<Computer> getComputerByName(String name)
			throws OpenException, MapperException, ExecuteQueryException {
		String query = computerQueryBuilder.selectComputerByName(name);
		DatabaseConnection dbConnection = DatabaseConnection.getInstance();
		try (Connection con = dbConnection.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(query);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			return MapperComputer.getInstance().fromResultSetToComputerList(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getComputerByName", e);
			throw new ExecuteQueryException();
		}
	}

	public LinkedList<Computer> getPartOfComputers(int n, int offset)
			throws OpenException, MapperException, ExecuteQueryException {
		String query = computerQueryBuilder.selectPartOfComputer(n, offset);
		DatabaseConnection dbConnection = DatabaseConnection.getInstance();
		try (Connection con = dbConnection.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(query);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			return MapperComputer.getInstance().fromResultSetToComputerList(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getPartOfComputers", e);
			throw new ExecuteQueryException();
		}
	}

	public LinkedList<Computer> getPartOfComputersByName(String name, int n, int offset)
			throws OpenException, MapperException, ExecuteQueryException {
		String query = computerQueryBuilder.selectPartOfComputerByName(name, n, offset);
		DatabaseConnection dbConnection = DatabaseConnection.getInstance();
		try (Connection con = dbConnection.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(query);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			return MapperComputer.getInstance().fromResultSetToComputerList(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getComputerByName", e);
			throw new ExecuteQueryException();
		}
	}

	public int getNumberOfComputer() throws OpenException, MapperException, ExecuteQueryException {
		String query = computerQueryBuilder.getNumberOfComputer();
		DatabaseConnection dbConnection = DatabaseConnection.getInstance();
		try (Connection con = dbConnection.openConnection(); PreparedStatement preparedStatement = con.prepareStatement(query);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			return MapperComputer.getInstance().fromResultSetToInt(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getNumberOfComputer", e);
			throw new ExecuteQueryException();
		}
	}

	public int deleteComputerById(int id) throws OpenException, ExecuteQueryException {
		String query = computerQueryBuilder.deleteComputerById(id);
		DatabaseConnection dbConnection = DatabaseConnection.getInstance();
		try (Connection con = dbConnection.openConnection(); PreparedStatement preparedStatement = con.prepareStatement(query);) {
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Echec deleteComputerById", e);
			throw new ExecuteQueryException();
		}
	}

	public int deleteComputersByCompanyId(int id) throws OpenException, ExecuteQueryException {
		String query = computerQueryBuilder.deleteComputerByCompanyId(id);
		DatabaseConnection dbConnection = DatabaseConnection.getInstance();
		try (Connection con = dbConnection.openConnection(); PreparedStatement preparedStatement = con.prepareStatement(query);) {
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Echec deleteComputersByCompanyId", e);
			throw new ExecuteQueryException();
		}
	}

	public int updateComputerById(int id, Computer computer) throws OpenException {
		String query = computerQueryBuilder.updateComputerById(id, computer);
		int numUpdate = 0;
		DatabaseConnection dbConnection = DatabaseConnection.getInstance();
		try (Connection con = dbConnection.openConnection(); PreparedStatement preparedStatement = con.prepareStatement(query);) {
			numUpdate = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Id du fabricant non existant !");
		}
		return numUpdate;
	}

	public int insertComputer(Computer computer) throws OpenException {
		String query = computerQueryBuilder.insertComputer(computer);
		int numInsert = 0;
		DatabaseConnection dbConnection = DatabaseConnection.getInstance();
		try (Connection con = dbConnection.openConnection(); PreparedStatement preparedStatement = con.prepareStatement(query);) {
			numInsert = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Id du fabricant non existant !");
		}
		return numInsert;
	}

}