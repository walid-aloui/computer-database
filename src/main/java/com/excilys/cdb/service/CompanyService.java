package com.excilys.cdb.service;

import java.util.LinkedList;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.excilys.cdb.daos.DaoCompany;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.model.Company;

@Service
public class CompanyService {

	private DaoCompany daoCompany;

	public CompanyService(DaoCompany daoCompany) {
		this.daoCompany = daoCompany;
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
