package mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import model.Computer;

public class MapperComputer {

	// Methode qui permet de transformer un ResultSet en liste d'ordinateur

	public static LinkedList<Computer> map(ResultSet resultSet) throws SQLException {
		LinkedList<Computer> computer = new LinkedList<Computer>();
		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String name = resultSet.getString("name");
			String introduced = resultSet.getString("introduced");
			String discontinued = resultSet.getString("discontinued");
			int company_id = resultSet.getInt("company_id");
			computer.add(new Computer(id, name, introduced, discontinued, company_id));
		}
		return computer;
	}

}