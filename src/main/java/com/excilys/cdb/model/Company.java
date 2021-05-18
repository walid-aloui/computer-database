package com.excilys.cdb.model;

public class Company {

	private int id;
	private String name;

	public Company(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + "]";
	}

	public int getId() {
		return id;
	}

}