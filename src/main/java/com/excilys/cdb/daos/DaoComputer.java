package com.excilys.cdb.daos;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.QCompany;
import com.excilys.cdb.model.QComputer;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class DaoComputer {

	private static final String KEY_SEARCH = "search";
	private static final String KEY_ORDER = "orderBy";
	private static final String KEY_MODE = "mode";
	private static final String KEY_LIMIT = "limit";
	private static final String KEY_OFFSET = "offset";

	private static final Logger LOGGER = LoggerFactory.getLogger(DaoComputer.class);
	private static final QComputer qComputer = QComputer.computer;
	private static final QCompany qCompany = QCompany.company;

	private EntityManager entityManager;
	private JPAQueryFactory queryFactory;

	private MapperComputer mapperComputer;

	public DaoComputer(MapperComputer mapperComputer, EntityManager entityManager) {
		this.mapperComputer = mapperComputer;
		this.entityManager = entityManager;
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	private JPAQuery<Tuple> buildSelectAll() {
		return queryFactory
				.select(qComputer.id, 
						qComputer.name, 
						qComputer.introduced, 
						qComputer.discontinued, 
						qComputer.company,
						qCompany.id, 
						qCompany.name)
				.from(qComputer)
				.leftJoin(qComputer.company, qCompany)
				.on(qComputer.company.eq(qCompany));
	}

	public List<Computer> selectAllComputers() {
		List<Tuple> tupleList = buildSelectAll().fetch();
		return mapperComputer.fromTupleListToComputerList(tupleList);
	}

	public Optional<Computer> selectComputerById(int id) {
		Tuple tuple = buildSelectAll()
				.where(qComputer.id.eq(id))
				.fetchOne();

		if (tuple == null) {
			return Optional.empty();
		} else {
			return Optional.of(mapperComputer.fromTupleToComputer(tuple));
		}
	}

	public List<Computer> selectPartOfComputers(long limit, int offset) {
		List<Tuple> tupleList = buildSelectAll()
				.limit(limit)
				.offset(offset)
				.fetch();
		return mapperComputer.fromTupleListToComputerList(tupleList);
	}

	public List<Computer> selectPartOfComputersBySearch(String search, int limit, int offset) {
		List<Tuple> tupleList = buildSelectAll()
				.where(qComputer.name.like("%" + search + "%")
						.or(qCompany.name.like("%" + search + "%")))
				.limit(limit)
				.offset(offset)
				.fetch();
		return mapperComputer.fromTupleListToComputerList(tupleList);
	}

	public List<Computer> selectComputersByCriteria(Map<String, String> criteria) {
		JPAQuery<Tuple> query = buildSelectAll();
		query = addLike(query, criteria);
		query = addOrderBy(query, criteria);
		query = addLimit(query, criteria);
		List<Tuple> tupleList = query.fetch();
		return mapperComputer.fromTupleListToComputerList(tupleList);
	}
	
	JPAQuery<Tuple> addLimit(JPAQuery<Tuple> query, Map<String, String> criteria){
		int offset = Integer.parseInt(criteria.get(KEY_OFFSET));
		int limit = Integer.parseInt(criteria.get(KEY_LIMIT));
		return query.limit(limit).offset(offset);
	}
	
	JPAQuery<Tuple> addLike(JPAQuery<Tuple> query, Map<String, String> criteria){
		String search = criteria.get(KEY_SEARCH);
		if(search == null || search.isBlank()) {
			search = "%";
		}else {
			search = "%" + search + "%";
		}
		return query.where(qComputer.name.like(search)
				.or(qCompany.name.like(search)));
	}
	
	JPAQuery<Tuple> addOrderBy(JPAQuery<Tuple> query, Map<String, String> criteria){
		String mode = criteria.get(KEY_MODE);
		if(mode == null || mode.isBlank()) {
			mode = "asc";
		}
		String order = criteria.get(KEY_ORDER);
		if(order == null || order.isBlank()) {
			order = "id";
		}
		switch (order) {
		
		case "id":
			if("asc".equals(mode)) {
				query = query.orderBy(qComputer.id.asc());
			}else {
				query = query.orderBy(qComputer.id.desc());
			}
			break;
			
		case "name":
			if("asc".equals(mode)) {
				query = query.orderBy(qComputer.name.asc());
			}else {
				query = query.orderBy(qComputer.name.desc());
			}
			break;
			
		case "introduced":
			if("asc".equals(mode)) {
				query = query.orderBy(qComputer.introduced.asc());
			}else {
				query = query.orderBy(qComputer.introduced.desc());
			}
			break;
			
		case "discontinued":
			if("asc".equals(mode)) {
				query = query.orderBy(qComputer.discontinued.asc());
			}else {
				query = query.orderBy(qComputer.discontinued.desc());
			}
			break;
			
		case "company":
			if("asc".equals(mode)) {
				query = query.orderBy(qComputer.company.name.asc());
			}else {
				query = query.orderBy(qComputer.company.name.desc());
			}
			break;
			
		default:
			if("asc".equals(mode)) {
				query = query.orderBy(qComputer.id.asc());
			}else {
				query = query.orderBy(qComputer.id.desc());
			}
			break;
		}
		return query;
	}

	public long selectNumberOfComputer() {
		return queryFactory
				.from(qComputer)
				.fetchCount();
	}

	public long selectNumberOfComputerBySearch(String search) {
		String s = null;
		if(search == null || search.isBlank()) {
			s = "%";
		}else {
			s = "%" + search + "%";
		}
		return queryFactory
				.from(qComputer)
				.leftJoin(qComputer.company, qCompany)
				.on(qComputer.company.eq(qCompany))
				.where(qComputer.name.like("%" + s + "%")
						.or(qCompany.name.like("%" + s + "%")))
				.fetchCount();
	}

	public long deleteComputerById(int id) {
		entityManager.getTransaction().begin();
		long numDelete = queryFactory
				.delete(qComputer)
				.where(qComputer.id.eq(id))
				.execute();
		entityManager.getTransaction().commit();
		return numDelete;
	}

	public long deleteComputersByCompanyId(int id) {
		entityManager.getTransaction().begin();
		long numDelete = queryFactory
				.delete(qComputer)
				.where(qComputer.company.id.eq(id))
				.execute();
		entityManager.getTransaction().commit();
		return numDelete;
	}

	public long updateComputer(Computer computer) {
		try {
			entityManager.getTransaction().begin();
			long numupdate = queryFactory
					.update(qComputer)
					.where(qComputer.id.eq(computer.getId()))
					.set(qComputer.name, computer.getName())
					.set(qComputer.introduced, computer.getIntroduced())
					.set(qComputer.discontinued, computer.getDiscontinued())
					.set(qComputer.company, computer.getCompany())
					.execute();
			entityManager.getTransaction().commit();
			return numupdate;
		} catch (PersistenceException e) {
			entityManager.getTransaction().rollback();
			LOGGER.warn("Echec updateComputer", e);
			return 0;
		}
	}

	public boolean insertComputer(Computer computer) {
		entityManager.getTransaction().begin();
		try {
			entityManager.merge(computer);
			entityManager.getTransaction().commit();
		} catch (EntityNotFoundException e) {
			entityManager.getTransaction().rollback();
			LOGGER.warn("Echec insertComputer", e);
			return false;
		}
		return true;
	}

}