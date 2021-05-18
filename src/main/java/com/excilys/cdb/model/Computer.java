package com.excilys.cdb.model;

import java.time.LocalDate;

public class Computer {

	private int id;
	private String name;
	private LocalDate introduced;
	private LocalDate discontinued;
	private Company company;

	public Computer(int id, String name, LocalDate introduced, LocalDate discontinued, Company company) {
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.company = company;
	}

	@Override
	public String toString() {
		return "Computer [id=" + id + ", name=" + name + ", introduced=" + introduced + ", discontinued=" + discontinued
				+ ", companyId=" + company.getId() + "]";
	}

}