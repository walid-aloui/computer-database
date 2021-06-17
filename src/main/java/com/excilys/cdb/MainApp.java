package com.excilys.cdb;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.cdb.config.CliConfig;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.ui.Cli;

public class MainApp {

	public static void main(String[] args) throws OpenException, MapperException, ExecuteQueryException {
		@SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(CliConfig.class);
		Cli cli = context.getBean(Cli.class);
		cli.runCli();
	}

}