package com.excilys.cdb.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class SecureInputsTest {

	@Test
	void testIsIntegerShouldReturnTrue() {
		assertTrue(SecureInputs.isInteger("1"));
	}

	@Test
	void testIsIntegerShouldReturnFalse() {
		assertFalse(SecureInputs.isInteger("Invalid input"));
		assertFalse(SecureInputs.isInteger(""));
		assertFalse(SecureInputs.isInteger(null));
	}
	
	@Test
	void testToLocalDateShouldReturnLocalDate() {
		Optional<LocalDate> date = SecureInputs.toLocalDate("2021-05-19");
		assertEquals("2021-05-19", date.get().toString());
	}
	
	@Test
	void testToLocalDateShouldNotReturnLocalDate() {
		Optional<LocalDate> date = SecureInputs.toLocalDate("Invalid input");
		assertEquals(Optional.empty(), date);
		Optional<LocalDate> date2 = SecureInputs.toLocalDate("");
		assertEquals(Optional.empty(), date2);
		Optional<LocalDate> date3 = SecureInputs.toLocalDate(null);
		assertEquals(Optional.empty(), date3);
	}
	
	@Test
	void testIsValidPageShouldReturnTrue() {
		assertTrue(SecureInputs.isValidPage("a", 2, 5));
		assertTrue(SecureInputs.isValidPage("z", 1, 5));
		assertTrue(SecureInputs.isValidPage("q", 0, 0));
	}
	
	@Test
	void testIsValidPageShouldReturnFalse() {
		assertFalse(SecureInputs.isValidPage("a", 1, 5));
		assertFalse(SecureInputs.isValidPage("z", 5, 5));
		assertFalse(SecureInputs.isValidPage("", 0, 0));
		assertFalse(SecureInputs.isValidPage("Invalid input", 0, 0));
		assertFalse(SecureInputs.isValidPage(null, 0, 0));
	}

}
