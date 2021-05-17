package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;

import com.excilys.cdb.model.Computer;

public class MapperComputer {

	public static LinkedList<Computer> map(ResultSet resultSet) throws SQLException {
		LinkedList<Computer> computer = new LinkedList<Computer>();
		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String name = resultSet.getString("name");
			String intro = resultSet.getString("introduced");
			LocalDate introduced = LocalDate.parse(intro);
			String discon = resultSet.getString("discontinued");
			LocalDate discontinued = LocalDate.parse(discon);
			int company_id = resultSet.getInt("company_id");
			computer.add(new Computer(id, name, introduced, discontinued, company_id));
		}
		return computer;
	}

}