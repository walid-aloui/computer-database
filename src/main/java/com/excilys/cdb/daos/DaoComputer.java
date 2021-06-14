package com.excilys.cdb.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;

@Repository
public class DaoComputer {

	private ComputerQueryBuilder computerQueryBuilder;
	private MapperComputer mapperComputer;
	private DatabaseConnection dbConnection;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DaoComputer.class);

	public DaoComputer(ComputerQueryBuilder computerQueryBuilder, MapperComputer mapperComputer,
			DatabaseConnection dbConnection) {
		this.computerQueryBuilder = computerQueryBuilder;
		this.mapperComputer = mapperComputer;
		this.dbConnection = dbConnection;
	}

	public LinkedList<Computer> getComputersByCriteria(Map<String,String> criteria) throws OpenException, MapperException, ExecuteQueryException{
		String query = computerQueryBuilder.selectComputerByCriteria(criteria);
		try(Connection con = dbConnection.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(query)){
			ResultSet resultSet = preparedStatement.executeQuery();
			return mapperComputer.fromResultSetToComputerList(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getComputersByCriteria", e);
			throw new ExecuteQueryException();
		}
	}

	public LinkedList<Computer> getAllComputers() throws OpenException, MapperException, ExecuteQueryException {
		String query = computerQueryBuilder.selectAllComputer();
		try (Connection con = dbConnection.openConnection(); PreparedStatement preparedStatement = con.prepareStatement(query);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			return mapperComputer.fromResultSetToComputerList(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getAllComputers", e);
			throw new ExecuteQueryException();
		}

	}

	public Optional<Computer> getComputerById(int id) throws OpenException, MapperException, ExecuteQueryException {
		String query = computerQueryBuilder.selectComputerById(id);
		try (Connection con = dbConnection.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(query);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			LinkedList<Computer> computer = mapperComputer.fromResultSetToComputerList(resultSet);
			return (computer.isEmpty()) ? Optional.empty() : Optional.of(computer.getFirst());
		} catch (SQLException e) {
			LOGGER.error("Echec getComputerById", e);
			throw new ExecuteQueryException();
		}
	}

	public LinkedList<Computer> getComputerByName(String name)
			throws OpenException, MapperException, ExecuteQueryException {
		String query = computerQueryBuilder.selectComputerByName(name);
		try (Connection con = dbConnection.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(query);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			return mapperComputer.fromResultSetToComputerList(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getComputerByName", e);
			throw new ExecuteQueryException();
		}
	}

	public LinkedList<Computer> getPartOfComputers(int n, int offset)
			throws OpenException, MapperException, ExecuteQueryException {
		String query = computerQueryBuilder.selectPartOfComputer(n, offset);
		try (Connection con = dbConnection.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(query);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			return mapperComputer.fromResultSetToComputerList(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getPartOfComputers", e);
			throw new ExecuteQueryException();
		}
	}

	public LinkedList<Computer> getPartOfComputersByName(String name, int n, int offset)
			throws OpenException, MapperException, ExecuteQueryException {
		String query = computerQueryBuilder.selectPartOfComputerByName(name, n, offset);
		try (Connection con = dbConnection.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(query);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			return mapperComputer.fromResultSetToComputerList(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getComputerByName", e);
			throw new ExecuteQueryException();
		}
	}

	public int getNumberOfComputer() throws OpenException, MapperException, ExecuteQueryException {
		String query = computerQueryBuilder.getNumberOfComputer();
		try (Connection con = dbConnection.openConnection(); PreparedStatement preparedStatement = con.prepareStatement(query);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			return mapperComputer.fromResultSetToInt(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getNumberOfComputer", e);
			throw new ExecuteQueryException();
		}
	}

	public int deleteComputerById(int id) throws OpenException, ExecuteQueryException {
		String query = computerQueryBuilder.deleteComputerById(id);
		try (Connection con = dbConnection.openConnection(); PreparedStatement preparedStatement = con.prepareStatement(query);) {
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Echec deleteComputerById", e);
			throw new ExecuteQueryException();
		}
	}

	public int deleteComputersByCompanyId(int id) throws OpenException, ExecuteQueryException {
		String query = computerQueryBuilder.deleteComputerByCompanyId(id);
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
		try (Connection con = dbConnection.openConnection(); PreparedStatement preparedStatement = con.prepareStatement(query);) {
			numInsert = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Id du fabricant non existant !");
		}
		return numInsert;
	}
	
}