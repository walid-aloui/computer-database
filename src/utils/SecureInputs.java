package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import daos.DaoCompany;
import exception.InconsistentStateException;
import ui.ChoixMenu;

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

	// Methode qui permet de convertir une chaine de caractere en une date

	public static Date toDate(String s) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return simpleDateFormat.parse(s);
		} catch (ParseException e) {
			return null;
		}
	}

	// Methode qui renvoie true si l'id correspond a un fabriquant et false sinon

	public static boolean isCompany(int id) throws InconsistentStateException {
		return DaoCompany.create().getCompanyById(id) != null;
	}

	// Methode qui renvoie true si l'entier appartient a l'enum ChoixMenu

	public static boolean isChoixMenu(int n) {
		return (n >= ChoixMenu.LIST_COMPANIES.getNumber() && n <= ChoixMenu.QUIT.getNumber());
	}

	// Methode qui renvoie "NULL" si la chaine est vide et ajoute des ' sinon

	public static String addQuote(String s) {
		if (s.equals(""))
			return "NULL";
		return "'" + s + "'";
	}

}