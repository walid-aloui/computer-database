package com.excilys.cdb.servlets;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dtos.CompanyDto;
import com.excilys.cdb.dtos.ComputerDto;
import com.excilys.cdb.dtos.ComputerDto.ComputerDtoBuilder;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.mapper.MapperCompany;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;


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
		ComputerService computerService = ComputerService.getInstance();
		MapperComputer mapperComputer = MapperComputer.getInstance();
		ComputerDto computerDto = new ComputerDtoBuilder()
				.withName(req.getParameter(FIELD_NAME))
				.withIntroduced(req.getParameter(FIELD_INTRODUCED))
				.withDiscontinued(req.getParameter(FIELD_DISCONTINUED))
				.withCompanyId(req.getParameter(FIELD_COMPANY_ID))
				.build();
		try {
			Computer computer = mapperComputer.fromComputerDtoToComputer(computerDto);
			computerService.insertComputer(computer);
			resp.sendRedirect(ROUTE_ADD_COMPUTER);
		} catch (MapperException | OpenException e) {
			this.getServletContext().getRequestDispatcher(JSP_ERROR_500).forward(req, resp);
		}
	}

	private void updateCompaniesId(HttpServletRequest req)
			throws OpenException, MapperException, ExecuteQueryException {
		CompanyService companyService = CompanyService.getInstance();
		MapperCompany mapperCompany = MapperCompany.getInstance();
		LinkedList<Company> listCompanies = companyService.getAllCompanies();
		LinkedList<CompanyDto> listDtoCompanies = mapperCompany.fromCompanyListToCompanyDtoList(listCompanies);
		req.setAttribute("listCompanies", listDtoCompanies);
	}

}
