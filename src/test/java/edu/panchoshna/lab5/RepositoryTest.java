package edu.panchoshna.lab5;/*
    @author User
    @project lab5
    @class RepositoryTest
    @version 1.0.0
    @since 24.04.2025 - 00.48 
*/

import edu.panchoshna.lab5.model.Passenger;
import edu.panchoshna.lab5.repository.PassengerRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataMongoTest
public class RepositoryTest {

    @Autowired
    PassengerRepository underTest;

    @BeforeAll
    void beforeAll() {}

    @BeforeEach
    void setUp() {
        Passenger nastya = new Passenger("1", "Настя", "Панчошна", "+380501112233", "mclovin@gmail.com", 20);
        Passenger dariia = new Passenger("2", "Дарія", "Литвинюк", "+380672223344", "dariia@gmail.com", 19);
        Passenger vitalina = new Passenger("3", "Віталіна", "Корчова", "+380993334455", "vita@gmail.com", 20);
        underTest.saveAll(List.of(nastya, dariia, vitalina));
    }

    @AfterEach
    void tearDown() {
        List<Passenger> passengersToDelete = underTest.findAll();
        underTest.deleteAll(passengersToDelete);
    }

    @AfterAll
    void afterAll() {}

    @Test
    void testSetShouldContain_3_RecordsToTest() {
        List<Passenger> testPassengers = underTest.findAll();
        assertEquals(3, testPassengers.size());
    }

    @Test
    void shouldGiveIdForNewRecord() {
        // given
        Passenger oleksandr = new Passenger("Олександр", "Іваненко", "+380661234567", "oleks@gmail.com", 22);

        // when
        underTest.save(oleksandr);
        Passenger passengerFromDb = underTest.findAll().stream()
                .filter(passenger -> passenger.getFirstName().equals("Олександр"))
                .findFirst().orElse(null);

        // then
        assertFalse(passengerFromDb.getId() == oleksandr.getId());
        assertNotNull(passengerFromDb);
        assertNotNull(passengerFromDb.getId());
        assertFalse(passengerFromDb.getId().isEmpty());
        assertEquals(24, passengerFromDb.getId().length());
    }

    @Test
    void whenRecordHasIdThenItIsPossibleToSave() {
        // given
        Passenger ivan = Passenger.builder()
                .id("test-id")
                .firstName("Іван")
                .lastName("Петренко")
                .phoneNumber("+380991234567")
                .email("ivan@gmail.com")
                .age(25)
                .build();

        // when
        underTest.save(ivan);
        Optional<Passenger> passengerFromDb = underTest.findById("test-id");

        // then
        assertTrue(passengerFromDb.isPresent());
        assertEquals("Іван", passengerFromDb.get().getFirstName());
    }

    @Test
    void shouldUpdateExistingRecord() {
        Passenger nastya = underTest.findById("1").orElse(null);
        assertNotNull(nastya);

        nastya.setAge(21);
        nastya.setEmail("updated-email@gmail.com");
        underTest.save(nastya);
        Passenger updatedNastya = underTest.findById("1").orElse(null);

        assertNotNull(updatedNastya);
        assertEquals(21, updatedNastya.getAge());
        assertEquals("updated-email@gmail.com", updatedNastya.getEmail());
    }

    @Test
    void shouldDeleteRecordById() {
        assertTrue(underTest.findById("3").isPresent());

        underTest.deleteById("3");

        assertFalse(underTest.findById("3").isPresent());
        assertEquals(2, underTest.count());
    }

    @Test
    void shouldFindAllRecordsWithSorting() {
        List<Passenger> sortedByAgeAsc = underTest.findAll(Sort.by(Sort.Direction.ASC, "age"));

        assertEquals(3, sortedByAgeAsc.size());
        assertEquals(19, sortedByAgeAsc.get(0).getAge());
        assertEquals("Дарія", sortedByAgeAsc.get(0).getFirstName());
    }

    @Test
    void shouldFindByExample() {
        Passenger example = new Passenger();
        example.setAge(20);

        List<Passenger> passengers = underTest.findAll(Example.of(example));

        assertEquals(2, passengers.size());
        assertTrue(passengers.stream().allMatch(p -> p.getAge() == 20));
    }

    @Test
    void shouldDeleteAllInBatch() {
        assertEquals(3, underTest.count());

        underTest.deleteAll();

        assertEquals(0, underTest.count());
    }

    @Test
    void shouldCountRecords() {
        long count = underTest.count();

        assertEquals(3, count);

        Passenger newPassenger = new Passenger(null, "Тетяна", "Коваленко", "+380671234567", "tanya@gmail.com", 22);
        underTest.save(newPassenger);

        assertEquals(4, underTest.count());
    }

    @Test
    void shouldCheckExistenceById() {
        assertTrue(underTest.existsById("1"));

        underTest.deleteById("1");

        assertFalse(underTest.existsById("1"));
        assertTrue(underTest.existsById("2"));
    }
}
