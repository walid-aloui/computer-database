package com.excilys.cdb.daos;

import org.springframework.stereotype.Repository;

import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

@Repository
public class Database {

	private static final String TABLE_COMPANY = "company";
	private static final String COLUMN_COMPANY_NAME = "name";
	private static final String COLUMN_COMPANY_ID = "id";

	private static final String TABLE_COMPUTER = "computer";
	private static final String COLUMN_COMPUTER_ID = "id";
	private static final String COLUMN_COMPUTER_NAME = "name";
	private static final String COLUMN_INTRODUCED = "introduced";
	private static final String COLUMN_DISCONTINUED = "discontinued";
	private static final String COLUMN_COMPUTER_COMPANY_ID = "company_id";

	private static DbSchema schemaObj;
	private static DbSpec specficationObj;

	private DbTable tableCompany;
	private DbColumn columnCompanyId;
	private DbColumn columnCompanyName;

	private DbTable tableComputer;
	private DbColumn columnComputerId;
	private DbColumn columnComputerName;
	private DbColumn columnComputerIntroduced;
	private DbColumn columnComputerDiscontinued;
	private DbColumn columnComputerCompanyId;

	public Database() {
		super();
		loadSQLBuilderSchema();
		createTables();
	}

	private void loadSQLBuilderSchema() {
		specficationObj = new DbSpec();
		schemaObj = specficationObj.addDefaultSchema();
	}

	private void createTables() {
		createTableCompany();
		createTableComputer();
	}

	private void createTableCompany() {
		tableCompany = new DbTable(schemaObj, TABLE_COMPANY, TABLE_COMPANY);
		// tableCompany = schemaObj.addTable(TABLE_COMPANY);
		columnCompanyId = tableCompany.addColumn(COLUMN_COMPANY_ID);
		columnCompanyName = tableCompany.addColumn(COLUMN_COMPANY_NAME);
	}

	private void createTableComputer() {
		tableComputer = new DbTable(schemaObj, TABLE_COMPUTER, TABLE_COMPUTER);
		// tableComputer = schemaObj.addTable(TABLE_COMPUTER);
		columnComputerId = tableComputer.addColumn(COLUMN_COMPUTER_ID);
		columnComputerName = tableComputer.addColumn(COLUMN_COMPUTER_NAME);
		columnComputerIntroduced = tableComputer.addColumn(COLUMN_INTRODUCED);
		columnComputerDiscontinued = tableComputer.addColumn(COLUMN_DISCONTINUED);
		columnComputerCompanyId = tableComputer.addColumn(COLUMN_COMPUTER_COMPANY_ID);
	}

	public DbTable getTableCompany() {
		return tableCompany;
	}

	public DbColumn getColumnCompanyId() {
		return columnCompanyId;
	}

	public DbColumn getColumnCompanyName() {
		return columnCompanyName;
	}

	public DbTable getTableComputer() {
		return tableComputer;
	}

	public DbColumn getColumnComputerId() {
		return columnComputerId;
	}

	public DbColumn getColumnComputerName() {
		return columnComputerName;
	}

	public DbColumn getColumnComputerIntroduced() {
		return columnComputerIntroduced;
	}

	public DbColumn getColumnComputerDiscontinued() {
		return columnComputerDiscontinued;
	}

	public DbColumn getColumnComputerCompanyId() {
		return columnComputerCompanyId;
	}

}
