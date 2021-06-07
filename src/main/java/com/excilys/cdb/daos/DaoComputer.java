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
	private static final Logger LOGGER = LoggerFactory.getLogger(DaoComputer.class);
	private static final String GET_ALL = "select computer.id,computer.name,introduced,discontinued,company_id,company.name "
											+ "from computer "
											+ "left join company "
											+ "on company_id = company.id";
	private static final String GET_BY_ID = "select computer.id,computer.name,introduced,discontinued,company_id,company.name "
											+ "from computer "
											+ "left join company "
											+ "on company_id = company.id "
											+ "where computer.id = ?";
	private static final String DELETE_BY_ID = "delete from computer "
											+ "where id = ?";
	private static final String UPDATE_BY_ID = "update computer set name=?,introduced=?,discontinued=?,company_id=? "
											+ "where id = ?";
	private static final String INSERT = "insert into computer (name, introduced, discontinued, company_id) "
											+ "values (?,?,?,?)";
	private static final String GET_PART = "select computer.id,computer.name,introduced,discontinued,company_id,company.name "
											+ "from computer "
											+ "left join company "
											+ "on company_id = company.id "
											+ "LIMIT ?,?";
	private static final String GET_NUMBER = "select count(*) as elements from computer";
	private static final String GET_BY_NAME = "select computer.id,computer.name,introduced,discontinued,company_id,company.name "
											+ "from computer "
											+ "left join company "
											+ "on company_id = company.id "
											+ "where computer.name = ?";
	private static final String GET_PART_BY_NAME = "select computer.id,computer.name,introduced,discontinued,company_id,company.name "
											+ "from computer "
											+ "left join company "
											+ "on company_id = company.id "
											+ "where computer.name = ?"
											+ "LIMIT ?,?";

	public static DaoComputer getInstance() {
		if (daoComputer == null) {
			daoComputer = new DaoComputer();
		}
		return daoComputer;
	}

	private DaoComputer() {
	}

	public LinkedList<Computer> getAllComputers() throws OpenException, MapperException, ExecuteQueryException {
		Database db = Database.getInstance();
		try (Connection con = db.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(GET_ALL);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			return MapperComputer.getInstance().fromResultSetToComputerList(resultSet);
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
			LinkedList<Computer> computer = MapperComputer.getInstance().fromResultSetToComputerList(resultSet);
			return (computer.isEmpty()) ? Optional.empty() : Optional.of(computer.getFirst());
		} catch (SQLException e) {
			LOGGER.error("Echec getComputerById", e);
			throw new ExecuteQueryException();
		}
	}

	public LinkedList<Computer> getComputerByName(String computerName)
			throws OpenException, MapperException, ExecuteQueryException {
		Database db = Database.getInstance();
		try (Connection con = db.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(GET_BY_NAME);) {
			preparedStatement.setString(1, computerName);
			ResultSet resultSet = preparedStatement.executeQuery();
			return MapperComputer.getInstance().fromResultSetToComputerList(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getComputerByName", e);
			throw new ExecuteQueryException();
		}
	}
	
	public LinkedList<Computer> getPartOfComputers(int n, int offset)
			throws OpenException, MapperException, ExecuteQueryException {
		Database db = Database.getInstance();
		try (Connection con = db.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(GET_PART);) {
			preparedStatement.setInt(1, offset);
			preparedStatement.setInt(2, n);
			ResultSet resultSet = preparedStatement.executeQuery();
			return MapperComputer.getInstance().fromResultSetToComputerList(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getPartOfComputers", e);
			throw new ExecuteQueryException();
		}
	}
	
	public LinkedList<Computer> getPartOfComputersByName(String name, int n, int offset)
			throws OpenException, MapperException, ExecuteQueryException {
		Database db = Database.getInstance();
		try (Connection con = db.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(GET_PART_BY_NAME);) {
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, offset);
			preparedStatement.setInt(3, n);
			ResultSet resultSet = preparedStatement.executeQuery();
			return MapperComputer.getInstance().fromResultSetToComputerList(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getComputerByName", e);
			throw new ExecuteQueryException();
		}
	}

	public int getNumberOfComputer() throws OpenException, MapperException, ExecuteQueryException {
		Database db = Database.getInstance();
		try (Connection con = db.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(GET_NUMBER);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			return MapperComputer.getInstance().fromResultSetToInt(resultSet);
		} catch (SQLException e) {
			LOGGER.error("Echec getNumberOfComputer", e);
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

	public int updateComputerById(int id, Computer computer) throws OpenException {
		int numUpdate = 0;
		Database db = Database.getInstance();
		try (Connection con = db.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(UPDATE_BY_ID);) {
			preparedStatement.setString(1, computer.getName());
			if (computer.getIntroduced() == null) {
				preparedStatement.setString(2, null);
			} else {
				preparedStatement.setString(2, computer.getIntroduced().toString());
			}
			if (computer.getDiscontinued() == null) {
				preparedStatement.setString(3, null);
			} else {
				preparedStatement.setString(3, computer.getDiscontinued().toString());
			}
			if (computer.getCompany() == null) {
				preparedStatement.setString(4, null);
			} else {
				preparedStatement.setInt(4, computer.getCompany().getId());
			}
			preparedStatement.setInt(5, id);
			numUpdate = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Id du fabricant non existant !");
		}
		return numUpdate;
	}

	public int insertComputer(Computer computer) throws OpenException {
		int numInsert = 0;
		Database db = Database.getInstance();
		try (Connection con = db.openConnection();
				PreparedStatement preparedStatement = con.prepareStatement(INSERT);) {
			preparedStatement.setString(1, computer.getName());
			if (computer.getIntroduced() == null) {
				preparedStatement.setString(2, null);
			} else {
				preparedStatement.setString(2, computer.getIntroduced().toString());
			}
			if (computer.getDiscontinued() == null) {
				preparedStatement.setString(3, null);
			} else {
				preparedStatement.setString(3, computer.getDiscontinued().toString());
			}
			if (computer.getCompany() == null) {
				preparedStatement.setString(4, null);
			} else {
				preparedStatement.setInt(4, computer.getCompany().getId());
			}
			numInsert = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Id du fabricant non existant !");
		}
		return numInsert;
	}

}