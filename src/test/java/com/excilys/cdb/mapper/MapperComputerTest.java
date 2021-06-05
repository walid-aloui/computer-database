package com.excilys.cdb.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import com.excilys.cdb.exception.MapperException;
import com.excilys.cdb.model.Computer;

class MapperComputerTest {

	@Test
	void testMapToComputerShouldReturnListOfComputers() throws SQLException, MapperException {
		ResultSet mockResultSet = mock(ResultSet.class);
		when(mockResultSet.next()).thenReturn(true).thenReturn(false);
		when(mockResultSet.getInt("computer.id")).thenReturn(1);
		when(mockResultSet.getString("computer.name")).thenReturn("WR7");
		Date introduced = Date.valueOf("2021-05-11");
		when(mockResultSet.getDate("introduced")).thenReturn(introduced);
		Date discontinued = Date.valueOf("2022-05-11");
		when(mockResultSet.getDate("discontinued")).thenReturn(discontinued);
		when(mockResultSet.getInt("company_id")).thenReturn(2);
		when(mockResultSet.getString("company.name")).thenReturn("companyWR7");
		LinkedList<Computer> computerList = MapperComputer.getInstance().fromResultSetToComputer(mockResultSet);
		assertEquals(1, computerList.size());
		Computer computer = computerList.getFirst();
		assertEquals(1, computer.getId());
		assertEquals("WR7", computer.getName());
		assertEquals(introduced.toLocalDate(), computer.getIntroduced());
		assertEquals(discontinued.toLocalDate(), computer.getDiscontinued());
		assertEquals(2, computer.getCompany().getId());
		assertEquals("companyWR7", computer.getCompany().getName());
		verify(mockResultSet, times(2)).next();
		verify(mockResultSet, times(1)).getInt("computer.id");
		verify(mockResultSet, times(1)).getString("computer.name");
		verify(mockResultSet, times(1)).getDate("introduced");
		verify(mockResultSet, times(1)).getDate("discontinued");
		verify(mockResultSet, times(1)).getInt("company_id");
		verify(mockResultSet, times(1)).getString("company.name");
	}

	@Test
	void test2MapToComputerShouldReturnListOfComputers() throws SQLException, MapperException {
		ResultSet mockResultSet = mock(ResultSet.class);
		when(mockResultSet.next()).thenReturn(true).thenReturn(false);
		when(mockResultSet.getInt("computer.id")).thenReturn(1);
		when(mockResultSet.getString("computer.name")).thenReturn("WR7");
		when(mockResultSet.getDate("introduced")).thenReturn(null);
		when(mockResultSet.getDate("discontinued")).thenReturn(null);
		when(mockResultSet.getInt("company_id")).thenReturn(0);
		when(mockResultSet.getString("company.name")).thenReturn(null);
		LinkedList<Computer> computerList = MapperComputer.getInstance().fromResultSetToComputer(mockResultSet);
		assertEquals(1, computerList.size());
		Computer computer = computerList.getFirst();
		assertEquals(1, computer.getId());
		assertEquals("WR7", computer.getName());
		assertEquals(null, computer.getIntroduced());
		assertEquals(null, computer.getDiscontinued());
		assertEquals(null, computer.getCompany());
		verify(mockResultSet, times(2)).next();
		verify(mockResultSet, times(1)).getInt("computer.id");
		verify(mockResultSet, times(1)).getString("computer.name");
		verify(mockResultSet, times(1)).getDate("introduced");
		verify(mockResultSet, times(1)).getDate("discontinued");
		verify(mockResultSet, times(1)).getInt("company_id");
		verify(mockResultSet, times(1)).getString("company.name");
	}

	@Test
	void testMapToComputerShouldReturnAnEmptyListOfComputers() {
		ResultSet mockResultSet = mock(ResultSet.class);
		try {
			when(mockResultSet.next()).thenReturn(false);
			LinkedList<Computer> computerList = MapperComputer.getInstance().fromResultSetToComputer(mockResultSet);
			assertEquals(0, computerList.size());
			verify(mockResultSet, times(1)).next();
		} catch (SQLException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testMapToComputerShouldThrowAnException() {
		assertThrows(MapperException.class, () -> {
			MapperComputer.getInstance().fromResultSetToComputer(null);
		});
	}

	@Test
	void testMapToIntShouldReturn0() {
		ResultSet mockResultSet = mock(ResultSet.class);
		try {
			when(mockResultSet.next()).thenReturn(true);
			when(mockResultSet.getInt("elements")).thenReturn(0);
			int numComputer = MapperComputer.getInstance().fromResultSetToInt(mockResultSet);
			assertEquals(0, numComputer);
			verify(mockResultSet, times(1)).next();
			verify(mockResultSet, times(1)).getInt("elements");
		} catch (SQLException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testMapToIntShouldReturn50() {
		ResultSet mockResultSet = mock(ResultSet.class);
		try {
			when(mockResultSet.next()).thenReturn(true);
			when(mockResultSet.getInt("elements")).thenReturn(50);
			int numComputer = MapperComputer.getInstance().fromResultSetToInt(mockResultSet);
			assertEquals(50, numComputer);
			verify(mockResultSet, times(1)).next();
			verify(mockResultSet, times(1)).getInt("elements");
		} catch (SQLException | MapperException e) {
			fail("Should not throw an exception");
		}
	}

	@Test
	void testMapToIntShouldThrowAnException() {
		ResultSet mockResultSet = mock(ResultSet.class);
		try {
			when(mockResultSet.next()).thenReturn(false);
			assertThrows(MapperException.class, () -> {
				MapperComputer.getInstance().fromResultSetToInt(mockResultSet);
			});
			verify(mockResultSet, times(1)).next();
		} catch (SQLException e) {
			fail("Should not throw an SQLException");
		}
	}

	@Test
	void test2MapToIntShouldThrowAnException() {
		assertThrows(MapperException.class, () -> {
			MapperComputer.getInstance().fromResultSetToInt(null);
		});
	}

}
