package edu.panchoshna.lab5;/*
    @author User
    @project lab5
    @class ServiceTests
    @version 1.0.0
    @since 30.04.2025 - 22.47 
*/

import edu.panchoshna.lab5.model.Passenger;
import edu.panchoshna.lab5.repository.PassengerRepository;
import edu.panchoshna.lab5.request.PassengerCreateRequest;
import edu.panchoshna.lab5.request.PassengerUpdateRequest;
import edu.panchoshna.lab5.service.PassengerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ServiceTests {
    @Autowired
    private PassengerRepository repository;

    @Autowired
    private PassengerService underTest;

    private PassengerCreateRequest defaultCreateRequest;

    @BeforeEach
    void setUp() {
        defaultCreateRequest = new PassengerCreateRequest(
                "Віталік", "Петров", "+380671112233", "vitalik@example.com", 30);
    }

    @AfterEach
    void tearsDown() {
        repository.deleteAll();
    }

    private Passenger createDefaultPassenger() {
        return underTest.create(defaultCreateRequest);
    }

    @Test
    void whenInsertNewPassenger_ThenPassengerIsNotNull() {
        Passenger createdPassenger = createDefaultPassenger();
        assertNotNull(createdPassenger);
    }

    @Test
    void whenInsertNewPassenger_ThenIdIsGenerated() {
        Passenger createdPassenger = createDefaultPassenger();
        assertNotNull(createdPassenger.getId());
    }

    @Test
    void whenInsertNewPassenger_ThenFirstNameIsCorrect() {
        Passenger createdPassenger = createDefaultPassenger();
        assertEquals("Віталік", createdPassenger.getFirstName());
    }

    @Test
    void whenInsertNewPassenger_ThenLastNameIsCorrect() {
        Passenger createdPassenger = createDefaultPassenger();
        assertEquals("Петров", createdPassenger.getLastName());
    }

    @Test
    void whenInsertNewPassenger_ThenPhoneNumberIsCorrect() {
        Passenger createdPassenger = createDefaultPassenger();
        assertEquals("+380671112233", createdPassenger.getPhoneNumber());
    }

    @Test
    void whenInsertNewPassenger_ThenEmailIsCorrect() {
        Passenger createdPassenger = createDefaultPassenger();
        assertEquals("vitalik@example.com", createdPassenger.getEmail());
    }

    @Test
    void whenInsertNewPassenger_ThenAgeIsCorrect() {
        Passenger createdPassenger = createDefaultPassenger();
        assertEquals(30, createdPassenger.getAge());
    }

    @Test
    void whenInsertNewPassenger_ThenCreateDateIsNotNull() {
        Passenger createdPassenger = createDefaultPassenger();
        assertNotNull(createdPassenger.getCreateDate());
    }

    @Test
    void whenInsertNewPassenger_ThenCreateDateHasCorrectType() {
        Passenger createdPassenger = createDefaultPassenger();
        assertSame(LocalDateTime.class, createdPassenger.getCreateDate().getClass());
    }

    @Test
    void whenInsertNewPassenger_ThenCreateDateIsAfterNow() {
        LocalDateTime now = LocalDateTime.now();

        Passenger createdPassenger = createDefaultPassenger();

        assertTrue(createdPassenger.getCreateDate().isAfter(now) ||
                createdPassenger.getCreateDate().equals(now));
    }

    @Test
    void whenInsertNewPassenger_ThenUpdateDateIsNotNull() {
        Passenger createdPassenger = createDefaultPassenger();

        assertNotNull(createdPassenger.getUpdateDate());
    }

    @Test
    void whenInsertNewPassenger_ThenUpdateDateHasCorrectType() {
        Passenger createdPassenger = createDefaultPassenger();
        assertSame(ArrayList.class, createdPassenger.getUpdateDate().getClass());
    }

    @Test
    void whenInsertNewPassenger_ThenUpdateDateIsEmpty() {
        Passenger createdPassenger = createDefaultPassenger();
        assertTrue(createdPassenger.getUpdateDate().isEmpty());
    }

    @Test
    void whenUpdatePassenger_ThenReturnNotNull() {
        Passenger createdPassenger = createDefaultPassenger();

        PassengerUpdateRequest updateRequest = new PassengerUpdateRequest(
                createdPassenger.getId(), "Віталік", "Сидоров", "+380671112233",
                "vitalik.sidorov@example.com", 31);

        Passenger updatedPassenger = underTest.update(updateRequest);

        assertNotNull(updatedPassenger);
    }

    @Test
    void whenUpdatePassenger_ThenIdRemainsSame() {
        Passenger createdPassenger = createDefaultPassenger();
        String originalId = createdPassenger.getId();

        PassengerUpdateRequest updateRequest = new PassengerUpdateRequest(
                originalId, "Віталік", "Сидоров", "+380671112233",
                "vitalik.sidorov@example.com", 31);

        Passenger updatedPassenger = underTest.update(updateRequest);
        assertEquals(originalId, updatedPassenger.getId());
    }

    @Test
    void whenUpdatePassenger_ThenFieldsAreUpdated() {
        Passenger createdPassenger = createDefaultPassenger();

        PassengerUpdateRequest updateRequest = new PassengerUpdateRequest(
                createdPassenger.getId(), "Віталік", "Сидоров", "+380677778899",
                "vitalik.sidorov@example.com", 31);

        Passenger updatedPassenger = underTest.update(updateRequest);

        assertEquals("Сидоров", updatedPassenger.getLastName());
        assertEquals("+380677778899", updatedPassenger.getPhoneNumber());
        assertEquals("vitalik.sidorov@example.com", updatedPassenger.getEmail());
        assertEquals(31, updatedPassenger.getAge());
    }

    @Test
    void whenUpdatePassengerWithNonExistingId_ThenReturnNull() {
        String nonExistingId = "non-existing-id";
        PassengerUpdateRequest updateRequest = new PassengerUpdateRequest(
                nonExistingId, "Віталік", "Сидоров", "+380671112233",
                "vitalik.sidorov@example.com", 31);

        Passenger updatedPassenger = underTest.update(updateRequest);

        assertNull(updatedPassenger);
    }

    @Test
    void whenUpdatePassenger_ThenCreateDateRemainsSame() {
        Passenger createdPassenger = createDefaultPassenger();
        LocalDateTime originalCreateDate = createdPassenger.getCreateDate();

        PassengerUpdateRequest updateRequest = new PassengerUpdateRequest(
                createdPassenger.getId(), "Віталік", "Сидоров", "+380671112233",
                "vitalik.sidorov@example.com", 31);

        Passenger updatedPassenger = underTest.update(updateRequest);

        LocalDateTime truncatedOriginal = originalCreateDate.withNano(0);
        LocalDateTime truncatedUpdated = updatedPassenger.getCreateDate().withNano(0);

        assertEquals(truncatedOriginal, truncatedUpdated);
    }

    @Test
    void whenUpdatePassenger_ThenUpdateDateIsAdded() {
        Passenger createdPassenger = createDefaultPassenger();

        PassengerUpdateRequest updateRequest = new PassengerUpdateRequest(
                createdPassenger.getId(), "Віталік", "Сидоров", "+380671112233",
                "vitalik.sidorov@example.com", 31);

        Passenger updatedPassenger = underTest.update(updateRequest);

        assertEquals(1, updatedPassenger.getUpdateDate().size());
    }

    @Test
    void whenUpdatePassengerMultipleTimes_ThenMultipleUpdateDatesAdded() {
        Passenger createdPassenger = createDefaultPassenger();
        String passengerId = createdPassenger.getId();

        PassengerUpdateRequest firstUpdateRequest = new PassengerUpdateRequest(
                passengerId, "Віталік", "Сидоров", "+380671112233",
                "vitalik.sidorov@example.com", 31);
        underTest.update(firstUpdateRequest);

        PassengerUpdateRequest secondUpdateRequest = new PassengerUpdateRequest(
                passengerId, "Віталік", "Сидоров", "+380677778899",
                "vitalik.sidorov2@example.com", 32);

        Passenger twiceUpdatedPassenger = underTest.update(secondUpdateRequest);
        assertEquals(2, twiceUpdatedPassenger.getUpdateDate().size());
    }
}