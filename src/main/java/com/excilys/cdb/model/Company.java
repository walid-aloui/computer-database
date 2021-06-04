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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static class CompanyBuilder {
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