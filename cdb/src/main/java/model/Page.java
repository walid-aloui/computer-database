package model;

import java.sql.SQLException;
import java.util.LinkedList;

import daos.DaoComputer;

public class Page {

	// Attributs

	private int numPage;
	private final int totalPage;
	private static final int MAX_ELEMENT = 10;
	private final LinkedList<Computer> contenue;

	// Constructeur

	public Page(int numPage, int totalPage) throws SQLException {
		this.numPage = numPage;
		this.totalPage = totalPage;
		this.contenue = DaoComputer.getInstance().getPartOfComputers(MAX_ELEMENT, (numPage - 1) * 10);
	}

	// Methode toString

	@Override
	public String toString() {
		String res = "\n";
		for (Computer c : contenue) {
			res += (c.toString() + "\n");
		}
		res += ("\nPage " + numPage + "/" + totalPage + "\n");
		return res;
	}

	// Getter

	public static int getMAX_ELEMENT() {
		return MAX_ELEMENT;
	}

	public int getTotalPage() {
		return totalPage;
	}

}
