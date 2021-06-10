package com.excilys.cdb.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

	private static final String ROUTE_DASHBOARD = "dashboard";

	private static final String ATTR_PAGE = "page";

	private static final String PARAM_SEARCH = "search";
	private static final String PARAM_COMPUTER_PER_PAGE = "numComputerPerPage";
	private static final String PARAM_NUM_PAGE = "page";
	private static final String PARAM_SELECTION = "selection";
	private static final String PARAM_ORDER_BY = "orderBy";
	private static final String PARAM_MODE = "mode";

	private static final String KEY_NAME = "computerName";
	private static final String KEY_ORDER = "orderBy";
	private static final String KEY_MODE = "mode";
	private static final String KEY_LIMIT = "limit";
	private static final String KEY_OFFSET = "offset";

	private PageDto pageDto = new PageDto();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			updatePage(req);
			this.getServletContext().getRequestDispatcher(JSP_DASHBOARD).forward(req, resp);
		} catch (OpenException | MapperException | ExecuteQueryException e) {
			this.getServletContext().getRequestDispatcher(JSP_ERROR_500).forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ComputerService computerService = ComputerService.getInstance();
		List<String> selection = Arrays.asList(req.getParameter(PARAM_SELECTION).split(","));
		selection.stream().forEach(id -> {
			try {
				computerService.deleteComputerById(Integer.valueOf(id));
			} catch (NumberFormatException | OpenException | ExecuteQueryException e) {
				e.printStackTrace();
			}
		});
		resp.sendRedirect(ROUTE_DASHBOARD);
	}

	private void updatePage(HttpServletRequest req) throws OpenException, MapperException, ExecuteQueryException {
		initNumElementPage(req);
		initNumPage(req);
		initNumElementPerPage(req);
		initTotalPage(req);
		initContenuePage(req);
		req.setAttribute(ATTR_PAGE, pageDto);
	}

	private void initNumElementPage(HttpServletRequest req)
			throws OpenException, MapperException, ExecuteQueryException {
		int numComputers = ComputerService.getInstance().getNumberOfComputer();
		pageDto.setNumElementTotal(numComputers);
	}

	private void initNumPage(HttpServletRequest req) {
		String nPage = req.getParameter(PARAM_NUM_PAGE);
		int numPage = (nPage == null || "".equals(nPage)) ? 1 : Integer.parseInt(nPage);
		pageDto.setNumPage(numPage);
	}

	private void initNumElementPerPage(HttpServletRequest req) {
		String nComputerPerPage = req.getParameter(PARAM_COMPUTER_PER_PAGE);
		int numComputerPerPage = (nComputerPerPage == null || "".equals(nComputerPerPage)) ? Page.getDefaultNumElement()
				: Integer.parseInt(nComputerPerPage);
		pageDto.setNumElementPerPage(numComputerPerPage);
	}

	private void initTotalPage(HttpServletRequest req) {
		int numComputer = pageDto.getNumElementTotal();
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
		String search = req.getParameter(PARAM_SEARCH);
		String orderBy = req.getParameter(PARAM_ORDER_BY);
		String mode = req.getParameter(PARAM_MODE);

		Map<String, String> criteria = new HashMap<>();
		criteria.put(KEY_NAME, search);
		criteria.put(KEY_LIMIT, String.valueOf(numComputerPerPage));
		criteria.put(KEY_OFFSET, String.valueOf(offset));
		criteria.put(KEY_ORDER, orderBy);
		criteria.put(KEY_MODE, mode);
		LinkedList<Computer> computers = computerService.getComputersByCriteria(criteria);
		LinkedList<ComputerDto> listDtoComputers = mapperComputer.fromComputerListToComputerDtoList(computers);
		pageDto.setContenue(listDtoComputers);
	}

}
