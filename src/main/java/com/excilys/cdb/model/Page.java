package com.excilys.cdb.model;

import java.util.LinkedList;

import com.excilys.cdb.daos.DaoComputer;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;

public class Page {

	private int numPage;
	private final int totalPage;
	private static final int MAX_ELEMENT = 10;
	private final LinkedList<Computer> contenue;

	private Page(PageBuilder pageBuilder) throws OpenException, ExecuteQueryException, MapperException {
		this.numPage = pageBuilder.numPage;
		this.totalPage = pageBuilder.totalPage;
		this.contenue = DaoComputer.getInstance().getPartOfComputers(MAX_ELEMENT, (numPage - 1) * 10);
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

	public static int getMAX_ELEMENT() {
		return MAX_ELEMENT;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public static class PageBuilder {
		private int numPage;
		private int totalPage;

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

		public Page build() throws OpenException, ExecuteQueryException, MapperException {
			return new Page(this);
		}
	}

}
