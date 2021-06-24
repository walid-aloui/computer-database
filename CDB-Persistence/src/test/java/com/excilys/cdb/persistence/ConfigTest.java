package com.excilys.cdb.persistence;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.excilys.cdb.persistence.config.PersistenceConfig;

@Configuration
@Import(PersistenceConfig.class)
@ComponentScan({"com.excilys.cdb.binding.validator",
				"com.excilys.cdb.binding.mapper",
				"com.excilys.cdb.persistence.daos"})
public class ConfigTest {
}
