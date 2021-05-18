package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.SecureInputs;

public class MapperComputer {

	public static LinkedList<Computer> map(ResultSet resultSet) throws SQLException {
		LinkedList<Computer> computer = new LinkedList<Computer>();
		while (resultSet.next()) {
			int id = resultSet.getInt("computer.id");
			String computerName = resultSet.getString("computer.name");
			String intro = resultSet.getString("introduced");
			Optional<LocalDate> introdu = SecureInputs.toDate(intro);
			LocalDate introduced = (introdu.isPresent()) ? introdu.get() : null;
			String discon = resultSet.getString("discontinued");
			Optional<LocalDate> discontinu = SecureInputs.toDate(discon);
			LocalDate discontinued = (discontinu.isPresent()) ? discontinu.get() : null;
			int companyId = resultSet.getInt("company_id");
			String companyName = resultSet.getString("company.name");
			Company company = new Company(companyId, companyName);
			computer.add(new Computer(id, computerName, introduced, discontinued, company));
		}
		return computer;
	}

}