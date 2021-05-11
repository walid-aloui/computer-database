package daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import exception.InconsistentStateException;
import mapper.MapperCompany;
import model.Company;
import persistence.Database;

public class DaoCompany {

	// Attributs

	private static DaoCompany daoCompany;
	private Statement statement;

	// Methode qui renvoie l'instance unique de la classe

	public static DaoCompany create() {
		if (daoCompany == null)
			daoCompany = new DaoCompany();
		return daoCompany;
	}

	// Constructeur

	public DaoCompany() {
		Database db = Database.create();
		try {
			statement = db.getCon().createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Impossible de creer le statement");
		}
	}

	// Methode qui permet de recuperer tous les fabricants

	public LinkedList<Company> getAllCompanies() throws SQLException {
		Database db = Database.create();
		String query = "select id,name from company";
		ResultSet resultSet = statement.executeQuery(query);
		LinkedList<Company> allCompanies = MapperCompany.map(resultSet);
		return allCompanies;
	}

	// Methode qui permet de recuperer un fabricant avec son id

	public Company getCompanyById(int id) throws SQLException, InconsistentStateException {
		Database db = Database.create();
		String query = "select id,name from company where id = ?";
		PreparedStatement preparedStatement = db.getCon().prepareStatement(query);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		LinkedList<Company> company = MapperCompany.map(resultSet);
		switch (company.size()) {
		case 0:
			return null;

		case 1:
			return company.getFirst();

		default:
			throw new InconsistentStateException(
					"Le fabricant avec id = " + id + " figure plusieurs fois dans la base de donnée !");
		}
	}
}