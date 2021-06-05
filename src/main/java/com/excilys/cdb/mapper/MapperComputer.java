package com.excilys.cdb.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dtos.ComputerDto;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Company.CompanyBuilder;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Computer.ComputerBuilder;

public class MapperComputer {

	private static MapperComputer mapperComputer;
	private static final Logger LOGGER = LoggerFactory.getLogger(MapperComputer.class);

	public static MapperComputer getInstance() {
		if (mapperComputer == null) {
			mapperComputer = new MapperComputer();
		}
		return mapperComputer;
	}

	private MapperComputer() {
		super();
	}

	public LinkedList<Computer> fromResultSetToComputer(ResultSet resultSet) throws MapperException {
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
				Company company = null;
				if (companyId != 0 && companyName != null) {
					company = new CompanyBuilder().withId(companyId).withName(companyName).build();
				}
				computer.add(new ComputerBuilder().withId(id).withName(computerName).withIntroduced(introduced)
						.withDiscontinued(discontinued).withCompany(company).build());
			}
		} catch (SQLException e) {
			LOGGER.error("Echec fromResultSetToComputer", e);
			throw new MapperException();
		} catch (NullPointerException e) {
			LOGGER.error("Echec fromResultSetToComputer : NullPointerException");
			throw new MapperException();
		}
		return computer;
	}

	public int fromResultSetToInt(ResultSet resultSet) throws MapperException {
		try {
			if (resultSet.next()) {
				return resultSet.getInt("elements");
			} else {
				LOGGER.error("Echec fromResultSetToInt : curseur vide");
				throw new MapperException();
			}
		} catch (SQLException e) {
			LOGGER.error("Echec fromResultSetToInt", e);
			throw new MapperException();
		} catch (NullPointerException e) {
			LOGGER.error("Echec fromResultSetToInt : NullPointerException");
			throw new MapperException();
		}
	}

	public Computer fromComputerDtoToComputer(ComputerDto computerDto) {
		return null;
	}

}