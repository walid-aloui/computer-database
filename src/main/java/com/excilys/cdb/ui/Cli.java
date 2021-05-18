package com.excilys.cdb.ui;

import java.util.Scanner;

import com.excilys.cdb.exception.CloseException;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.utils.SecureInputs;

public class Cli {

	private ControllerCli controllerCli;
	private Scanner sc;

	public Cli() {
		sc = new Scanner(System.in);
		this.controllerCli = new ControllerCli(sc);
	}

	public void runCli() throws OpenException, ExecuteQueryException, MapperException, CloseException {
		int opt;
		do {
			opt = askChoice();
			controllerCli.executeOption(opt);
		} while (opt != ChoixMenu.QUIT.getNumber());
	}

	private int askChoice() {
		controllerCli.getView().showMenu();
		String input = sc.nextLine();
		if (!SecureInputs.isInteger(input)) {
			System.out.println("Veuillez entrez un chiffre !");
			return askChoice();
		}
		int opt = Integer.parseInt(input);
		if (!ChoixMenu.isChoixMenu(opt)) {
			System.out.println("Option invalide !");
			return askChoice();
		}
		return opt;
	}

}