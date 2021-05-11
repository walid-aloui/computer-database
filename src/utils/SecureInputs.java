package utils;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import daos.DaoCompany;
import exception.InconsistentStateException;

public class SecureInputs {

	// Methode qui renvoie true si la chaine est un entier false sinon

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// Methode qui renvoie true si la chaine est une date et false sinon

	public static boolean isDate(String s) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			simpleDateFormat.parse(s);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	// Methode qui renvoie true si l'id correspond a un fabriquant et false sinon

	public static boolean isCompany(int id) throws SQLException, InconsistentStateException {
		return DaoCompany.create().getCompanyById(id) != null;
	}

	// Methode qui renvoie "NULL" si la chaine est vide et ajoute des ' sinon

	public static String addQuote(String s) {
		if (s.equals(""))
			return "NULL";
		return "'" + s + "'";
	}

}