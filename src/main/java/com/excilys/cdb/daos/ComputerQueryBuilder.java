package com.excilys.cdb.daos;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.DeleteQuery;
import com.healthmarketscience.sqlbuilder.FunctionCall;
import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.healthmarketscience.sqlbuilder.OrderObject.Dir;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.SelectQuery.JoinType;
import com.healthmarketscience.sqlbuilder.UpdateQuery;
import com.healthmarketscience.sqlbuilder.custom.mysql.MysLimitClause;

public class ComputerQueryBuilder {

	private static ComputerQueryBuilder computerQueryBuilder;
	private Database db;

	public static ComputerQueryBuilder getInstance() {
		if (computerQueryBuilder == null) {
			computerQueryBuilder = new ComputerQueryBuilder();
		}
		return computerQueryBuilder;
	}

	private ComputerQueryBuilder() {
		super();
		db = Database.getInstance();
	}

	private SelectQuery buildSelect() {
		JoinType joinType = JoinType.LEFT_OUTER;
		Condition joinCond = BinaryCondition.equalTo(db.getColumnComputerCompanyId(), db.getColumnCompanyId());
		return new SelectQuery()
				.addColumns(db.getColumnComputerId(), 
							db.getColumnComputerName(), 
							db.getColumnComputerIntroduced(),
							db.getColumnComputerDiscontinued(), 
							db.getColumnComputerCompanyId(), 
							db.getColumnCompanyId(),
							db.getColumnCompanyName())
				.addJoin(joinType, 
						db.getTableComputer(), 
						db.getTableCompany(), 
						joinCond);
	}

	String selectAllComputer() {
		return buildSelect()
				.validate()
				.toString();
	}

	String selectComputerById(int id) {
		Condition whereCond = BinaryCondition.equalTo(db.getColumnComputerId(), id);
		return buildSelect()
				.addCondition(whereCond)
				.validate()
				.toString();
	}

	String selectComputerByName(String name) {
		Condition whereCond = BinaryCondition.equalTo(db.getColumnComputerName(), name);
		return buildSelect()
				.addCondition(whereCond)
				.validate()
				.toString();
	}

	String selectComputerOrderBy(String order, Dir dir) {
		return buildSelect()
				.addCustomOrdering(order, dir)
				.validate()
				.toString();
	}

	String selectPartOfComputer(int n, int offset) {
		return buildSelect()
				.addCustomization(new MysLimitClause(offset, n))
				.validate()
				.toString();
	}

	String selectPartOfComputerByName(String name, int n, int offset) {
		Condition whereCond = BinaryCondition.equalTo(db.getColumnComputerName(), name);
		return buildSelect()
				.addCustomization(new MysLimitClause(offset, n))
				.addCondition(whereCond)
				.validate()
				.toString();
	}

	String deleteComputerById(int id) {
		Condition whereCond = BinaryCondition.equalTo(db.getColumnComputerId(), id);
		return new DeleteQuery(db.getTableComputer())
				.addCondition(whereCond)
				.validate()
				.toString();
	}

	String deleteComputerByCompanyId(int id) {
		Condition whereCond = BinaryCondition.equalTo(db.getColumnComputerCompanyId(), id);
		return new DeleteQuery(db.getTableComputer())
				.addCondition(whereCond)
				.validate()
				.toString();
	}

	String insertComputer(Computer computer) {
		Company company = computer.getCompany();
		Integer companyId = (company == null) ? null : company.getId();
		return new InsertQuery(db.getTableComputer())
				.addColumn(db.getColumnComputerName(), computer.getName())
				.addColumn(db.getColumnComputerIntroduced(), computer.getIntroduced())
				.addColumn(db.getColumnComputerDiscontinued(), computer.getDiscontinued())
				.addColumn(db.getColumnComputerCompanyId(), companyId)
				.validate()
				.toString();
	}

	String updateComputerById(int id, Computer computer) {
		Company company = computer.getCompany();
		Integer companyId = (company == null) ? null : company.getId();
		Condition cond = BinaryCondition.equalTo(db.getColumnComputerId(), id);
		return new UpdateQuery(db.getTableComputer())
				.addSetClause(db.getColumnComputerName(), computer.getName())
				.addSetClause(db.getColumnComputerIntroduced(), computer.getIntroduced())
				.addSetClause(db.getColumnComputerDiscontinued(), computer.getDiscontinued())
				.addSetClause(db.getColumnComputerCompanyId(), companyId).addCondition(cond)
				.validate()
				.toString();
	}

	String getNumberOfComputer() {
		final FunctionCall column = FunctionCall.countAll();
		final String alias = "elements";
		return new SelectQuery()
				.addAliasedColumn(column, alias)
				.addFromTable(db.getTableComputer())
				.validate()
				.toString();
	}

	public static void main(String[] args) {
		// ComputerQueryBuilder builder = ComputerQueryBuilder.getInstance();
		// System.out.println(builder.selectPartOfComputerByName("walid", 10, 50));
	}
}
