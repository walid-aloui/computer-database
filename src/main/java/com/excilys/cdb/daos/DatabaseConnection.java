package com.excilys.cdb.daos;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.OpenException;
import org.apache.commons.dbcp2.BasicDataSource;

public class DatabaseConnection extends BasicDataSource {

	private static DatabaseConnection dbConnection;
	private static final String DB_CONFIG_FILE = "db.properties";
	private static final String DRIVER_PROP = "jdbc.driverClassName";
	private static final String URl_PROP = "jdbc.url";
	private static final String USERNAME_PROP = "jdbc.user";
	private static final String PASSWORD_PROP = "jdbc.pass";
	private final String driver;
	private final String url;
	private final String username;
	private final String password;
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnection.class);

	public static DatabaseConnection getInstance() throws OpenException {
		if (dbConnection == null) {
			dbConnection = new DatabaseConnection();
		}
		return dbConnection;
	}

	private DatabaseConnection() throws OpenException {
		Properties properties = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream configFile = classLoader.getResourceAsStream(DB_CONFIG_FILE);
		try {
			properties.load(configFile);
			driver = properties.getProperty(DRIVER_PROP);
			url = properties.getProperty(URl_PROP);
			username = properties.getProperty(USERNAME_PROP);
			password = properties.getProperty(PASSWORD_PROP);
		} catch (IOException e) {
			LOGGER.error("Echec de l'intéraction avec le fichier " + DB_CONFIG_FILE + " " + e);
			throw new OpenException();
		}
		this.setDriverClassName(driver);
		this.setUrl(url);
		this.setUsername(username);
		this.setPassword(password);
	}

	Connection openConnection() throws OpenException {
		try {
			LOGGER.info("Connection to database : " + url);
			return this.getConnection();
		} catch (SQLException e) {
			LOGGER.error("Echec openConnection", e);
			throw new OpenException();
		}
	}

}