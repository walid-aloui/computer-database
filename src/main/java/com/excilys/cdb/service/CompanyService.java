package com.excilys.cdb.service;

import java.util.LinkedList;
import java.util.Optional;

import com.excilys.cdb.daos.DaoCompany;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.model.Company;

public class CompanyService {

	private static CompanyService companyService;

	public static CompanyService getInstance() {
		if (companyService == null) {
			companyService = new CompanyService();
		}
		return companyService;
	}

	public static void setCompanyService(CompanyService companyService) {
		CompanyService.companyService = companyService;
	}

	private CompanyService() {
		super();
	}

	public LinkedList<Company> getAllCompanies() throws OpenException, MapperException, ExecuteQueryException {
		return DaoCompany.getInstance().getAllCompanies();
	}

	public Optional<Company> getCompanyById(int id) throws OpenException, MapperException, ExecuteQueryException {
		return DaoCompany.getInstance().getCompanyById(id);
	}

}
