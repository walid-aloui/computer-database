package com.excilys.cdb.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SecureInputs {

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static Date toDate(String s) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return simpleDateFormat.parse(s);
		} catch (ParseException e) {
			return null;
		}
	}

	public static boolean isValidPage(String choice, int numPage, int totalPage) {
		if (choice.equals("q") || (choice.equals("a") && numPage > 1) || (choice.equals("z") && numPage < totalPage))
			return true;
		return false;
	}

}