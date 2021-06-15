package com.excilys.cdb.ui;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.utils.SecureInputs;

@Component
public class Cli {

	private Scanner sc;
	@Autowired
	private ControllerCli controllerCli;

	public Cli() {
		sc = new Scanner(System.in);
	}

	public void runCli() throws ExecuteQueryException, OpenException, MapperException {
		int opt;
		do {
			opt = askChoice();
			controllerCli.executeOption(opt);
		} while (opt != ChoixMenu.QUIT.getNumber());
	}

	private int askChoice() {
		controllerCli.getViewCli().showMenu();
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

	public void setSc(Scanner sc) {
		this.sc = sc;
	}

	public ControllerCli getControllerCli() {
		return controllerCli;
	}

}