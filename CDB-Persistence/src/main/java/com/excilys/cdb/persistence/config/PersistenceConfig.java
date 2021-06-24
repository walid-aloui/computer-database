package com.excilys.cdb.persistence.config;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class PersistenceConfig {

	private static final String HIKARY_CONFIG_FILE = "/datasource.properties";

	/*--------------------Datasource--------------------->*/

	@Bean
	public HikariDataSource getDatasource() {
		return new HikariDataSource(new HikariConfig(HIKARY_CONFIG_FILE));
	}

	/*--------------------Hibernate--------------------->*/
	
	@Bean
	public EntityManager entityManager() {
		return entityManagerFactory().createEntityManager();
	}

	private EntityManagerFactory entityManagerFactory() {
		final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(getDatasource());
		em.setPackagesToScan("com.excilys.cdb.core.model");
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setJpaProperties(hibernateProperties());
		em.setPersistenceUnitName("UP_CDB");
		em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		em.afterPropertiesSet();
		return em.getObject();
	}

	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.show_sql", "false");
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		properties.setProperty("hibernate.hbm2ddl.auto", "update");
		return properties;
	}

}
