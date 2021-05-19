package com.excilys.cdb.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.CloseException;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.OpenException;

public class Database {

	private static Database db;
	private final String url = "jdbc:mysql://localhost:3306/computer-database-db";
	private final String username = "admincdb";
	private final String password = "qwerty1234";
	private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);

	public static Database getInstance() {
		if (db == null) {
			db = new Database();
		}
		return db;
	}

	private Database() {
	}

	Connection openConnection() throws OpenException {
		try {
			return DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			LOGGER.error("Echec openConnection", e);
			throw new OpenException();
		}
	}

	Statement openStatement(Connection con) throws OpenException {
		try {
			return con.createStatement();
		} catch (SQLException e) {
			LOGGER.error("Echec openStatement", e);
			throw new OpenException();
		}
	}

	void closeConnection(Connection con) throws CloseException {
		try {
			con.close();
		} catch (SQLException e) {
			LOGGER.error("Echec closeConnection", e);
			throw new CloseException();
		}
	}

	void closeStatement(Statement statement) throws CloseException {
		try {
			statement.close();
		} catch (SQLException e) {
			LOGGER.error("Echec closeStatement", e);
			throw new CloseException();
		}
	}

	void closePreparedStatement(PreparedStatement preparedStatement) throws CloseException {
		try {
			preparedStatement.close();
		} catch (SQLException e) {
			LOGGER.error("Echec closePreparedStatement", e);
			throw new CloseException();
		}
	}

	ResultSet executeQuery(Statement statement, String query) throws ExecuteQueryException {
		try {
			return statement.executeQuery(query);
		} catch (SQLException e) {
			LOGGER.error("Echec executeQuery", e);
			throw new ExecuteQueryException();
		}
	}

	void execute(Statement statement, String query) throws ExecuteQueryException {
		try {
			statement.execute(query);
			LOGGER.info("Nombre de ligne affecte: " + statement.getUpdateCount());
		} catch (SQLException e) {
			LOGGER.error("Echec execute", e);
			throw new ExecuteQueryException();
		}
	}

}