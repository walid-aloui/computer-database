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

}
