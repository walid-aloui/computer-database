package com.excilys.cdb.model;

public class Company {

	// Attributs

	private int id;
	private String name;

	// Constructeur

	public Company(int id, String name) {
		this.id = id;
		this.name = name;
	}

	// Methode toString

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + "]";
	}

}