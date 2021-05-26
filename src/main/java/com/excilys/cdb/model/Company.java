package com.excilys.cdb.model;

public class Company {

	private int id;
	private String name;

	private Company(CompanyBuilder companyBuilder) {
		this.id = companyBuilder.id;
		this.name = companyBuilder.name;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + "]";
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public static class CompanyBuilder{
		private int id;
		private String name;
		
		public CompanyBuilder() {
			super();
		}
		
		public CompanyBuilder withId(int id) {
			this.id = id;
			return this;
		}
		
		public CompanyBuilder withName(String name) {
			this.name = name;
			return this;
		}
		
		public Company build() {
			return new Company(this);
		}
	}

}