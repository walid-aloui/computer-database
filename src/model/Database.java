package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	// Attributs

	public static Database db;
	private Connection con;
	private Statement statement;

	// Methode static qui renvoie l'instance unique de la bdd

	public static Database create() throws SQLException {
		if (db == null)
			db = new Database();
		return db;
	}

	// Constructeurs

	private Database() throws SQLException {
		String url = "jdbc:mysql://localhost:3306/computer-database-db";
		String username = "admincdb";
		String password = "qwerty1234";

		con = (Connection) DriverManager.getConnection(url, username, password);
		statement = con.createStatement();
	}

	// Methode qui permet de renvoyer la liste des fabriquants

	public ResultSet getCompanies() throws SQLException {
		return statement.executeQuery("select name from company");
	}

	// Methode qui permet d'afficher la liste des fabriquants

	public void showCompanies() throws SQLException {
		ResultSet resultSet = getCompanies();
		while (resultSet.next()) {
			System.out.println(resultSet.getString("name"));
		}
	}

	// Methode qui permet de renvoyer la liste des ordinateurs

	public ResultSet getComputers() throws SQLException {
		return statement.executeQuery("select id,name from computer");
	}

	// Methode qui permet d'afficher la liste des ordinateurs

	public void showComputers() throws SQLException {
		ResultSet resultSet = getComputers();
		while (resultSet.next()) {
			System.out.println("id: " + resultSet.getInt("id") + " name: " + resultSet.getString("name"));
		}
	}

	// Methode qui permet de renvoyer les details d'un ordinateur

	public ResultSet getDetails(int id) throws SQLException {
		return statement.executeQuery("select name,introduced,discontinued,company_id from computer where id = " + id);
	}

	// Methode qui permet d'afficher les details d'un ordinateur

	public void showDetails(int id) throws SQLException {
		ResultSet resultSet = getDetails(id);
		while (resultSet.next()) {
			System.out.println("name: " + resultSet.getString("name"));
			System.out.println("introduced: " + resultSet.getString("introduced"));
			System.out.println("discontinued: " + resultSet.getString("discontinued"));
			System.out.println("company_id: " + resultSet.getInt("company_id"));
		}
	}

	// Methode qui permet de modifier un ordinateur dans la bdd

	public void updateComputer(int id, String column, String newValue) throws SQLException {
		statement.execute("update computer set " + column + " = '" + newValue + "' where id = " + id);
	}

	// Methode qui permet de supprimer un ordinateur de la bdd

	public void deleteComputer(int id) throws SQLException {
		statement.execute("delete from computer where id = " + id);
		System.out.println("Nombre de suppression: " + statement.getUpdateCount());
	}

	// Methode qui permet de creer un ordinateur dans la bdd

	public void createComputer(String name, String introduced, String discontinued, String company_id)
			throws SQLException {
		String query = "insert into computer(name, introduced, discontinued, company_id) values ('" + name + "',";

		if (introduced.equals(""))
			query += "NULL,";
		else
			query += ("'" + introduced + "',");

		if (discontinued.equals(""))
			query += "NULL,";
		else
			query += ("'" + discontinued + "',");

		if (company_id.equals(""))
			query += "NULL)";
		else
			query += ("'" + company_id + "')");

		statement.execute(query);
		System.out.println("Nombre d'insertion: " + statement.getUpdateCount());
	}

}
