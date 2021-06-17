package com.excilys.cdb.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.excilys.cdb.validator",
				"com.excilys.cdb.mapper",
				"com.excilys.cdb.daos",
				"com.excilys.cdb.service",
				"com.excilys.cdb.ui"})
public class CliConfig {

}
