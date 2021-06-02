package com.excilys.cdb.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.daos.DaoComputer;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.utils.SecureInputs;

@SuppressWarnings("serial")
@WebServlet("/addComputer")
public class AddComputerServlet extends HttpServlet {

	private static final String JSP_ADD_COMPUTER = "/WEB-INF/jsp/addComputer.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher(JSP_ADD_COMPUTER).forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = getFieldComputerName(req);
		Optional<LocalDate> introduced = getFieldIntroduced(req);
		Optional<LocalDate> discontinued = getFieldDiscontinued(req);
		Optional<String> companyId = getFieldCompanyId(req);
		try {
			DaoComputer.getInstance().insertComputer(name, introduced, discontinued, companyId);
		} catch (OpenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getServletContext().getRequestDispatcher(JSP_ADD_COMPUTER).forward(req, resp);
	}

	private String getFieldComputerName(HttpServletRequest req) {
		return req.getParameter("computerName");
	}

	private Optional<LocalDate> getFieldIntroduced(HttpServletRequest req) {
		return SecureInputs.toLocalDate(req.getParameter("introduced"));
	}

	private Optional<LocalDate> getFieldDiscontinued(HttpServletRequest req) {
		return SecureInputs.toLocalDate(req.getParameter("discontinued"));
	}

	private Optional<String> getFieldCompanyId(HttpServletRequest req) {
		String companyId = req.getParameter("companyId");
		return "0".equals(companyId) ? Optional.empty() : Optional.of(companyId);
	}

}
