package daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import exception.InconsistentStateException;
import mapper.MapperComputer;
import model.Computer;
import persistence.Database;
import utils.SecureInputs;

public class DaoComputer {

	// Attributs

	private static DaoComputer daoComputer;
	private Statement statement;

	// Methode qui renvoie l'instance unique de la classe

	public static DaoComputer create() {
		if (daoComputer == null)
			daoComputer = new DaoComputer();
		return daoComputer;
	}

	// Constructeur

	public DaoComputer() {
		Database db = Database.create();
		try {
			statement = db.getCon().createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Impossible de creer le statement");
		}
	}

	// Methode qui permet de recuperer tous les ordinateurs

	public LinkedList<Computer> getAllComputers() throws SQLException {
		String query = "select id,name,introduced,discontinued,company_id from computer";
		ResultSet resultSet = statement.executeQuery(query);
		LinkedList<Computer> allComputers = MapperComputer.map(resultSet);
		return allComputers;
	}

	// Methode qui permet de recuperer un ordinateur avec son id

	public Computer getComputerById(int id) throws SQLException, InconsistentStateException {
		Database db = Database.create();
		String query = "select id,name,introduced,discontinued,company_id from computer where id = ?";
		PreparedStatement preparedStatement = db.getCon().prepareStatement(query);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		LinkedList<Computer> computer = MapperComputer.map(resultSet);
		switch (computer.size()) {
		case 0:
			return null;

		case 1:
			return computer.getFirst();

		default:
			throw new InconsistentStateException(
					"L'ordinateur avec id = " + id + " figure plusieurs fois dans la base de donnée !");
		}
	}

	// Methode qui permet d'update le champ d'un ordinateur

	public void updateComputerById(int id, String newName, String newIntroduced, String newDiscontinued,
			String newCompanyId) throws SQLException {
		String query = "update computer set name=" + SecureInputs.addQuote(newName) + ",introduced="
				+ SecureInputs.addQuote(newIntroduced) + ",discontinued=" + SecureInputs.addQuote(newDiscontinued)
				+ ",company_id=" + SecureInputs.addQuote(newCompanyId) + " where id = " + id;
		statement.execute(query);
	}

	// Methode qui permet de supprimer un ordinateur

	public void deleteComputerById(int id) throws SQLException {
		String query = "delete from computer where id = " + id;
		statement.execute(query);
		System.out.println("Nombre de suppression: " + statement.getUpdateCount());
	}

	// Methode qui permet d'inserer un ordinateur

	public void insertComputer(String name, String introduced, String discontinued, String company_id)
			throws SQLException {
		String query = "insert into computer (name, introduced, discontinued, company_id) values ("
				+ SecureInputs.addQuote(name) + "," + SecureInputs.addQuote(introduced) + ","
				+ SecureInputs.addQuote(discontinued) + "," + SecureInputs.addQuote(company_id) + ")";
		statement.execute(query);
		System.out.println("Nombre d'insertion: " + statement.getUpdateCount());
	}

}