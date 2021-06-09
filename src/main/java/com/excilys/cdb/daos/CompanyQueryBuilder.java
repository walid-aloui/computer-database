package com.excilys.cdb.daos;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.DeleteQuery;
import com.healthmarketscience.sqlbuilder.SelectQuery;

public class CompanyQueryBuilder {

	private static CompanyQueryBuilder companyQueryBuilder;
	Database db;

	public static CompanyQueryBuilder getInstance() {
		if (companyQueryBuilder == null) {
			companyQueryBuilder = new CompanyQueryBuilder();
		}
		return companyQueryBuilder;
	}

	private CompanyQueryBuilder() {
		super();
		db = Database.getInstance();
	}

	String selectAllCompanies() {
		return new SelectQuery()
				.addColumns(db.getColumnCompanyId(), 
							db.getColumnCompanyName())
				.validate()
				.toString();
	}

	String selectCompanyById(int id) {
		Condition cond = BinaryCondition.equalTo(db.getColumnCompanyId(), id);
		return new SelectQuery()
				.addColumns(db.getColumnCompanyId(), 
							db.getColumnCompanyName())
				.addCondition(cond)
				.validate()
				.toString();
	}

	String deleteCompanyById(int id) {
		Condition cond = BinaryCondition.equalTo(db.getColumnCompanyId(), id);
		return new DeleteQuery(db.getTableCompany())
				.addCondition(cond)
				.validate()
				.toString();
	}

}
