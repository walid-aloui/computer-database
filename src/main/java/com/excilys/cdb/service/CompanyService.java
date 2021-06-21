package com.excilys.cdb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.excilys.cdb.daos.DaoCompany;
import com.excilys.cdb.model.Company;

@Service
public class CompanyService {

	private DaoCompany daoCompany;

	public CompanyService(DaoCompany daoCompany) {
		this.daoCompany = daoCompany;
	}

	public List<Company> selectAllCompanies() {
		return daoCompany.selectAllCompanies();
	}

	public Optional<Company> selectCompanyById(int id) {
		return daoCompany.selectCompanyById(id);
	}

	public long deleteCompanyById(int id) {
		return daoCompany.deleteCompanyById(id);
	}

}
