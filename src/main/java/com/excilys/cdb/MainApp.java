package com.excilys.cdb;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.ui.Cli;

@Configuration
@ComponentScan({"com.excilys.cdb.mapper", 
				"com.excilys.cdb.daos", 
				"com.excilys.cdb.service", 
				"com.excilys.cdb.ui",
				"com.excilys.cdb.validator"})
public class MainApp {

	public static void main(String[] args) throws OpenException, MapperException, ExecuteQueryException {
		@SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(MainApp.class);
		Cli cli = context.getBean(Cli.class);
		cli.runCli();
	}

}