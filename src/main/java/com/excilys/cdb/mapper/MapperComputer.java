package com.excilys.cdb.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public class MapperComputer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapperComputer.class);

	public static LinkedList<Computer> mapToComputer(ResultSet resultSet) throws MapperException {
		LinkedList<Computer> computer = new LinkedList<Computer>();
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("computer.id");
				String computerName = resultSet.getString("computer.name");
				LocalDate introduced = null;
				LocalDate discontinued = null;
				Date intro = resultSet.getDate("introduced");
				if (intro != null) {
					introduced = intro.toLocalDate();
				}
				Date discon = resultSet.getDate("discontinued");
				if (discon != null) {
					discontinued = discon.toLocalDate();
				}
				int companyId = resultSet.getInt("company_id");
				String companyName = resultSet.getString("company.name");
				Company company = new Company(companyId, companyName);
				computer.add(new Computer(id, computerName, introduced, discontinued, company));
			}
		} catch (SQLException e) {
			LOGGER.error("Echec Mapper computer", e);
			throw new MapperException();
		} catch (NullPointerException e) {
			LOGGER.error("Echec Mapper computer : NullPointerException");
			throw new MapperException();
		}
		return computer;
	}

	public static int mapToInt(ResultSet resultSet) throws MapperException {
		try {
			if (resultSet.next()) {
				return resultSet.getInt("elements");
			} else {
				LOGGER.error("Echec Mapper mapToInt : curseur vide");
				throw new MapperException();
			}
		} catch (SQLException e) {
			LOGGER.error("Echec Mapper mapToInt", e);
			throw new MapperException();
		} catch (NullPointerException e) {
			LOGGER.error("Echec Mapper computer : NullPointerException");
			throw new MapperException();
		}
	}

}