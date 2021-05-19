package com.excilys.cdb.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;

import com.excilys.cdb.exception.CloseException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoComputer {

	private static DaoComputer daoComputer;
	private static final Logger LOGGER = LoggerFactory.getLogger(DaoComputer.class);

	public static DaoComputer getInstance() {
		if (daoComputer == null) {
			daoComputer = new DaoComputer();
		}
		return daoComputer;
	}

	private DaoComputer() {
	}

	public LinkedList<Computer> getAllComputers()
			throws OpenException, ExecuteQueryException, MapperException, CloseException {
		Database db = Database.getInstance();
		Connection con = db.openConnection();
		Statement statement = db.openStatement(con);
		String query = "select computer.id,computer.name,introduced,discontinued,company_id,company.name "
				+ "from computer left join company on computer.company_id = company.id";
		ResultSet resultSet = db.executeQuery(statement, query);
		LinkedList<Computer> allComputers = MapperComputer.mapToComputer(resultSet);
		db.closeStatement(statement);
		db.closeConnection(con);
		return allComputers;
	}

	public Optional<Computer> getComputerById(int id)
			throws OpenException, ExecuteQueryException, MapperException, CloseException {
		Database db = Database.getInstance();
		Connection con = db.openConnection();
		Statement statement = db.openStatement(con);
		String query = "select computer.id,computer.name,introduced,discontinued,company_id,company.name "
				+ "from computer left join company on computer.company_id = company.id where computer.id = " + id;
		ResultSet resultSet = db.executeQuery(statement, query);
		LinkedList<Computer> computer = MapperComputer.mapToComputer(resultSet);
		db.closeStatement(statement);
		db.closeConnection(con);
		if (computer.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(computer.getFirst());
	}

	public void updateComputerById(int id, String newName, Optional<LocalDate> newIntroduced,
			Optional<LocalDate> newDiscontinued, Optional<String> newCompanyId) throws OpenException, CloseException {
		Database db = Database.getInstance();
		Connection con = db.openConnection();
		PreparedStatement preparedStatement = null;
		String query = "update computer set name=?,introduced=?,discontinued=?,company_id=? where id = " + id;
		try {
			preparedStatement = con.prepareStatement(query);
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
			preparedStatement.execute();
			LOGGER.info("Nombre de update: " + preparedStatement.getUpdateCount());
		} catch (SQLException e) {
			System.out.println("Id du fabricant non existant !");
		}
		db.closePreparedStatement(preparedStatement);
		db.closeConnection(con);
	}

	public void deleteComputerById(int id) throws OpenException, ExecuteQueryException, CloseException {
		Database db = Database.getInstance();
		Connection con = db.openConnection();
		Statement statement = db.openStatement(con);
		String query = "delete from computer where id = " + id;
		db.execute(statement, query);
		db.closeStatement(statement);
		db.closeConnection(con);
	}

	public void insertComputer(String name, Optional<LocalDate> introduced, Optional<LocalDate> discontinued,
			Optional<String> company_id) throws OpenException, CloseException {
		Database db = Database.getInstance();
		Connection con = db.openConnection();
		String query = "insert into computer (name, introduced, discontinued, company_id) values (?,?,?,?)";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(query);
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
			preparedStatement.execute();
			LOGGER.info("Nombre d'insertion: " + preparedStatement.getUpdateCount());
		} catch (SQLException e) {
			System.out.println("Id du fabricant non existant !");
		}
		db.closePreparedStatement(preparedStatement);
		db.closeConnection(con);
	}

	public LinkedList<Computer> getPartOfComputers(int n, int offset)
			throws OpenException, ExecuteQueryException, MapperException, CloseException {
		Database db = Database.getInstance();
		Connection con = db.openConnection();
		Statement statement = db.openStatement(con);
		String query = "select computer.id,computer.name,introduced,discontinued,company_id,company.name "
				+ "from computer left join company on computer.company_id = company.id LIMIT " + offset + "," + n;
		ResultSet resultSet = db.executeQuery(statement, query);
		LinkedList<Computer> computers = MapperComputer.mapToComputer(resultSet);
		db.closeStatement(statement);
		db.closeConnection(con);
		return computers;
	}

	public int getNumberOfComputer() throws OpenException, ExecuteQueryException, MapperException, CloseException {
		Database db = Database.getInstance();
		Connection con = db.openConnection();
		Statement statement = db.openStatement(con);
		String query = "select count(*) as elements from computer";
		ResultSet resultSet = db.executeQuery(statement, query);
		int res = MapperComputer.mapToInt(resultSet);
		db.closeStatement(statement);
		db.closeConnection(con);
		return res;
	}

}