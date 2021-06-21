package com.excilys.cdb.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dtos.ComputerDto;
import com.excilys.cdb.dtos.ComputerDto.ComputerDtoBuilder;
import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Company.CompanyBuilder;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Computer.ComputerBuilder;
import com.excilys.cdb.model.QComputer;
import com.excilys.cdb.utils.SecureInputs;
import com.excilys.cdb.validator.ValidatorComputerDto;
import com.querydsl.core.Tuple;

@Component
public class MapperComputer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapperComputer.class);
	private static final QComputer qComputer = QComputer.computer;
	
	private ValidatorComputerDto validatorComputerDto;
	
	public MapperComputer(ValidatorComputerDto validatorComputerDto) {
		this.validatorComputerDto = validatorComputerDto;
	}

	public Computer fromComputerDtoToComputer(ComputerDto computerDto) throws MapperException {
		if (validatorComputerDto.isValid(computerDto)) {
			Company company = null;
			int companyId = Integer.parseInt(computerDto.getCompanyId());
			if (companyId != 0) {
				company = new CompanyBuilder()
						.withId(companyId)
						.build();
			}
			return new ComputerBuilder()
					.withId(computerDto.getId())
					.withName(computerDto.getName())
					.withIntroduced(SecureInputs.toLocalDate(computerDto.getIntroduced()).orElse(null))
					.withDiscontinued(SecureInputs.toLocalDate(computerDto.getDiscontinued()).orElse(null))
					.withCompany(company)
					.build();
		}
		LOGGER.error("Echec fromComputerDtoToComputer : ComputerDto pas valide");
		throw new MapperException();
	}

	public ComputerDto fromComputerToComputerDto(Computer computer) {
		ComputerDtoBuilder computerDtoBuilder = new ComputerDtoBuilder()
				.withId(computer.getId())
				.withName(computer.getName());
		if (computer.getIntroduced() != null) {
			computerDtoBuilder.withIntroduced(computer.getIntroduced().toString());
		}
		if (computer.getDiscontinued() != null) {
			computerDtoBuilder.withDiscontinued(computer.getDiscontinued().toString());
		}
		if (computer.getCompany() != null) {
			computerDtoBuilder.withCompanyId(String.valueOf(computer.getCompany().getId()));
		}
		return computerDtoBuilder.build();
	}

	public List<ComputerDto> fromComputerListToComputerDtoList(List<Computer> computers) {
		return computers
				.stream()
				.map(computer -> fromComputerToComputerDto(computer))
				.collect(Collectors.toList());
	}
	
	public Computer fromTupleToComputer(Tuple t) {
		return new ComputerBuilder()
				.withId(t.get(qComputer.id))
				.withName(t.get(qComputer.name))
				.withIntroduced(t.get(qComputer.introduced))
				.withDiscontinued(t.get(qComputer.discontinued))
				.withCompany(t.get(qComputer.company))
				.build();
	}
	
	public List<Computer> fromTupleListToComputerList(List<Tuple> tupleList) {
		return tupleList
				.stream()
				.map(tuple -> fromTupleToComputer(tuple))
				.collect(Collectors.toList());
	}

}