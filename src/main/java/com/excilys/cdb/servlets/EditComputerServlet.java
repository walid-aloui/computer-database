package com.excilys.cdb.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;

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

@Controller
@SuppressWarnings("serial")
@WebServlet("/editComputer")
public class EditComputerServlet extends HttpServlet {

	private static final String JSP_EDIT_COMPUTER = "/WEB-INF/views/editComputer.jsp";
	private static final String JSP_ERROR_500 = "/WEB-INF/views/500.jsp";
	
	private static final String ROUTE_DASHBOARD = "dashboard";
	
	private static final String ATTR_LIST_COMPANY = "listCompanies";
	private static final String ATTR_COMPANY_ID = "computerId";
	
	private static final String PARAM_ID = "id";
	private static final String PARAM_NAME = "computerName";
	private static final String PARAM_INTRODUCED = "introduced";
	private static final String PARAM_DISCONTINUED = "discontinued";
	private static final String PARAM_COMPANY_ID = "companyId";
	
	private ComputerService computerService;
	private CompanyService companyService;
	private MapperComputer mapperComputer;
	private MapperCompany mapperCompany;
	
	@Override
	public void init() throws ServletException {
		super.init();
		@SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(com.excilys.cdb.MainApp.class);
		computerService = context.getBean(ComputerService.class);
		companyService = context.getBean(CompanyService.class);
		mapperComputer = context.getBean(MapperComputer.class);
		mapperCompany = context.getBean(MapperCompany.class);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			updateCompaniesId(req);
			updateComputerId(req);
			this.getServletContext().getRequestDispatcher(JSP_EDIT_COMPUTER).forward(req, resp);
		} catch (OpenException | MapperException | ExecuteQueryException e) {
			this.getServletContext().getRequestDispatcher(JSP_ERROR_500).forward(req, resp);
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int computerId = Integer.parseInt(req.getParameter(PARAM_ID));
		ComputerDto computerDto = new ComputerDtoBuilder()
				.withId(computerId)
				.withName(req.getParameter(PARAM_NAME))
				.withIntroduced(req.getParameter(PARAM_INTRODUCED))
				.withDiscontinued(req.getParameter(PARAM_DISCONTINUED))
				.withCompanyId(req.getParameter(PARAM_COMPANY_ID))
				.build();
		try {
			Computer computer = mapperComputer.fromComputerDtoToComputer(computerDto);
			computerService.updateComputer(computer);
			resp.sendRedirect(ROUTE_DASHBOARD);
		} catch (MapperException e) {
			this.getServletContext().getRequestDispatcher(JSP_ERROR_500).forward(req, resp);
		}
	}

	private void updateCompaniesId(HttpServletRequest req)
			throws OpenException, MapperException, ExecuteQueryException {
		List<Company> listCompanies = companyService.selectAllCompanies();
		List<CompanyDto> listDtoCompanies = mapperCompany.fromCompanyListToCompanyDtoList(listCompanies);
		req.setAttribute(ATTR_LIST_COMPANY, listDtoCompanies);
	}
	
	private void updateComputerId(HttpServletRequest req) {
		req.setAttribute(ATTR_COMPANY_ID, req.getParameter(PARAM_ID));
	}

}
