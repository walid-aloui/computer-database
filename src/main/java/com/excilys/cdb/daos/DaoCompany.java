package com.excilys.cdb.daos;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.mapper.MapperCompany;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.QCompany;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class DaoCompany {

	private static final Logger LOGGER = LoggerFactory.getLogger(DaoCompany.class);
	private static final QCompany qCompany = QCompany.company;

	private EntityManager entityManager;
	private JPAQueryFactory queryFactory;

	private MapperCompany mapperCompany;
	private DaoComputer daoComputer;

	public DaoCompany(MapperCompany mapperCompany, DaoComputer daoComputer, EntityManager entityManager) {
		this.mapperCompany = mapperCompany;
		this.daoComputer = daoComputer;
		this.entityManager = entityManager;
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	private JPAQuery<Tuple> buildSelectAll() {
		return queryFactory
				.select(qCompany.id, qCompany.name)
				.from(qCompany);
	}

	public List<Company> selectAllCompanies() {
		List<Tuple> tupleList = buildSelectAll().fetch();
		return mapperCompany.fromTupleListToCompanyList(tupleList);
	}

	public Optional<Company> selectCompanyById(int id) {
		Tuple tuple = buildSelectAll()
				.where(qCompany.id.eq(id))
				.fetchOne();

		if (tuple == null) {
			return Optional.empty();
		} else {
			return Optional.of(mapperCompany.fromTupleToCompany(tuple));
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