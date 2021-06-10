package com.excilys.cdb.daos;

import java.util.Map;

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
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

public class ComputerQueryBuilder {

	private static final String KEY_COMPUTER_ID = "computerId";
	private static final String KEY_NAME = "computerName";
	private static final String KEY_COMPANY_ID = "companyId";
	private static final String KEY_ORDER = "orderBy";
	private static final String KEY_MODE = "mode";
	private static final String KEY_LIMIT = "limit";
	private static final String KEY_OFFSET = "offset";

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
		return buildSelect().validate().toString();
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
		return new UpdateQuery(db.getTableComputer()).addSetClause(db.getColumnComputerName(), computer.getName())
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

	String selectComputerByCriteria(Map<String, String> criteria) {
		SelectQuery query = buildSelect();
		query = buildComputerId(query, criteria);
		query = buildCompanyId(query, criteria);
		query = buildComputerName(query, criteria);
		query = buildComputerLimit(query, criteria);
		query = buildComputerOrder(query, criteria);
		return query.validate().toString();
	}

	private SelectQuery buildComputerId(SelectQuery query, Map<String, String> criteria) {
		if (criteria.containsKey(KEY_COMPUTER_ID)) {
			String computerId = criteria.get(KEY_COMPUTER_ID);
			if (computerId != null && !computerId.isBlank()) {
				int id = Integer.valueOf(computerId);
				Condition whereCondition = BinaryCondition.equalTo(db.getColumnComputerId(), id);
				query = query.addCondition(whereCondition);
			}
		}
		return query;
	}

	private SelectQuery buildCompanyId(SelectQuery query, Map<String, String> criteria) {
		if (criteria.containsKey(KEY_COMPANY_ID)) {
			String companyId = criteria.get(KEY_COMPANY_ID);
			if (companyId != null && !companyId.isBlank()) {
				int id = Integer.valueOf(companyId);
				Condition whereCondition = BinaryCondition.equalTo(db.getColumnComputerCompanyId(), id);
				query = query.addCondition(whereCondition);
			}
		}
		return query;
	}

	private SelectQuery buildComputerName(SelectQuery query, Map<String, String> criteria) {
		if (criteria.containsKey(KEY_NAME)) {
			String computerName = criteria.get(KEY_NAME);
			if (computerName != null && !computerName.isBlank()) {
				Condition whereCondition = BinaryCondition.equalTo(db.getColumnComputerName(), computerName);
				query = query.addCondition(whereCondition);
			}
		}
		return query;
	}

	private SelectQuery buildComputerLimit(SelectQuery query, Map<String, String> criteria) {
		if (criteria.containsKey(KEY_LIMIT)) {
			String limit = criteria.get(KEY_LIMIT);
			if (limit != null && !limit.isBlank()) {
				int lim = Integer.valueOf(limit);
				if (criteria.containsKey(KEY_OFFSET)) {
					String offset = criteria.get(KEY_OFFSET);
					if (offset != null && !offset.isBlank()) {
						int off = Integer.valueOf(offset);
						query = query.addCustomization(new MysLimitClause(off, lim));
					} else {
						query = query.addCustomization(new MysLimitClause(lim));
					}
				} else {
					query = query.addCustomization(new MysLimitClause(lim));
				}
			}
		}
		return query;
	}

	private SelectQuery buildComputerOrder(SelectQuery query, Map<String, String> criteria) {
		if (criteria.containsKey(KEY_ORDER)) {
			String order = criteria.get(KEY_ORDER);
			if (order != null && !order.isBlank()) {
				DbColumn column = null;

				switch (order) {

				case "name":
					column = db.getColumnComputerName();
					break;

				case "introduced":
					column = db.getColumnComputerIntroduced();
					break;

				case "discontinued":
					column = db.getColumnComputerDiscontinued();
					break;

				case "company":
					column = db.getColumnCompanyId();
					break;

				default:
					break;
				}

				if (column != null && criteria.containsKey(KEY_MODE)) {
					String mode = criteria.get(KEY_MODE);
					if (mode != null && !mode.isBlank()) {

						switch (mode) {

						case "asc":
							query = query.addOrdering(column, Dir.ASCENDING);
							break;

						case "desc":
							query = query.addOrdering(column, Dir.DESCENDING);
							break;

						default:
							query = query.addOrderings(column);
							break;
						}
					} else {
						query = query.addOrderings(column);
					}
				}
			}
		}
		return query;
	}
}
