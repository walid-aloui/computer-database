package com.excilys.cdb;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.excilys.cdb.mapper", 
				"com.excilys.cdb.daos", 
				"com.excilys.cdb.service", 
				"com.excilys.cdb.ui",
				"com.excilys.cdb.validator"})
public class ConfigTest {
}
