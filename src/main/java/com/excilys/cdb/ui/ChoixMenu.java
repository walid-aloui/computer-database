package com.excilys.cdb.ui;

public enum ChoixMenu {
	DEFAULT(0), LIST_COMPANIES(1), LIST_COMPUTERS(2), SHOW_DETAILS_COMPUTER(3), UPDATE_COMPUTER(4), INSERT_COMPUTER(5),
	DELETE_COMPUTER(6), DELETE_COMPANY(7), QUIT(8);

	private int number;

	ChoixMenu(int number) {
		this.number = number;
	}

	public static boolean isChoixMenu(int n) {
		return (n >= LIST_COMPANIES.getNumber() && n <= QUIT.getNumber());
	}
	
	public static ChoixMenu toChoixMenu(int value) {
		for(ChoixMenu c : ChoixMenu.values()) {
			if(c.number == value) return c;
		}
		return DEFAULT;
	}

	public int getNumber() {
		return number;
	}

}