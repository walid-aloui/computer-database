package com.excilys.cdb.daos;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.CloseException;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.OpenException;
import org.apache.commons.dbcp2.BasicDataSource;

public class Database extends BasicDataSource {

	private static Database db;
	private static final String DB_CONFIG_FILE = "db.properties";
	private static final String URl_PROP = "jdbc.url";
	private static final String USERNAME_PROP = "jdbc.user";
	private static final String PASSWORD_PROP = "jdbc.pass";
	private final String url;
	private final String username;
	private final String password;
	private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);

	public static Database getInstance() throws OpenException {
		if (db == null) {
			db = new Database();
		}
		return db;
	}

	private Database() throws OpenException {
		Properties properties = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream configFile = classLoader.getResourceAsStream(DB_CONFIG_FILE);
		try {
			properties.load(configFile);
			url = properties.getProperty(URl_PROP);
			username = properties.getProperty(USERNAME_PROP);
			password = properties.getProperty(PASSWORD_PROP);
		} catch (IOException e) {
			LOGGER.error("Echec de l'intéraction avec le fichier " + DB_CONFIG_FILE + " " + e);
			throw new OpenException();
		}
		this.setUrl(url);
		this.setUsername(username);
		this.setPassword(password);
	}

	Connection openConnection() throws OpenException {
		try {
			return this.getConnection();
		} catch (SQLException e) {
			LOGGER.error("Echec openConnection", e);
			throw new OpenException();
		}
		/*
		 * try { LOGGER.info("Connection to database : " + url); return
		 * DriverManager.getConnection(url, username, password); } catch (SQLException
		 * e) { LOGGER.error("Echec openConnection", e); throw new OpenException(); }
		 */
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
		if (con != null) {
			try {
				con.close();
				LOGGER.info("Disconnection from database : " + url);
			} catch (SQLException e) {
				LOGGER.error("Echec closeConnection", e);
				throw new CloseException();
			}
		}
	}

	void closeStatement(Statement statement) throws CloseException {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				LOGGER.error("Echec closeStatement", e);
				throw new CloseException();
			}
		}
	}

	void closePreparedStatement(PreparedStatement preparedStatement) throws CloseException {
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				LOGGER.error("Echec closePreparedStatement", e);
				throw new CloseException();
			}
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

	int executeUpdate(Statement statement, String query) throws ExecuteQueryException {
		try {
			return statement.executeUpdate(query);
		} catch (SQLException e) {
			LOGGER.error("Echec execute", e);
			throw new ExecuteQueryException();
		}
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}