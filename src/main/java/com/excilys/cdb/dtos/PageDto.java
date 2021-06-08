package com.excilys.cdb.dtos;

import java.util.LinkedList;

public class PageDto {

	private static final int DEFAULT_NUM_ELEMENT = 25;
	private int numPage;
	private int totalPage;
	private int numElementPerPage;
	private int numElementTotal;
	private LinkedList<ComputerDto> contenue;

	public PageDto() {
		super();
	}

	private PageDto(PageDtoBuilder builder) {
		this.numPage = builder.numPage;
		this.totalPage = builder.totalPage;
		this.numElementPerPage = builder.numElementPerPage;
		this.contenue = builder.contenue;
		this.numElementTotal = builder.numElementTotal;
	}

	@Override
	public String toString() {
		return "PageDto [numPage=" + numPage + ", totalPage=" + totalPage + ", contenue=" + contenue + "]";
	}

	public int getNumPage() {
		return numPage;
	}

	public void setNumPage(int numPage) {
		this.numPage = numPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getNumElementPerPage() {
		return numElementPerPage;
	}

	public void setNumElementPerPage(int numElementPerPage) {
		this.numElementPerPage = numElementPerPage;
	}

	public int getNumElementTotal() {
		return numElementTotal;
	}

	public void setNumElementTotal(int numElementTotal) {
		this.numElementTotal = numElementTotal;
	}

	public LinkedList<ComputerDto> getContenue() {
		return contenue;
	}

	public void setContenue(LinkedList<ComputerDto> contenue) {
		this.contenue = contenue;
	}

	public static int getDefaultNumElement() {
		return DEFAULT_NUM_ELEMENT;
	}

	public static class PageDtoBuilder {

		private int numPage;
		private int totalPage;
		private int numElementPerPage;
		private int numElementTotal;
		private LinkedList<ComputerDto> contenue;

		public PageDtoBuilder() {
			super();
		}

		public PageDtoBuilder withNumPage(int numPage) {
			this.numPage = numPage;
			return this;
		}

		public PageDtoBuilder withTotalpage(int totalPage) {
			this.totalPage = totalPage;
			return this;
		}

		public PageDtoBuilder withNumElementPerPage(int numElementPerPage) {
			this.numElementPerPage = numElementPerPage;
			return this;
		}

		public PageDtoBuilder withNumElementTotal(int numElementTotal) {
			this.numElementTotal = numElementTotal;
			return this;
		}

		public PageDtoBuilder withContenue(LinkedList<ComputerDto> contenue) {
			this.contenue = contenue;
			return this;
		}

		public PageDto build() {
			return new PageDto(this);
		}
	}

}
