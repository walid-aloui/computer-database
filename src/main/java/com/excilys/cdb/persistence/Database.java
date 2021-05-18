package com.excilys.cdb.persistence;

public class Database {

	private static Database db;
	private final String url = "jdbc:mysql://localhost:3306/computer-database-db";
	private final String username = "admincdb";
	private final String password = "qwerty1234";

	public static Database getInstance() {
		if (db == null) {
			db = new Database();
		}
		return db;
	}

	private Database() {
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}