package com.excilys.cdb.webapp.servlets;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.excilys.cdb.binding.dtos.CompanyDto;
import com.excilys.cdb.binding.dtos.ComputerDto;
import com.excilys.cdb.binding.dtos.ComputerDto.ComputerDtoBuilder;
import com.excilys.cdb.binding.exception.MapperException;
import com.excilys.cdb.binding.mapper.MapperCompany;
import com.excilys.cdb.binding.mapper.MapperComputer;
import com.excilys.cdb.core.model.Company;
import com.excilys.cdb.core.model.Computer;
import com.excilys.cdb.persistence.exception.ExecuteQueryException;
import com.excilys.cdb.service.service.CompanyService;
import com.excilys.cdb.service.service.ComputerService;

@Controller
@RequestMapping("/editComputer")
public class EditComputerServlet {

	private static final String ROUTE_EDIT_COMPUTER = "editComputer";
	private static final String ROUTE_DASHBOARD = "dashboard";
	private static final String ROUTE_ERROR_500 = "500";
	
	private static final String ATTR_LIST_COMPANY = "listCompanies";
	private static final String ATTR_COMPUTER_ID = "computerId";
	
	private static final String PARAM_ID = "id";
	private static final String PARAM_NAME = "computerName";
	private static final String PARAM_INTRODUCED = "introduced";
	private static final String PARAM_DISCONTINUED = "discontinued";
	private static final String PARAM_COMPANY_ID = "companyId";
	
	private ComputerService computerService;
	private CompanyService companyService;
	private MapperComputer mapperComputer;
	private MapperCompany mapperCompany;
	
	public EditComputerServlet(ComputerService computerService, CompanyService companyService,
			MapperComputer mapperComputer, MapperCompany mapperCompany) {
		super();
		this.computerService = computerService;
		this.companyService = companyService;
		this.mapperComputer = mapperComputer;
		this.mapperCompany = mapperCompany;
	}

	@GetMapping
	public ModelAndView doGet(@RequestParam(value = PARAM_ID) int id) {
		try {
			ModelAndView modelAndView = new ModelAndView(ROUTE_EDIT_COMPUTER);
			modelAndView = updateCompaniesId(modelAndView);
			modelAndView = updateComputerId(id, modelAndView);
			return modelAndView;
		} catch (ExecuteQueryException e) {
			return new ModelAndView(ROUTE_ERROR_500);
		}
	}
	
	@GetMapping("/error")
	public ModelAndView getError500() {
		return new ModelAndView(ROUTE_ERROR_500);
	}

	@PostMapping
	public RedirectView doPost(@RequestParam(value = PARAM_ID, required = true) int id,
					   @RequestParam(value = PARAM_NAME, required = true) String name,
					   @RequestParam(value = PARAM_INTRODUCED, required = false) String introduced,
					   @RequestParam(value = PARAM_DISCONTINUED, required = false) String discontinued,
					   @RequestParam(value = PARAM_COMPANY_ID, required = false) String companyId) {
		ComputerDto computerDto = new ComputerDtoBuilder()
				.withId(id)
				.withName(name)
				.withIntroduced(introduced)
				.withDiscontinued(discontinued)
				.withCompanyId(companyId)
				.build();
		try {
			Computer computer = mapperComputer.fromComputerDtoToComputer(computerDto);
			computerService.updateComputer(computer);
			return new RedirectView(ROUTE_DASHBOARD);
		} catch (MapperException e) {
			return new RedirectView(ROUTE_EDIT_COMPUTER + "/error");
		}
	}

	private ModelAndView updateCompaniesId(ModelAndView modelAndView) throws ExecuteQueryException {
		List<Company> listCompanies = companyService.selectAllCompanies();
		List<CompanyDto> listDtoCompanies = mapperCompany.fromCompanyListToCompanyDtoList(listCompanies);
		return modelAndView.addObject(ATTR_LIST_COMPANY, listDtoCompanies);
	}
	
	private ModelAndView updateComputerId(int id, ModelAndView modelAndView) {
		return modelAndView.addObject(ATTR_COMPUTER_ID, id);
	}

}
