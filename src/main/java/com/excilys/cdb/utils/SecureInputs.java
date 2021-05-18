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
		} catch (DateTimeParseException e) {
			return Optional.empty();
		} catch (NullPointerException e) {
			return Optional.empty();
		}
	}

	public static boolean isValidPage(String choice, int numPage, int totalPage) {
		if (choice.equals("q") || (choice.equals("a") && numPage > 1) || (choice.equals("z") && numPage < totalPage)) {
			return true;
		}
		return false;
	}

}