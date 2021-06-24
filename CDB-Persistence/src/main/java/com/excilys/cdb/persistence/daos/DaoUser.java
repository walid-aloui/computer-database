package com.excilys.cdb.persistence.daos;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.core.model.QUser;
import com.excilys.cdb.core.model.User;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class DaoUser {

	private static final Logger LOGGER = LoggerFactory.getLogger(DaoUser.class);
	private static final QUser qUser = QUser.user;

	private EntityManager entityManager;
	private JPAQueryFactory queryBuilder;

	public DaoUser(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.queryBuilder = new JPAQueryFactory(entityManager);
	}

	private JPAQuery<User> buildSelectAll() {
		return queryBuilder.selectFrom(qUser);
	}

	public List<User> selectAllUsers() {
		return buildSelectAll().fetch();
	}

	public Optional<User> selectUserById(String login) {
		User user = buildSelectAll()
				.where(qUser.login.eq(login))
				.fetchOne();
		if (user == null) {
			return Optional.empty();
		} else {
			return Optional.of(user);
		}
	}

	public boolean insertUser(User user) {
		try {
			entityManager.clear();
			entityManager.getTransaction().begin();
			entityManager.persist(user);
			entityManager.getTransaction().commit();
		} catch (PersistenceException e) {
			entityManager.getTransaction().rollback();
			LOGGER.warn("Echec insertUser", e);
			return false;
		}
		return true;
	}

}
