package com.excilys.cdb.console.ui;

import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.excilys.cdb.binding.utils.SecureInputs;

@Component
public class Cli {

	private Scanner sc;
	private ControllerCli controllerCli;
	
	public Cli(ControllerCli controllerCli) {
		this.controllerCli = controllerCli;
		sc = new Scanner(System.in);
	}

	public void runCli() {
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