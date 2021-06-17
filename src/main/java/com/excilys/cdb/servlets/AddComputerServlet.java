package com.excilys.cdb.servlets;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dtos.CompanyDto;
import com.excilys.cdb.dtos.ComputerDto;
import com.excilys.cdb.dtos.ComputerDto.ComputerDtoBuilder;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.mapper.MapperCompany;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

@Controller
@RequestMapping("/addComputer")
public class AddComputerServlet {

	private static final String ROUTE_ADD_COMPUTER = "addComputer";
	private static final String ROUTE_ERROR_500 = "500";
	
	private static final String ATTR_LIST_COMPANY = "listCompanies";

	private static final String PARAM_NAME = "computerName";
	private static final String PARAM_INTRODUCED = "introduced";
	private static final String PARAM_DISCONTINUED = "discontinued";
	private static final String PARAM_COMPANY_ID = "companyId";
	
	private ComputerService computerService;
	private CompanyService companyService;
	private MapperComputer mapperComputer;
	private MapperCompany mapperCompany;
	
	public AddComputerServlet(ComputerService computerService, CompanyService companyService,
			MapperComputer mapperComputer, MapperCompany mapperCompany) {
		this.computerService = computerService;
		this.companyService = companyService;
		this.mapperComputer = mapperComputer;
		this.mapperCompany = mapperCompany;
	}

	@GetMapping
	public ModelAndView doGet() {
		try {
			return updateCompaniesId();
		} catch (ExecuteQueryException e) {
			return new ModelAndView(ROUTE_ERROR_500);
		}
	}

	@PostMapping
	public ModelAndView doPost(@RequestParam(value = PARAM_NAME, required = true) String name,
							   @RequestParam(value = PARAM_INTRODUCED, required = false) String introduced,
							   @RequestParam(value = PARAM_DISCONTINUED, required = false) String discontinued,
							   @RequestParam(value = PARAM_COMPANY_ID, required = false) String companyId) {
		ComputerDto computerDto = new ComputerDtoBuilder()
				.withName(name)
				.withIntroduced(introduced)
				.withDiscontinued(discontinued)
				.withCompanyId(companyId)
				.build();
		try {
			Computer computer = mapperComputer.fromComputerDtoToComputer(computerDto);
			computerService.insertComputer(computer);
			return new ModelAndView(ROUTE_ADD_COMPUTER);
		} catch (MapperException e) {
			return new ModelAndView(ROUTE_ERROR_500);
		}
	}

	private ModelAndView updateCompaniesId() throws ExecuteQueryException {
		List<Company> listCompanies = companyService.selectAllCompanies();
		List<CompanyDto> listDtoCompanies = mapperCompany.fromCompanyListToCompanyDtoList(listCompanies);
		return new ModelAndView(ROUTE_ADD_COMPUTER).addObject(ATTR_LIST_COMPANY, listDtoCompanies);
	}

}
