package com.excilys.cdb.persistence.daos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.excilys.cdb.core.model.User;
import com.excilys.cdb.core.model.UserRole;
import com.excilys.cdb.persistence.ConfigTest;
import com.zaxxer.hikari.HikariDataSource;

@SpringJUnitConfig(ConfigTest.class)
public class DaoUserTest {
	
	private DaoUser daoUser;
	private HikariDataSource ds;
	
	@Autowired
	public DaoUserTest(DaoUser daoUser, HikariDataSource ds) {
		this.daoUser = daoUser;
		this.ds = ds;
	}
	
	@BeforeEach
	void setUp() throws Exception {
		try (Connection con = ds.getConnection();) {
			ScriptRunner sr = new ScriptRunner(con);
			Reader reader = new BufferedReader(
					new FileReader("src/test/resources/test-db.sql"));
			sr.runScript(reader);
		}
	}
	
	private User createWr7User() {
		String login = "wr7";
		String password = "boss";
		UserRole role = UserRole.ADMIN;
		User user = new User(login, password, role);
		daoUser.insertUser(user);
		return user;
	}
	
	private User createTestUser() {
		String login = "test";
		String password = "test";
		UserRole role = UserRole.USER;
		User user = new User(login, password, role);
		daoUser.insertUser(user);
		return user;
	}
	
	@Test
	void testSelectAllUsersShouldReturnListOfUsers() {
		createWr7User();
		createTestUser();
		List<User> userList = daoUser.selectAllUsers();
		assertEquals(2, userList.size());
	}
	
	@Test
	void testSelectAllUsersShouldReturnAnEmptyListOfUsers() {
		List<User> userList = daoUser.selectAllUsers();
		assertEquals(0, userList.size());
	}
	
	@Test
	void testSelectUserByIdShouldReturnUser() {
		User wr7 = createWr7User();
		createTestUser();
		Optional<User> optUser = daoUser.selectUserById(wr7.getLogin());
		if(optUser.isPresent()) {
			User user = optUser.get();
			assertEquals(wr7, user);
		}else {
			fail("Should Not Be Empty");
		}
	}
	
	@Test
	void testSelectUserByIdShouldNotReturnUser() {
		createWr7User();
		createTestUser();
		String login = "falseLogin";
		Optional<User> optUser = daoUser.selectUserById(login);
		if(optUser.isPresent()) {
			fail("Should Be Empty");
		}
	}
	
	@Test
	void testInsertUserShouldNotInsertUser() {
		User wr7 = createWr7User();
		String login = wr7.getLogin();
		String password = "bla";
		UserRole role = UserRole.USER;
		User user = new User(login, password, role);
		boolean insert = daoUser.insertUser(user);
		assertFalse(insert);
	}
	
}
