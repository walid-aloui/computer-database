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
	private DaoCompany daoCompany;

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
		daoCompany = DaoCompany.getInstance();
	}

	public LinkedList<Company> getAllCompanies() throws OpenException, MapperException, ExecuteQueryException {
		return daoCompany.getAllCompanies();
	}

	public Optional<Company> getCompanyById(int id) throws OpenException, MapperException, ExecuteQueryException {
		return daoCompany.getCompanyById(id);
	}

	public int deleteCompanyById(int id) throws OpenException, ExecuteQueryException {
		return daoCompany.deleteCompanyById(id);
	}

}
