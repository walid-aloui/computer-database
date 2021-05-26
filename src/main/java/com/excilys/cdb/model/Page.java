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

	public Page(int numPage, int totalPage) throws OpenException, ExecuteQueryException, MapperException {
		this.numPage = numPage;
		this.totalPage = totalPage;
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

}
