package com.excilys.cdb.ui;

public enum ChoixMenu {
	LIST_COMPANIES(1), LIST_COMPUTERS(2), SHOW_DETAILS_COMPUTER(3), UPDATE_COMPUTER(4), INSERT_COMPUTER(5),
	DELETE_COMPUTER(6), QUIT(7);

	private int number;

	ChoixMenu(int number) {
		this.number = number;
	}

	public static boolean isChoixMenu(int n) {
		return (n >= LIST_COMPANIES.getNumber() && n <= QUIT.getNumber());
	}

	public int getNumber() {
		return number;
	}

}