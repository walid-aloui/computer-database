package ui;

import java.util.LinkedList;

import model.Company;
import model.Computer;
import model.Page;

public class ViewCli {

	// Methode qui permet d'afficher le menu

	void showMenu() {
		System.out.println("\nVeuillez selectionner l'option souhaite");
		System.out.println("1- Afficher la liste des fabriquants");
		System.out.println("2- Afficher la liste des ordinateurs");
		System.out.println("3- Afficher les details d'un ordinateur");
		System.out.println("4- Mettre a jour un ordinateur");
		System.out.println("5- Creer un ordinateur");
		System.out.println("6- Supprimer un ordinateur");
		System.out.println("7- Quitter\n");
	}

	// Methode qui permet d'afficher une liste de fabricant

	void showCompanies(LinkedList<Company> companies) {
		for (Company c : companies) {
			System.out.println(c);
		}
	}

	// Methode qui permet d'afficher une liste d'ordinateur

	void showComputers(LinkedList<Computer> computers) {
		for (Computer c : computers) {
			System.out.println(c);
		}
	}

	// Methode qui permet d'afficher une page

	public void showPage(Page p) {
		System.out.println(p);
	}

}
