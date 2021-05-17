package com.excilys.cdb.model;

public class Computer {

	// Attributs

	private int id;
	private String name;
	private String introduced;
	private String discontinued;
	private int companyId;

	// Constructeur

	public Computer(int id, String name, String introduced, String discontinued, int companyId) {
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.companyId = companyId;
	}

	// Methode toString

	@Override
	public String toString() {
		return "Computer [id=" + id + ", name=" + name + ", introduced=" + introduced + ", discontinued=" + discontinued
				+ ", companyId=" + companyId + "]";
	}

}