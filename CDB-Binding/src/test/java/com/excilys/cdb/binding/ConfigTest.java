package com.excilys.cdb.binding;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.excilys.cdb.binding.validator",
				"com.excilys.cdb.binding.mapper"})
public class ConfigTest {
}
