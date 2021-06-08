package com.excilys.cdb.servlets;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dtos.ComputerDto;
import com.excilys.cdb.dtos.PageDto;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.ComputerService;

@SuppressWarnings("serial")
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

	private static final String JSP_DASHBOARD = "/WEB-INF/views/dashboard.jsp";
	private static final String JSP_ERROR_500 = "/WEB-INF/views/500.jsp";

	private static final String NAME_SEARCH = "search";
	private static final String NUM_COMPUTER_PER_PAGE = "numComputerPerPage";
	private static final String NUM_PAGE = "page";

	private PageDto pageDto = new PageDto();

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
		System.out.println(req.getParameter("selection"));
		System.out.println("doPost !!!!!");
	}

	private void updateNumberComputer(HttpServletRequest req)
			throws OpenException, MapperException, ExecuteQueryException {
		int numComputers = ComputerService.getInstance().getNumberOfComputer();
		req.setAttribute("numComputers", numComputers);
	}

	private void updateListComputers(HttpServletRequest req)
			throws OpenException, MapperException, ExecuteQueryException {
		initNumPage(req);
		initNumElementPerPage(req);
		initTotalPage(req);
		initContenuePage(req);
		req.setAttribute("page", pageDto);
	}

	private void initNumPage(HttpServletRequest req) {
		String nPage = req.getParameter(NUM_PAGE);
		int numPage = (nPage == null) ? 1 : Integer.parseInt(nPage);
		pageDto.setNumPage(numPage);
	}

	private void initNumElementPerPage(HttpServletRequest req) {
		String nComputerPerPage = req.getParameter(NUM_COMPUTER_PER_PAGE);
		int numComputerPerPage = (nComputerPerPage == null || "".equals(nComputerPerPage)) ? Page.getDefaultNumElement()
				: Integer.parseInt(nComputerPerPage);
		pageDto.setNumElementPerPage(numComputerPerPage);
	}

	private void initTotalPage(HttpServletRequest req) {
		int numComputer = (int) req.getAttribute("numComputers");
		int numComputerPerPage = pageDto.getNumElementPerPage();
		int totalPage = (int) Math.ceil((double) numComputer / numComputerPerPage);
		pageDto.setTotalPage(totalPage);
	}

	private void initContenuePage(HttpServletRequest req) throws OpenException, MapperException, ExecuteQueryException {
		ComputerService computerService = ComputerService.getInstance();
		MapperComputer mapperComputer = MapperComputer.getInstance();
		int numPage = pageDto.getNumPage();
		int numComputerPerPage = pageDto.getNumElementPerPage();
		int offset = (numPage - 1) * numComputerPerPage;
		String search = req.getParameter(NAME_SEARCH);
		LinkedList<Computer> computers = null;

		if (search == null || "".equals(search)) {
			computers = computerService.getPartOfComputers(numComputerPerPage, offset);
		} else {
			computers = computerService.getPartOfComputersByName(search, numComputerPerPage, offset);
		}
		LinkedList<ComputerDto> listDtoComputers = mapperComputer.fromComputerListToComputerDtoList(computers);
		pageDto.setContenue(listDtoComputers);
	}

}
