package com.excilys.cdb.service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.excilys.cdb.core.model.Company;
import com.excilys.cdb.persistence.daos.DaoCompany;

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
