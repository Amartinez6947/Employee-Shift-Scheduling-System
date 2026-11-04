package com.example.shiftscheduler;

import com.example.shiftscheduler.entity.Employee;
import com.example.shiftscheduler.entity.Shift;
import com.example.shiftscheduler.repository.EmployeeRepository;
import com.example.shiftscheduler.repository.ShiftRepository;
import com.example.shiftscheduler.service.ConflictDetector;
import com.example.shiftscheduler.service.ShiftService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShiftSchedulerApplicationTests {

	@Mock
	private ShiftRepository shiftRepository;

	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private ConflictDetector conflictDetector;

	@InjectMocks
	private ShiftService shiftService;

	private Employee employee;

	@BeforeEach
	void setUp() {
		employee = new Employee();
		employee.setEmployeeID(1);
		employee.setFirstName("Jordan");
		employee.setLastName("Johnson");
	}

	@Test
	void contextLoads() {
		assertNotNull(shiftService);
	}

	@Test
	void createShiftSuccessfully() {

		LocalDate date = LocalDate.of(2026, 11, 9);
		LocalTime start = LocalTime.of(10, 0);
		LocalTime end = LocalTime.of(12, 0);

		when(employeeRepository.findById(1))
				.thenReturn(Optional.of(employee));

		when(conflictDetector.checkAvailabilityConflict(any(Shift.class)))
				.thenReturn(false);

		when(conflictDetector.checkShiftOverlap(any(Shift.class)))
				.thenReturn(false);

		when(shiftRepository.save(any(Shift.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		Shift result = shiftService.createShift(1, date, start, end);

		assertNotNull(result);
		assertEquals(date, result.getDate());
		assertEquals(start, result.getStartTime());
		assertEquals(end, result.getEndTime());
		assertEquals(employee, result.getEmployee());

		verify(shiftRepository).save(any(Shift.class));
	}

	@Test
	void rejectShiftWhenEndTimeIsBeforeStartTime() {

		LocalDate date = LocalDate.of(2026, 11, 9);

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> shiftService.createShift(
						1,
						date,
						LocalTime.of(16, 0),
						LocalTime.of(14, 0)
				)
		);

		assertEquals(
				"End time must be later than start time.",
				exception.getMessage()
		);

		verify(shiftRepository, never()).save(any(Shift.class));
	}

	@Test
	void rejectShiftWhenAvailabilityConflictExists() {

		when(employeeRepository.findById(1))
				.thenReturn(Optional.of(employee));

		when(conflictDetector.checkAvailabilityConflict(any(Shift.class)))
				.thenReturn(true);

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> shiftService.createShift(
						1,
						LocalDate.of(2026, 11, 9),
						LocalTime.of(15, 0),
						LocalTime.of(18, 0)
				)
		);

		assertEquals(
				"Shift conflicts with employee availability.",
				exception.getMessage()
		);

		verify(shiftRepository, never()).save(any(Shift.class));
	}

	@Test
	void rejectShiftWhenOverlapExists() {

		when(employeeRepository.findById(1))
				.thenReturn(Optional.of(employee));

		when(conflictDetector.checkAvailabilityConflict(any(Shift.class)))
				.thenReturn(false);

		when(conflictDetector.checkShiftOverlap(any(Shift.class)))
				.thenReturn(true);

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> shiftService.createShift(
						1,
						LocalDate.of(2026, 11, 9),
						LocalTime.of(12, 0),
						LocalTime.of(15, 0)
				)
		);

		assertEquals(
				"Shift overlaps with another scheduled shift.",
				exception.getMessage()
		);

		verify(shiftRepository, never()).save(any(Shift.class));
	}

	@Test
	void allowConsecutiveShifts() {

		when(employeeRepository.findById(1))
				.thenReturn(Optional.of(employee));

		when(conflictDetector.checkAvailabilityConflict(any(Shift.class)))
				.thenReturn(false);

		when(conflictDetector.checkShiftOverlap(any(Shift.class)))
				.thenReturn(false);

		when(shiftRepository.save(any(Shift.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		Shift result = shiftService.createShift(
				1,
				LocalDate.of(2026, 11, 9),
				LocalTime.of(14, 0),
				LocalTime.of(16, 0)
		);

		assertNotNull(result);
		assertEquals(LocalTime.of(14, 0), result.getStartTime());
		assertEquals(LocalTime.of(16, 0), result.getEndTime());

		verify(shiftRepository).save(any(Shift.class));
	}

	@Test
	void rejectShiftWhenEmployeeDoesNotExist() {

		when(employeeRepository.findById(99))
				.thenReturn(Optional.empty());

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> shiftService.createShift(
						99,
						LocalDate.of(2026, 11, 9),
						LocalTime.of(10, 0),
						LocalTime.of(12, 0)
				)
		);

		assertEquals("Employee not found.", exception.getMessage());

		verify(shiftRepository, never()).save(any(Shift.class));
	}

	@Test
	void updateShiftSuccessfully() {

		Shift existingShift = new Shift(
				employee,
				LocalDate.of(2026, 11, 9),
				LocalTime.of(10, 0),
				LocalTime.of(12, 0)
		);

		existingShift.setShiftID(1);

		when(shiftRepository.findById(1))
				.thenReturn(Optional.of(existingShift));

		when(employeeRepository.findById(1))
				.thenReturn(Optional.of(employee));

		when(conflictDetector.checkAvailabilityConflict(any(Shift.class)))
				.thenReturn(false);

		when(conflictDetector.checkShiftOverlap(any(Shift.class)))
				.thenReturn(false);

		when(shiftRepository.save(any(Shift.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		Shift result = shiftService.updateShift(
				1,
				1,
				LocalDate.of(2026, 11, 9),
				LocalTime.of(12, 0),
				LocalTime.of(14, 0)
		);

		assertNotNull(result);
		assertEquals(LocalTime.of(12, 0), result.getStartTime());
		assertEquals(LocalTime.of(14, 0), result.getEndTime());

		verify(shiftRepository).save(existingShift);
	}

	@Test
	void rejectUpdateWhenEndTimeIsBeforeStartTime() {

		Shift existingShift = new Shift(
				employee,
				LocalDate.of(2026, 11, 9),
				LocalTime.of(10, 0),
				LocalTime.of(12, 0)
		);

		existingShift.setShiftID(1);

		when(shiftRepository.findById(1))
				.thenReturn(Optional.of(existingShift));

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> shiftService.updateShift(
						1,
						1,
						LocalDate.of(2026, 11, 9),
						LocalTime.of(16, 0),
						LocalTime.of(14, 0)
				)
		);

		assertEquals(
				"End time must be later than start time.",
				exception.getMessage()
		);

		verify(shiftRepository, never()).save(any(Shift.class));
	}

	@Test
	void rejectUpdateWhenAvailabilityConflictExists() {

		Shift existingShift = new Shift(
				employee,
				LocalDate.of(2026, 11, 9),
				LocalTime.of(10, 0),
				LocalTime.of(12, 0)
		);

		existingShift.setShiftID(1);

		when(shiftRepository.findById(1))
				.thenReturn(Optional.of(existingShift));

		when(employeeRepository.findById(1))
				.thenReturn(Optional.of(employee));

		when(conflictDetector.checkAvailabilityConflict(any(Shift.class)))
				.thenReturn(true);

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> shiftService.updateShift(
						1,
						1,
						LocalDate.of(2026, 11, 9),
						LocalTime.of(15, 0),
						LocalTime.of(18, 0)
				)
		);

		assertEquals(
				"Updated shift conflicts with employee availability.",
				exception.getMessage()
		);

		verify(shiftRepository, never()).save(any(Shift.class));
	}

	@Test
	void deleteShiftSuccessfully() {

		when(shiftRepository.existsById(1))
				.thenReturn(true);

		shiftService.deleteShift(1);

		verify(shiftRepository).deleteById(1);
	}
}
