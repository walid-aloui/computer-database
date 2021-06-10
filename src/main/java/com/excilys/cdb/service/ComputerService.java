package com.excilys.cdb.service;

import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

import com.excilys.cdb.daos.DaoComputer;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.model.Computer;

public class ComputerService {

	private static ComputerService computerService;
	private DaoComputer daoComputer;

	public static ComputerService getInstance() {
		if (computerService == null) {
			computerService = new ComputerService();
		}
		return computerService;
	}

	public static void setComputerService(ComputerService computerService) {
		ComputerService.computerService = computerService;
	}

	private ComputerService() {
		super();
		daoComputer = DaoComputer.getInstance();
	}

	public LinkedList<Computer> getComputersByCriteria(Map<String, String> criteria)
			throws OpenException, MapperException, ExecuteQueryException {
		return daoComputer.getComputersByCriteria(criteria);
	}

	public LinkedList<Computer> getAllComputers() throws OpenException, MapperException, ExecuteQueryException {
		return daoComputer.getAllComputers();
	}

	public Optional<Computer> getComputerById(int id) throws OpenException, MapperException, ExecuteQueryException {
		return daoComputer.getComputerById(id);
	}

	public LinkedList<Computer> getComputerByName(String computerName)
			throws OpenException, MapperException, ExecuteQueryException {
		return daoComputer.getComputerByName(computerName);
	}

	public LinkedList<Computer> getPartOfComputers(int n, int offset)
			throws OpenException, MapperException, ExecuteQueryException {
		return daoComputer.getPartOfComputers(n, offset);
	}

	public LinkedList<Computer> getPartOfComputersByName(String name, int n, int offset)
			throws OpenException, MapperException, ExecuteQueryException {
		return daoComputer.getPartOfComputersByName(name, n, offset);
	}

	public int getNumberOfComputer() throws OpenException, MapperException, ExecuteQueryException {
		return daoComputer.getNumberOfComputer();
	}

	public int deleteComputerById(int id) throws OpenException, ExecuteQueryException {
		return daoComputer.deleteComputerById(id);
	}

	public int updateComputerById(int id, Computer computer) throws OpenException {
		return daoComputer.updateComputerById(id, computer);
	}

	public int insertComputer(Computer computer) throws OpenException {
		return daoComputer.insertComputer(computer);
	}

}
