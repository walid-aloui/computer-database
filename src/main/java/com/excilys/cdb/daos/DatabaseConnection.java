package com.excilys.cdb.daos;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exception.OpenException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Repository
public class DatabaseConnection {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnection.class);
	private static final String HIKARY_CONFIG_FILE = "datasource.properties";

	private HikariConfig config;
	private HikariDataSource ds;

	public DatabaseConnection() throws OpenException {
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

	public HikariDataSource getDs() {
		return ds;
	}

}