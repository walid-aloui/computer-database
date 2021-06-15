package com.excilys.cdb.validator;

import org.springframework.stereotype.Component;

import com.excilys.cdb.dtos.ComputerDto;
import com.excilys.cdb.utils.SecureInputs;

@Component
public class ValidatorComputerDto {

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
		return (id == null) ||
				(SecureInputs.isInteger(id) && 
				validatorId(Integer.parseInt(id)));
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
