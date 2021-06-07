package com.excilys.cdb.validator;

import com.excilys.cdb.dtos.ComputerDto;
import com.excilys.cdb.utils.SecureInputs;

public class ValidatorComputerDto {

	private static ValidatorComputerDto validatorComputerDto;

	public static ValidatorComputerDto getInstance() {
		if (validatorComputerDto == null) {
			validatorComputerDto = new ValidatorComputerDto();
		}
		return validatorComputerDto;
	}

	private ValidatorComputerDto() {
		super();
	}

	public boolean isValid(ComputerDto computerDto) {
		return validatorId(computerDto.getId()) && 
				validatorCompanyId(computerDto.getCompanyId()) && 
				validatorName(computerDto.getName()) && 
				validatorDate(computerDto.getIntroduced(), computerDto.getDiscontinued());
	}

	private boolean validatorId(int id) {
		return id >= 0;
	}

	private boolean validatorCompanyId(String id) {
		return SecureInputs.isInteger(id) && validatorId(Integer.parseInt(id));
	}

	private boolean validatorName(String name) {
		return name != null && !name.isBlank();
	}

	private boolean isDate(String date) {
		return "".equals(date) || SecureInputs.toLocalDate(date).isPresent();
	}

	private boolean validatorDate(String introduced, String discontinued) {
		if (!isDate(introduced) || !isDate(discontinued)) {
			return false;
		}
		if ("".equals(introduced) || "".equals(discontinued)) {
			return true;
		}
		return SecureInputs.toLocalDate(discontinued).get().isAfter(SecureInputs.toLocalDate(introduced).get());

	}

}
