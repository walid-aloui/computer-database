package com.excilys.cdb.console;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.cdb.console.config.CliConfig;
import com.excilys.cdb.console.ui.Cli;

public class MainConsole {

	public static void main(String[] args) {
		System.out.println("MainConsole");
		@SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(CliConfig.class);
		Cli cli = context.getBean(Cli.class);
		cli.runCli();
	}

}
