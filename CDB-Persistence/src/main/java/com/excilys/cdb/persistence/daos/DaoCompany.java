package com.excilys.cdb.persistence.daos;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.core.model.Company;
import com.excilys.cdb.core.model.QCompany;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class DaoCompany {

	private static final Logger LOGGER = LoggerFactory.getLogger(DaoCompany.class);
	private static final QCompany qCompany = QCompany.company;

	private EntityManager entityManager;
	private JPAQueryFactory queryFactory;

	private DaoComputer daoComputer;

	public DaoCompany(DaoComputer daoComputer, EntityManager entityManager) {
		this.daoComputer = daoComputer;
		this.entityManager = entityManager;
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	private JPAQuery<Company> buildSelectAll() {
		return queryFactory.selectFrom(qCompany);
	}

	public List<Company> selectAllCompanies() {
		return buildSelectAll().fetch();
	}

	public Optional<Company> selectCompanyById(int id) {
		Company company = buildSelectAll()
				.where(qCompany.id.eq(id))
				.fetchOne();

		if (company == null) {
			return Optional.empty();
		} else {
			return Optional.of(company);
		}
	}

	public long deleteCompanyById(int id) {
		daoComputer.deleteComputersByCompanyId(id);
		entityManager.getTransaction().begin();
		long numDelete = queryFactory
				.delete(qCompany)
				.where(qCompany.id.eq(id))
				.execute();
		entityManager.getTransaction().commit();
		return numDelete;
	}

}