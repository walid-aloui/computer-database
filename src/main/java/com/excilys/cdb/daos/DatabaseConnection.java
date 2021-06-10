package com.excilys.cdb.daos;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.OpenException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConnection {

	private static final String HIKARY_CONFIG_FILE = "datasource.properties";

	private static HikariConfig config;
	private static HikariDataSource ds;

	private static DatabaseConnection dbConnection;
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
		InputStream configFile = classLoader.getResourceAsStream(HIKARY_CONFIG_FILE);

		try {
			properties.load(configFile);
		} catch (IOException e) {
			LOGGER.error("Echec de l'intéraction avec le fichier " + HIKARY_CONFIG_FILE + " " + e);
			throw new OpenException();
		}

		config = new HikariConfig(properties);
		ds = new HikariDataSource(config);
	}

	Connection openConnection() throws OpenException {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			LOGGER.error("Echec openConnection", e);
			throw new OpenException();
		}
	}

}