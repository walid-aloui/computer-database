package com.excilys.cdb.dtos;

public class ComputerDto {

	private String id;
	private String name;
	private String introduced;
	private String discontinued;
	private String companyId;

	public ComputerDto() {
		super();
	}

	private ComputerDto(ComputerDtoBuilder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.introduced = builder.introduced;
		this.discontinued = builder.discontinued;
		this.companyId = builder.companyId;
	}

	@Override
	public String toString() {
		return "ComputerDto [id=" + id + ", name=" + name + ", introduced=" + introduced + ", discontinued="
				+ discontinued + ", companyId=" + companyId + "]";
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getIntroduced() {
		return introduced;
	}

	public String getDiscontinued() {
		return discontinued;
	}

	public String getCompanyId() {
		return companyId;
	}

	public static class ComputerDtoBuilder {

		private String id;
		private String name;
		private String introduced;
		private String discontinued;
		private String companyId;

		public ComputerDtoBuilder() {
			super();
		}

		public ComputerDtoBuilder withId(String id) {
			this.id = id;
			return this;
		}

		public ComputerDtoBuilder withName(String name) {
			this.name = name;
			return this;
		}

		public ComputerDtoBuilder withIntroduced(String introduced) {
			this.introduced = introduced;
			return this;
		}

		public ComputerDtoBuilder withDiscontinued(String discontinued) {
			this.discontinued = discontinued;
			return this;
		}

		public ComputerDtoBuilder withCompanyId(String companyId) {
			this.companyId = companyId;
			return this;
		}

		public ComputerDto build() {
			return new ComputerDto(this);
		}
	}

}
