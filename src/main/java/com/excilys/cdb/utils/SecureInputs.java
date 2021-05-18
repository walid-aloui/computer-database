package com.excilys.cdb.utils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class SecureInputs {

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static LocalDate toDate(String s) {
		try {
			LocalDate date = LocalDate.parse(s);
			return date;
		} catch (DateTimeParseException e) {
			return null;
		} catch(NullPointerException e) {
			return null;
		}
	}

	public static boolean isValidPage(String choice, int numPage, int totalPage) {
		if (choice.equals("q") || (choice.equals("a") && numPage > 1) || (choice.equals("z") && numPage < totalPage))
			return true;
		return false;
	}

}