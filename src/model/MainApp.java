package model;

import java.sql.SQLException;

public class MainApp {

	public static void main(String[] args) {
		try {
			Database db = Database.create();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
