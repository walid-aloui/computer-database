package com.excilys.cdb.model;

import java.util.List;

import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;

public class Page {

	private static final int DEFAULT_NUM_ELEMENT = 25;
	private int numPage;
	private final int totalPage;
	private List<Computer> contenue;

	private Page(PageBuilder pageBuilder) throws OpenException, ExecuteQueryException, MapperException {
		this.numPage = pageBuilder.numPage;
		this.totalPage = pageBuilder.totalPage;
		this.contenue = pageBuilder.contenue;
	}

	@Override
	public String toString() {
		String res = "\n";
		for (Computer c : contenue) {
			res += (c.toString() + "\n");
		}
		res += ("\nPage " + numPage + "/" + totalPage + "\n");
		return res;
	}

	public static int getDefaultNumElement() {
		return DEFAULT_NUM_ELEMENT;
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

	public void setContenue(List<Computer> contenue) {
		this.contenue = contenue;
	}

	public static class PageBuilder {
		private int numPage;
		private int totalPage;
		private List<Computer> contenue;

		public PageBuilder() {
			super();
		}

		public PageBuilder withNumPage(int numPage) {
			this.numPage = numPage;
			return this;
		}

		public PageBuilder withTotalPage(int totalPage) {
			this.totalPage = totalPage;
			return this;
		}

		public PageBuilder withContenue(List<Computer> contenue) {
			this.contenue = contenue;
			return this;
		}

		public Page build() throws OpenException, ExecuteQueryException, MapperException {
			return new Page(this);
		}
	}

}
