package ui;

public enum ChoixMenu {
	LIST_COMPANIES(1), LIST_COMPUTERS(2), SHOW_DETAILS_COMPUTER(3), UPDATE_COMPUTER(4), INSERT_COMPUTER(5),
	DELETE_COMPUTER(6), QUIT(7);

	// Attributs

	private int number;

	// Constructeurs

	ChoixMenu(int number) {
		this.number = number;
	}

	// Getter

	public int getNumber() {
		return number;
	}

}
