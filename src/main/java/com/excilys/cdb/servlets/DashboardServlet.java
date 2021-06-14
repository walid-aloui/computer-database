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

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;

import com.excilys.cdb.dtos.ComputerDto;
import com.excilys.cdb.dtos.PageDto;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.ComputerService;

@Controller
@SuppressWarnings("serial")
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

	private static final String JSP_DASHBOARD = "/WEB-INF/views/dashboard.jsp";
	private static final String JSP_ERROR_500 = "/WEB-INF/views/500.jsp";

	private static final String ROUTE_DASHBOARD = "dashboard";

	private static final String ATTR_PAGE = "page";

	private static final String PARAM_SEARCH = "search";
	private static final String PARAM_LIMIT = "limit";
	private static final String PARAM_NUM_PAGE = "page";
	private static final String PARAM_SELECTION = "selection";
	private static final String PARAM_ORDER_BY = "orderBy";
	private static final String PARAM_MODE = "mode";

	private static final String KEY_NAME = "computerName";
	private static final String KEY_ORDER = "orderBy";
	private static final String KEY_MODE = "mode";
	private static final String KEY_LIMIT = "limit";
	private static final String KEY_OFFSET = "offset";

	private PageDto pageDto;
	private ComputerService computerService;
	private MapperComputer mapperComputer;
	
	@Override
	public void init() throws ServletException {
		super.init();
		this.pageDto = new PageDto();
		@SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(com.excilys.cdb.MainApp.class);
		computerService = context.getBean(ComputerService.class);
		mapperComputer = context.getBean(MapperComputer.class);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			initPage(req);
			this.getServletContext().getRequestDispatcher(JSP_DASHBOARD).forward(req, resp);
		} catch (OpenException | MapperException | ExecuteQueryException e) {
			this.getServletContext().getRequestDispatcher(JSP_ERROR_500).forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

	private void initPage(HttpServletRequest req) throws OpenException, MapperException, ExecuteQueryException {
		initNumElementPage(req);
		initNumPage(req);
		initNumElementPerPage(req);
		initTotalPage(req);
		initContenuePage(req);
		req.setAttribute(ATTR_PAGE, pageDto);
	}

	private void initNumElementPage(HttpServletRequest req)
			throws OpenException, MapperException, ExecuteQueryException {
		int numComputers = computerService.getNumberOfComputer();
		pageDto.setNumElementTotal(numComputers);
	}

	private void initNumPage(HttpServletRequest req) {
		String numPageParam = req.getParameter(PARAM_NUM_PAGE);
		int numPage = (numPageParam == null || "".equals(numPageParam)) ? 1 : Integer.parseInt(numPageParam);
		pageDto.setNumPage(numPage);
	}

	private void initNumElementPerPage(HttpServletRequest req) {
		String limitParam = req.getParameter(PARAM_LIMIT);
		int limit = (limitParam == null || "".equals(limitParam)) ? Page.getDefaultNumElement()
				: Integer.parseInt(limitParam);
		pageDto.setNumElementPerPage(limit);
	}

	private void initTotalPage(HttpServletRequest req) {
		int numComputer = pageDto.getNumElementTotal();
		int limit = pageDto.getNumElementPerPage();
		int totalPage = (int) Math.ceil((double) numComputer / limit);
		pageDto.setTotalPage(totalPage);
	}

	private void initContenuePage(HttpServletRequest req) throws OpenException, MapperException, ExecuteQueryException {
		int numPage = pageDto.getNumPage();
		int limit = pageDto.getNumElementPerPage();
		int offset = (numPage - 1) * limit;
		String search = req.getParameter(PARAM_SEARCH);
		String orderBy = req.getParameter(PARAM_ORDER_BY);
		String mode = req.getParameter(PARAM_MODE);

		Map<String, String> criteria = new HashMap<>();
		criteria.put(KEY_NAME, search);
		criteria.put(KEY_LIMIT, String.valueOf(limit));
		criteria.put(KEY_OFFSET, String.valueOf(offset));
		criteria.put(KEY_ORDER, orderBy);
		criteria.put(KEY_MODE, mode);
		LinkedList<Computer> computers = computerService.getComputersByCriteria(criteria);
		LinkedList<ComputerDto> listDtoComputers = mapperComputer.fromComputerListToComputerDtoList(computers);
		pageDto.setContenue(listDtoComputers);
	}

}
