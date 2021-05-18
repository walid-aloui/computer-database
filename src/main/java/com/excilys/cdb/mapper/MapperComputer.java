package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.SecureInputs;

public class MapperComputer {

	public static LinkedList<Computer> map(ResultSet resultSet) throws SQLException {
		LinkedList<Computer> computer = new LinkedList<Computer>();
		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String name = resultSet.getString("name");
			String intro = resultSet.getString("introduced");
			Optional<LocalDate> introdu = SecureInputs.toDate(intro);
			LocalDate introduced = (introdu.isPresent()) ? introdu.get() : null;
			String discon = resultSet.getString("discontinued");
			Optional<LocalDate> discontinu = SecureInputs.toDate(discon);
			LocalDate discontinued = (discontinu.isPresent()) ? discontinu.get() : null;
			int company_id = resultSet.getInt("company_id");
			computer.add(new Computer(id, name, introduced, discontinued, company_id));
		}
		return computer;
	}

}