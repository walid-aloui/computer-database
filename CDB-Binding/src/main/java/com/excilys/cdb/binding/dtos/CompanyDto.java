package com.excilys.cdb.binding.dtos;

public class CompanyDto {

	private int id;
	private String name;

	public CompanyDto() {
		super();
	}

	private CompanyDto(CompanyDtoBuilder builder) {
		this.id = builder.id;
		this.name = builder.name;
	}

	@Override
	public String toString() {
		return "CompanyDto [id=" + id + ", name=" + name + "]";
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static class CompanyDtoBuilder {

		private int id;
		private String name;

		public CompanyDtoBuilder() {
			super();
		}

		public CompanyDtoBuilder withId(int id) {
			this.id = id;
			return this;
		}

		public CompanyDtoBuilder withName(String name) {
			this.name = name;
			return this;
		}

		public CompanyDto build() {
			return new CompanyDto(this);
		}
	}

}
