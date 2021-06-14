package com.excilys.cdb.daos;

import org.springframework.stereotype.Repository;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.DeleteQuery;
import com.healthmarketscience.sqlbuilder.SelectQuery;

@Repository
public class CompanyQueryBuilder {

	private Database db;
	
	public CompanyQueryBuilder(Database db) {
		this.db = db;
	}

	SelectQuery buildSelect() {
		return new SelectQuery()
				.addColumns(db.getColumnCompanyId(), 
							db.getColumnCompanyName());
	}

	String selectAllCompanies() {
		return buildSelect()
				.validate()
				.toString();
	}

	String selectCompanyById(int id) {
		Condition cond = BinaryCondition.equalTo(db.getColumnCompanyId(), id);
		return buildSelect()
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
