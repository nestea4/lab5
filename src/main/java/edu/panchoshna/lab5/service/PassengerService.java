package edu.panchoshna.lab5.service;/*
    @author User
    @project lab5
    @class PassengerService
    @version 1.0.0
    @since 14.04.2025 - 23.14 
*/

import edu.panchoshna.lab5.model.Passenger;
import edu.panchoshna.lab5.repository.PassengerRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Passenger update(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    public void deleteById(String id) {
        passengerRepository.deleteById(id);
    }
}
