package com.excilys.cdb.dtos;

public class CompanyDto {

	private String id;
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

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static class CompanyDtoBuilder {

		private String id;
		private String name;

		public CompanyDtoBuilder() {
			super();
		}

		public CompanyDtoBuilder withId(String id) {
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
