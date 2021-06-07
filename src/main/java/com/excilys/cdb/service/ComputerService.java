package com.excilys.cdb.service;

import java.util.LinkedList;
import java.util.Optional;

import com.excilys.cdb.daos.DaoComputer;
import com.excilys.cdb.exception.ExecuteQueryException;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.exception.OpenException;
import com.excilys.cdb.model.Computer;

public class ComputerService {

	private static ComputerService computerService;

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
	}

	public LinkedList<Computer> getAllComputers() throws OpenException, MapperException, ExecuteQueryException {
		return DaoComputer.getInstance().getAllComputers();
	}

	public Optional<Computer> getComputerById(int id) throws OpenException, MapperException, ExecuteQueryException {
		return DaoComputer.getInstance().getComputerById(id);
	}

	public LinkedList<Computer> getComputerByName(String computerName)
			throws OpenException, MapperException, ExecuteQueryException {
		return DaoComputer.getInstance().getComputerByName(computerName);
	}

	public LinkedList<Computer> getPartOfComputers(int n, int offset)
			throws OpenException, MapperException, ExecuteQueryException {
		return DaoComputer.getInstance().getPartOfComputers(n, offset);
	}

	public LinkedList<Computer> getPartOfComputersByName(String name, int n, int offset)
			throws OpenException, MapperException, ExecuteQueryException {
		return DaoComputer.getInstance().getPartOfComputersByName(name, n, offset);
	}

	public int getNumberOfComputer() throws OpenException, MapperException, ExecuteQueryException {
		return DaoComputer.getInstance().getNumberOfComputer();
	}

	public int deleteComputerById(int id) throws OpenException, ExecuteQueryException {
		return DaoComputer.getInstance().deleteComputerById(id);
	}

	public int updateComputerById(int id, Computer computer) throws OpenException {
		return DaoComputer.getInstance().updateComputerById(id, computer);
	}

	public int insertComputer(Computer computer) throws OpenException {
		return DaoComputer.getInstance().insertComputer(computer);
	}

}
