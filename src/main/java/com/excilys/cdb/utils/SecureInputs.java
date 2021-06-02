package com.excilys.cdb.utils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class SecureInputs {

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static Optional<LocalDate> toLocalDate(String s) {
		try {
			LocalDate date = LocalDate.parse(s);
			return Optional.of(date);
		} catch (DateTimeParseException | NullPointerException e) {
			return Optional.empty();
		}
	}

	public static boolean isValidPage(String choice, int numPage, int totalPage) {
		return ("q".equals(choice) || 
				("a".equals(choice) && numPage > 1) || 
				("z".equals(choice) && numPage < totalPage));
	}

}