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

@SuppressWarnings("serial")
@WebServlet("/dashboard")
public class ServletDashBoard extends HttpServlet {

	private static final String JSP_DASHBOARD = "/WEB-INF/jsp/dashboard.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int numComputers = 0;
		LinkedList<Computer> listComputers = null;
		try {
			numComputers = DaoComputer.getInstance().getNumberOfComputer();
			listComputers = DaoComputer.getInstance().getPartOfComputers(10, 0);
		} catch (OpenException | MapperException | ExecuteQueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		req.setAttribute("numComputers", numComputers);
		req.setAttribute("listComputers", listComputers);
		this.getServletContext().getRequestDispatcher(JSP_DASHBOARD).forward(req, resp);
	}

}
