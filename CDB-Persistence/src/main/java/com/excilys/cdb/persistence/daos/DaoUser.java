package com.excilys.cdb.persistence.daos;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.binding.mapper.MapperUser;
import com.excilys.cdb.core.model.QUser;
import com.excilys.cdb.core.model.User;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class DaoUser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DaoUser.class);
	private static final QUser qUser = QUser.user;

	private EntityManager entityManager;
	private JPAQueryFactory queryBuilder;
	private MapperUser mapperUser;
	
	public DaoUser(EntityManager entityManager, MapperUser mapperUser) {
		this.entityManager = entityManager;
		this.queryBuilder = new JPAQueryFactory(entityManager);
		this.mapperUser = mapperUser;
	}

	public JPAQuery<Tuple> buildSelectAll() {
		return queryBuilder
				.select(qUser.login,
						qUser.password,
						qUser.role)
				.from(qUser);
	}
	
	public List<User> selectAllUser() {
		List<Tuple> tupleList = buildSelectAll().fetch();
		return mapperUser.fromTupleListToUserList(tupleList);
	}
	
	public Optional<User> selectUserById(String login) {
		Tuple tuple = buildSelectAll()
				.where(qUser.login.eq(login))
				.fetchOne();
		if(tuple == null) {
			return Optional.empty();
		}else {
			return Optional.of(mapperUser.fromTupleToUser(tuple));
		}
	}
	
	public boolean insertUser(User user) {
		entityManager.getTransaction().begin();
		try {
			entityManager.merge(user);
		} catch (EntityNotFoundException e) {
			entityManager.getTransaction().rollback();
			LOGGER.warn("Echec insertUser", e);
			return false;
		}
		entityManager.getTransaction().commit();
		return true;
	}
	
}
