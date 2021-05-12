package mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import model.Company;

public class MapperCompany {

	// Methode qui permet de transformer un ResultSet en liste de fabricant

	public static LinkedList<Company> map(ResultSet resultSet) throws SQLException {
		LinkedList<Company> company = new LinkedList<Company>();
		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String name = resultSet.getString("name");
			company.add(new Company(id, name));
		}
		return company;
	}

}