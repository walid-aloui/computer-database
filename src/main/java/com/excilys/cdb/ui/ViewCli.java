package com.excilys.cdb.ui;

import java.util.List;

import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;

@Component
public class ViewCli {

	public ViewCli() {
		super();
	}

	void showMenu() {
		System.out.println("\nVeuillez selectionner l'option souhaite");
		System.out.println("1- Afficher la liste des fabriquants");
		System.out.println("2- Afficher la liste des ordinateurs");
		System.out.println("3- Afficher les details d'un ordinateur");
		System.out.println("4- Mettre a jour un ordinateur");
		System.out.println("5- Creer un ordinateur");
		System.out.println("6- Supprimer un ordinateur");
		System.out.println("7- Supprimer un fabriquant");
		System.out.println("8- Quitter\n");
	}

	void showCompanies(List<Company> companies) {
		companies.forEach(company -> System.out.println(company));
	}

	public void showPage(Page p) {
		System.out.println(p);
	}

}
