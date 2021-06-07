package com.excilys.cdb.ui;

import java.util.LinkedList;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;

public class ViewCli {

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

	void showCompanies(LinkedList<Company> companies) {
		for (Company c : companies) {
			System.out.println(c);
		}
	}

	public void showPage(Page p) {
		System.out.println(p);
	}

}
