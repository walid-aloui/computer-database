package com.excilys.cdb.webapp.servlets;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.excilys.cdb.binding.dtos.ComputerDto;
import com.excilys.cdb.binding.dtos.PageDto;
import com.excilys.cdb.binding.mapper.MapperComputer;
import com.excilys.cdb.core.model.Computer;
import com.excilys.cdb.persistence.exception.ExecuteQueryException;
import com.excilys.cdb.service.service.ComputerService;

@Controller
@RequestMapping("/dashboard")
public class DashboardServlet {

	private static final String ROUTE_DASHBOARD = "dashboard";
	private static final String ROUTE_ERROR_500 = "500";

	private static final String ATTR_PAGE = "page";

	private static final String PARAM_SEARCH = "search";
	private static final String PARAM_LIMIT = "limit";
	private static final String PARAM_NUM_PAGE = "page";
	private static final String PARAM_SELECTION = "selection";
	private static final String PARAM_ORDER_BY = "orderBy";
	private static final String PARAM_MODE = "mode";

	private static final String KEY_SEARCH = "search";
	private static final String KEY_ORDER = "orderBy";
	private static final String KEY_MODE = "mode";
	private static final String KEY_LIMIT = "limit";
	private static final String KEY_OFFSET = "offset";

	private PageDto pageDto;
	private ComputerService computerService;
	private MapperComputer mapperComputer;
	
	public DashboardServlet(ComputerService computerService, MapperComputer mapperComputer) {
		this.pageDto = new PageDto();
		this.computerService = computerService;
		this.mapperComputer = mapperComputer;
	}
	
	@GetMapping
	public ModelAndView doGet(@RequestParam(value = PARAM_SEARCH, required = false) String search,
					  @RequestParam(value = PARAM_LIMIT, required = false) Integer limit,
					  @RequestParam(value = PARAM_NUM_PAGE, required = false) Integer numPage,
					  @RequestParam(value = PARAM_ORDER_BY, required = false) String order,
					  @RequestParam(value =  PARAM_MODE, required = false) String mode) {
		try {
			initPage(search, limit, numPage, order, mode);
			return new ModelAndView(ROUTE_DASHBOARD)
					.addObject(ATTR_PAGE, pageDto);
		} catch (ExecuteQueryException e) {
			return new ModelAndView(ROUTE_ERROR_500);
		}
	}

	@PostMapping
	public RedirectView doPost(@RequestParam(value = PARAM_SELECTION) String selection) {
		List<String> select = Arrays.asList(selection.split(","));
		select.stream().forEach(id -> {
			try {
				computerService.deleteComputerById(Integer.valueOf(id));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		});
		return new RedirectView(ROUTE_DASHBOARD);
	}

	private void initPage(String search, Integer limit, Integer numPage, String order, String mode) throws ExecuteQueryException {
		initNumElementPage(search);
		initNumPage(numPage);
		initNumElementPerPage(limit);
		initTotalPage();
		initContenuePage(search, order, mode);
	}

	private void initNumElementPage(String search) throws ExecuteQueryException {
		long numComputers = computerService.selectNumberOfComputerBySearch(search);
		pageDto.setNumElementTotal(numComputers);
	}

	private void initNumPage(Integer numPage) {
		int nPage = (numPage == null) ? 1 : numPage;
		pageDto.setNumPage(nPage);
	}

	private void initNumElementPerPage(Integer limit) {
		int lim = (limit == null) ? PageDto.getDefaultNumElement() : limit;
		pageDto.setNumElementPerPage(lim);
	}

	private void initTotalPage() {
		long numComputer = pageDto.getNumElementTotal();
		int limit = pageDto.getNumElementPerPage();
		int totalPage = (int) Math.ceil((double) numComputer / limit);
		pageDto.setTotalPage(totalPage);
	}

	private void initContenuePage(String search, String orderBy, String mode) throws ExecuteQueryException {
		int numPage = pageDto.getNumPage();
		int limit = pageDto.getNumElementPerPage();
		int offset = (numPage - 1) * limit;

		Map<String, String> criteria = new HashMap<>();
		criteria.put(KEY_SEARCH, search);
		criteria.put(KEY_LIMIT, String.valueOf(limit));
		criteria.put(KEY_OFFSET, String.valueOf(offset));
		criteria.put(KEY_ORDER, orderBy);
		criteria.put(KEY_MODE, mode);
		List<Computer> computers = computerService.selectComputersByCriteria(criteria);
		List<ComputerDto> listDtoComputers = mapperComputer.fromComputerListToComputerDtoList(computers);
		pageDto.setContenue(listDtoComputers);
	}

}
