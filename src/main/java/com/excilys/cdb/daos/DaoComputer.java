package com.excilys.cdb.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.excilys.cdb.exception.InconsistentStateException;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.Database;

public class DaoComputer {

	private static DaoComputer daoComputer;

	public static DaoComputer getInstance() {
		if (daoComputer == null)
			daoComputer = new DaoComputer();
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

	public Computer getComputerById(int id) throws SQLException, InconsistentStateException {
		Database db = Database.getInstance();
		Connection con = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
		Statement statement = con.createStatement();
		String query = "select id,name,introduced,discontinued,company_id from computer where id = " + id;
		ResultSet resultSet = statement.executeQuery(query);
		LinkedList<Computer> computer = MapperComputer.map(resultSet);
		statement.close();
		con.close();
		switch (computer.size()) {
		case 0:
			return null;

		case 1:
			return computer.getFirst();

		default:
			throw new InconsistentStateException(
					"L'ordinateur avec id = " + id + " figure plusieurs fois dans la base de donnée !");
		}
	}

	public void updateComputerById(int id, String newName, String newIntroduced, String newDiscontinued,
			String newCompanyId) throws SQLException {
		Database db = Database.getInstance();
		Connection con = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
		String query = "update computer set name=?,introduced=?,discontinued=?,copany_id=? where id = " + id;
		PreparedStatement preparedStatement = con.prepareStatement(query);
		preparedStatement.setString(1, newName);
		preparedStatement.setString(2, newIntroduced);
		preparedStatement.setString(3, newDiscontinued);
		preparedStatement.setString(4, newCompanyId);
		preparedStatement.execute();
		preparedStatement.close();
		con.close();
		System.out.println("Nombre de update: " + preparedStatement.getUpdateCount());
	}

	public void deleteComputerById(int id) throws SQLException {
		Database db = Database.getInstance();
		Connection con = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
		Statement statement = con.createStatement();
		String query = "delete from computer where id = " + id;
		statement.execute(query);
		statement.close();
		con.close();
		System.out.println("Nombre de suppression: " + statement.getUpdateCount());
	}

	public void insertComputer(String name, String introduced, String discontinued, String company_id)
			throws SQLException {
		Database db = Database.getInstance();
		Connection con = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
		String query = "insert into computer (name, introduced, discontinued, company_id) values (?,?,?,?)";
		PreparedStatement preparedStatement = con.prepareStatement(query);
		preparedStatement.setString(1, name);
		preparedStatement.setString(2, introduced);
		preparedStatement.setString(3, discontinued);
		preparedStatement.setString(4, company_id);
		preparedStatement.execute();
		preparedStatement.close();
		con.close();
		System.out.println("Nombre d'insertion: " + preparedStatement.getUpdateCount());
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