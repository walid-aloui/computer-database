package com.excilys.cdb.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

	// Methode qui renvoie "NULL" si la chaine est vide et ajoute des ' sinon

	public static String addQuote(String s) {
		if (s.equals(""))
			return "NULL";
		return "'" + s + "'";
	}

	// Methode qui permet de verifier la saisie de la pagination

	public static boolean isValidPage(String choice, int numPage, int totalPage) {
		if  (choice.equals("q") || 
			(choice.equals("a") && numPage > 1) || 
			(choice.equals("z") && numPage < totalPage))
			return true;
		return false;
	}

}