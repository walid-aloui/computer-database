package com.excilys.cdb.servlets;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.daos.DaoComputer;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.model.Page.PageBuilder;

@SuppressWarnings("serial")
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

	private static final String JSP_DASHBOARD = "/WEB-INF/jsp/dashboard.jsp";
	private static final String JSP_ERROR_500 = "/WEB-INF/jsp/500.jsp";

	private static final String ROUTE_DASHBOARD = "dashboard";

	private static final String NAME_SEARCH = "search";
	private static final String NUM_COMPUTER_PAGE = "numComputerPage";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			updateNumberComputer(req);
			updateListComputers(req);
			this.getServletContext().getRequestDispatcher(JSP_DASHBOARD).forward(req, resp);
		} catch (OpenException | MapperException | ExecuteQueryException e) {
			this.getServletContext().getRequestDispatcher(JSP_ERROR_500).forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("On est la");
	}

	private void updateNumberComputer(HttpServletRequest req)
			throws OpenException, MapperException, ExecuteQueryException {
		int numComputers = DaoComputer.getInstance().getNumberOfComputer();
		req.setAttribute("numComputers", numComputers);
	}

	private void updateListComputers(HttpServletRequest req)
			throws OpenException, MapperException, ExecuteQueryException {
		String search = req.getParameter(NAME_SEARCH);
		String numComputerPage = req.getParameter(NUM_COMPUTER_PAGE);
		int n = (numComputerPage == null) ? Page.getDefaultNumElement() : Integer.parseInt(numComputerPage);
		LinkedList<Computer> listComputers = null;
		if (search == null || "".equals(search)) {
			listComputers = DaoComputer.getInstance().getPartOfComputers(n, 500);
		} else {
			listComputers = DaoComputer.getInstance().getComputerByName(search);
		}
		req.setAttribute("listComputers", listComputers);
	}

}
