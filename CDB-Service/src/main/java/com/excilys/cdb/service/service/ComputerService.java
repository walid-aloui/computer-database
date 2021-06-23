package com.excilys.cdb.service.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.excilys.cdb.core.model.Computer;
import com.excilys.cdb.persistence.daos.DaoComputer;

@Service
public class ComputerService {

	private DaoComputer daoComputer;

	public ComputerService(DaoComputer daoComputer) {
		this.daoComputer = daoComputer;
	}

	public List<Computer> selectAllComputers() {
		return daoComputer.selectAllComputers();
	}

	public Optional<Computer> selectComputerById(int id) {
		return daoComputer.selectComputerById(id);
	}

	public List<Computer> selectPartOfComputers(long limit, int offset) {
		return daoComputer.selectPartOfComputers(limit, offset);
	}

	public List<Computer> selectPartOfComputersBySearch(String search, int limit, int offset) {
		return daoComputer.selectPartOfComputersBySearch(search, limit, offset);
	}

	public List<Computer> selectComputersByCriteria(Map<String, String> criteria) {
		return daoComputer.selectComputersByCriteria(criteria);
	}

	public long selectNumberOfComputer() {
		return daoComputer.selectNumberOfComputer();
	}

	public long selectNumberOfComputerBySearch(String search) {
		return daoComputer.selectNumberOfComputerBySearch(search);
	}

	public long deleteComputerById(int id) {
		return daoComputer.deleteComputerById(id);
	}

	public long deleteComputersByCompanyId(int id) {
		return daoComputer.deleteComputersByCompanyId(id);
	}

	public long updateComputer(Computer computer) {
		return daoComputer.updateComputer(computer);
	}

	public boolean insertComputer(Computer computer) {
		return daoComputer.insertComputer(computer);
	}

}
