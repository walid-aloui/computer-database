package com.excilys.cdb.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.daos.DaoCompany;
import com.excilys.cdb.daos.DaoComputer;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.utils.SecureInputs;

@SuppressWarnings("serial")
@WebServlet("/addComputer")
public class AddComputerServlet extends HttpServlet {

	private static final String JSP_ADD_COMPUTER = "/WEB-INF/jsp/addComputer.jsp";
	private static final String JSP_ERROR_500 = "/WEB-INF/jsp/500.jsp";

	private static final String ROUTE_ADD_COMPUTER = "addComputer";

	private static final String FIELD_NAME = "computerName";
	private static final String FIELD_INTRODUCED = "introduced";
	private static final String FIELD_DISCONTINUED = "discontinued";
	private static final String FIELD_COMPANY_ID = "companyId";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			updateCompaniesId(req);
			this.getServletContext().getRequestDispatcher(JSP_ADD_COMPUTER).forward(req, resp);
		} catch (OpenException | MapperException | ExecuteQueryException e) {
			this.getServletContext().getRequestDispatcher(JSP_ERROR_500).forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = getFieldComputerName(req);
		Optional<LocalDate> introduced = getFieldIntroduced(req);
		Optional<LocalDate> discontinued = getFieldDiscontinued(req);
		Optional<String> companyId = getFieldCompanyId(req);
		try {
			DaoComputer.getInstance().insertComputer(name, introduced, discontinued, companyId);
			resp.sendRedirect(ROUTE_ADD_COMPUTER);
		} catch (OpenException e) {
			this.getServletContext().getRequestDispatcher(JSP_ERROR_500).forward(req, resp);
		}
	}

	private void updateCompaniesId(HttpServletRequest req)
			throws OpenException, MapperException, ExecuteQueryException {
		LinkedList<Company> listCompanies = DaoCompany.getInstance().getAllCompanies();
		req.setAttribute("listCompanies", listCompanies);
	}

	private String getFieldComputerName(HttpServletRequest req) {
		return req.getParameter(FIELD_NAME);
	}

	private Optional<LocalDate> getFieldIntroduced(HttpServletRequest req) {
		return SecureInputs.toLocalDate(req.getParameter(FIELD_INTRODUCED));
	}

	private Optional<LocalDate> getFieldDiscontinued(HttpServletRequest req) {
		return SecureInputs.toLocalDate(req.getParameter(FIELD_DISCONTINUED));
	}

	private Optional<String> getFieldCompanyId(HttpServletRequest req) {
		String companyId = req.getParameter(FIELD_COMPANY_ID);
		return "0".equals(companyId) ? Optional.empty() : Optional.of(companyId);
	}

}
