package edu.panchoshna.lab5.service;/*
    @author User
    @project lab5
    @class PassengerService
    @version 1.0.0
    @since 14.04.2025 - 23.14 
*/

import edu.panchoshna.lab5.model.Passenger;
import edu.panchoshna.lab5.repository.PassengerRepository;
import edu.panchoshna.lab5.request.PassengerCreateRequest;
import edu.panchoshna.lab5.request.PassengerUpdateRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerService {
    private final PassengerRepository passengerRepository;
    private List<Passenger> passengers = new ArrayList<>();

    {
        passengers.add(new Passenger("1", "Настя", "Панчошна", "+380501112233", "mclovin@gmail.com", 20));
        passengers.add(new Passenger("2", "Дарія", "Литвинюк", "+380672223344", "dariia@gmail.com", 19));
        passengers.add(new Passenger("3", "Віталіна", "Корчова", "+380993334455", "vita@gmail.com", 20));
    }

    @PostConstruct
    void init() {
        passengerRepository.deleteAll();
        passengerRepository.saveAll(passengers);
    }

    public List<Passenger> getAll() {
        return passengerRepository.findAll();
    }

    public Passenger getById(String id) {
        return passengerRepository.findById(id).orElse(null);
    }

    public Passenger create(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    public Passenger create(PassengerCreateRequest request) {
        Passenger passenger = mapToPassenger(request);
        passenger.setCreateDate(LocalDateTime.now());
        passenger.setUpdateDate(new ArrayList<LocalDateTime>());
        return passengerRepository.save(passenger);
    }

    public Passenger update(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    public Passenger update(PassengerUpdateRequest request) {
        Passenger passengerPersisted = passengerRepository.findById(request.id()).orElse(null);
        if (passengerPersisted != null) {
            List<LocalDateTime> updateDates = passengerPersisted.getUpdateDate();
            updateDates.add(LocalDateTime.now());
            Passenger passengerToUpdate =
                    Passenger.builder()
                            .id(request.id())
                            .firstName(request.firstName())
                            .lastName(request.lastName())
                            .phoneNumber(request.phoneNumber())
                            .email(request.email())
                            .age(request.age())
                            .createDate(passengerPersisted.getCreateDate())
                            .updateDate(updateDates)
                            .build();
            return passengerRepository.save(passengerToUpdate);
        }
        return null;
    }

    public void deleteById(String id) {
        passengerRepository.deleteById(id);
    }

    private Passenger mapToPassenger(PassengerCreateRequest request) {
        Passenger passenger = new Passenger(
                request.firstName(),
                request.lastName(),
                request.phoneNumber(),
                request.email(),
                request.age()
        );
        return passenger;
    }
}