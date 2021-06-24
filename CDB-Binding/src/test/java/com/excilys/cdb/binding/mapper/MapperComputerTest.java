package com.excilys.cdb.binding.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.excilys.cdb.binding.ConfigTest;
import com.excilys.cdb.binding.dtos.ComputerDto;
import com.excilys.cdb.binding.dtos.ComputerDto.ComputerDtoBuilder;
import com.excilys.cdb.binding.exception.MapperException;
import com.excilys.cdb.binding.utils.SecureInputs;
import com.excilys.cdb.core.model.Computer;
import com.excilys.cdb.core.model.Computer.ComputerBuilder;

@SpringJUnitConfig(ConfigTest.class)
class MapperComputerTest {
	
	private MapperComputer mapperComputer;
	
	@Autowired
	public MapperComputerTest(MapperComputer mapperComputer) {
		this.mapperComputer = mapperComputer;
	}
	
	@Test
	void testFromComputerDtoToComputerShouldReturnComputer() {
		ComputerDto computerDto = new ComputerDtoBuilder()
				.withId(1)
				.withName("test")
				.withIntroduced("")
				.withDiscontinued("")
				.withCompanyId("0")
				.build();
		try {
			Computer computer = mapperComputer.fromComputerDtoToComputer(computerDto);
			String companyId = (computer.getCompany() == null) ? "0" : String.valueOf(computer.getCompany().getId());
			assertEquals(computerDto.getId(), computer.getId());
			assertEquals(computerDto.getName(), computer.getName());
			assertEquals(null, computer.getIntroduced());
			assertEquals(null, computer.getDiscontinued());
			assertEquals(computerDto.getCompanyId(), companyId);
		} catch (MapperException e) {
			fail("Should not throw an exception");
		}
	}
	
	@Test
	void testFromComputerDtoToComputerShouldThrowAnException() {
		assertThrows(MapperException.class, () -> {
			ComputerDto computerDto = new ComputerDtoBuilder()
					.withId(1)
					.withName("test")
					.withIntroduced("2021-05-11")
					.withDiscontinued("2021-05-11")
					.withCompanyId("0")
					.build();
			mapperComputer.fromComputerDtoToComputer(computerDto);
		});
	}
	
	@Test
	void testFromComputerListToComputerDtoListShouldReturnListOfComputerDto() {
		List<Computer> computers = new ArrayList<>();
		Computer wr7 = new ComputerBuilder()
				.withId(1)
				.withName("Computer_WR7")
				.build();
		Computer test = new ComputerBuilder()
				.withId(2)
				.withName("test")
				.withIntroduced(SecureInputs.toLocalDate("2021-05-11").get())
				.withDiscontinued(SecureInputs.toLocalDate("2022-05-11").get())
				.build();
		computers.add(wr7);
		computers.add(test);
		List<ComputerDto> computersDto = mapperComputer.fromComputerListToComputerDtoList(computers);
		assertEquals(2, computersDto.size());
		ComputerDto computerDto = computersDto.get(0);
		assertEquals(wr7.getId(), computerDto.getId());
		assertEquals(wr7.getName(), computerDto.getName());
		assertEquals(null, computerDto.getIntroduced());
		assertEquals(null, computerDto.getDiscontinued());
		assertEquals(null, computerDto.getCompanyId());
	}
	
	@Test
	void testFromComputerListToComputerDtoListShouldReturnAnEmptyListOfComputerDto() {
		List<Computer> computers = new ArrayList<>();
		List<ComputerDto> computersDto = mapperComputer.fromComputerListToComputerDtoList(computers);
		assertEquals(0, computersDto.size());
	}

}
