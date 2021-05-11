package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

	// Attributs

	private static Database db;
	private Connection con;

	// Methode static qui renvoie l'instance unique de la bdd

	public static Database create() {
		if (db == null)
			db = new Database();
		return db;
	}

	// Constructeurs

	private Database() {
		String url = "jdbc:mysql://localhost:3306/computer-database-db";
		String username = "admincdb";
		String password = "qwerty1234";

		try {
			con = (Connection) DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connexion non etablit !");
		}
	}

	// Getter

	public Connection getCon() {
		return con;
	}

}