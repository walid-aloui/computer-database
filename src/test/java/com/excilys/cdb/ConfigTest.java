package com.excilys.cdb;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan({"com.excilys.cdb.mapper", 
				"com.excilys.cdb.daos", 
				"com.excilys.cdb.service", 
				"com.excilys.cdb.ui",
				"com.excilys.cdb.validator"})
public class ConfigTest {
	
	private static final String HIKARY_CONFIG_FILE = "/datasource.properties";

	@Bean
	public HikariDataSource getDatasource() {
		return new HikariDataSource(new HikariConfig(HIKARY_CONFIG_FILE));
	}
	
	@Bean
	public EntityManager entityManager() {
		return entityManagerFactory().createEntityManager();
	}

	private EntityManagerFactory entityManagerFactory() {
		final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(getDatasource());
		em.setPackagesToScan("com.excilys.cdb.model");
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setJpaProperties(hibernateProperties());
		em.setPersistenceUnitName("UP_CDB");
		em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		em.afterPropertiesSet();
		return em.getObject();
	}

	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.show_sql", "true");
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		properties.setProperty("hibernate.hbm2ddl.auto", "update");
		return properties;
	}
}
