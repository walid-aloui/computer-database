package com.excilys.cdb.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;

import com.excilys.cdb.exception.InconsistentStateException;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.Database;

public class DaoComputer {

	private static DaoComputer daoComputer;

	public static DaoComputer getInstance() {
		if (daoComputer == null) {
			daoComputer = new DaoComputer();
		}
		return daoComputer;
	}

	private DaoComputer() {
	}

	public LinkedList<Computer> getAllComputers() throws SQLException {
		Database db = Database.getInstance();
		Connection con = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
		Statement statement = con.createStatement();
		String query = "select id,name,introduced,discontinued,company_id from computer";
		ResultSet resultSet = statement.executeQuery(query);
		LinkedList<Computer> allComputers = MapperComputer.map(resultSet);
		statement.close();
		con.close();
		return allComputers;
	}

	public Optional<Computer> getComputerById(int id) throws SQLException, InconsistentStateException {
		Database db = Database.getInstance();
		Connection con = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
		Statement statement = con.createStatement();
		String query = "select id,name,introduced,discontinued,company_id from computer where id = " + id;
		ResultSet resultSet = statement.executeQuery(query);
		LinkedList<Computer> computer = MapperComputer.map(resultSet);
		statement.close();
		con.close();
		if (computer.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(computer.getFirst());
	}

	public void updateComputerById(int id, String newName, Optional<LocalDate> newIntroduced,
			Optional<LocalDate> newDiscontinued, Optional<String> newCompanyId) throws SQLException {
		Database db = Database.getInstance();
		Connection con = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
		String query = "update computer set name=?,introduced=?,discontinued=?,copany_id=? where id = " + id;
		PreparedStatement preparedStatement = con.prepareStatement(query);
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
		System.out.println("Nombre de update: " + preparedStatement.getUpdateCount());
		preparedStatement.close();
		con.close();
	}

	public void deleteComputerById(int id) throws SQLException {
		Database db = Database.getInstance();
		Connection con = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
		Statement statement = con.createStatement();
		String query = "delete from computer where id = " + id;
		statement.execute(query);
		System.out.println("Nombre de suppression: " + statement.getUpdateCount());
		statement.close();
		con.close();
	}

	public void insertComputer(String name, Optional<LocalDate> introduced, Optional<LocalDate> discontinued,
			Optional<String> company_id) throws SQLException {
		Database db = Database.getInstance();
		Connection con = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
		String query = "insert into computer (name, introduced, discontinued, company_id) values (?,?,?,?)";
		PreparedStatement preparedStatement = con.prepareStatement(query);
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
		System.out.println("Nombre d'insertion: " + preparedStatement.getUpdateCount());
		preparedStatement.close();
		con.close();
	}

	public LinkedList<Computer> getPartOfComputers(int n, int offset) throws SQLException {
		Database db = Database.getInstance();
		Connection con = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
		Statement statement = con.createStatement();
		String query = "select id,name,introduced,discontinued,company_id from computer LIMIT " + offset + "," + n;
		ResultSet resultSet = statement.executeQuery(query);
		LinkedList<Computer> computers = MapperComputer.map(resultSet);
		statement.close();
		con.close();
		return computers;
	}

	public int getNumberOfComputer() throws SQLException {
		Database db = Database.getInstance();
		Connection con = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
		Statement statement = con.createStatement();
		String query = "select count(*) as elements from computer";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			int res = resultSet.getInt("elements");
			statement.close();
			con.close();
			return res;
		}
		return -1;
	}

}