package com.excilys.cdb.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.excilys.cdb.daos.DaoComputer;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.model.Computer;

@Service
public class ComputerService {

	private DaoComputer daoComputer;

	public ComputerService(DaoComputer daoComputer) {
		this.daoComputer = daoComputer;
	}

	public List<Computer> selectAllComputers() throws ExecuteQueryException {
		return daoComputer.selectAllComputers();
	}

	public Optional<Computer> selectComputerById(int id) throws ExecuteQueryException {
		return daoComputer.selectComputerById(id);
	}

	public List<Computer> selectComputersByName(String name) throws ExecuteQueryException {
		return daoComputer.selectComputersByName(name);
	}

	public List<Computer> selectPartOfComputers(int limit, int offset) throws ExecuteQueryException {
		return daoComputer.selectPartOfComputers(limit, offset);
	}

	public List<Computer> selectPartOfComputersByName(String name, int limit, int offset) throws ExecuteQueryException {
		return daoComputer.selectPartOfComputersByName(name, limit, offset);
	}

	public List<Computer> selectComputersByCriteria(Map<String, String> criteria) throws ExecuteQueryException {
		return daoComputer.selectComputersByCriteria(criteria);
	}

	public int selectNumberOfComputer() throws ExecuteQueryException {
		return daoComputer.selectNumberOfComputer();
	}

	public int selectNumberOfComputerBySearch(String search) throws ExecuteQueryException {
		return daoComputer.selectNumberOfComputerBySearch(search);
	}

	public int deleteComputerById(int id) {
		return daoComputer.deleteComputerById(id);
	}

	public int deleteComputersByCompanyId(int id) {
		return daoComputer.deleteComputersByCompanyId(id);
	}

	public int updateComputer(Computer computer) {
		return daoComputer.updateComputer(computer);
	}

	public int insertComputer(Computer computer) {
		return daoComputer.insertComputer(computer);
	}

}
