package edu.panchoshna.lab5.controller;/*
    @author User
    @project lab5
    @class PassengerRestController
    @version 1.0.0
    @since 14.04.2025 - 23.17 
*/

import edu.panchoshna.lab5.model.Passenger;
import edu.panchoshna.lab5.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/passengers/")
@RequiredArgsConstructor
public class PassengerRestController {
    private final PassengerService passengerService;

    // read all
    @GetMapping
    public List<Passenger> showAll() {
        return passengerService.getAll();
    }

    // read one
    @GetMapping("{id}")
    public Passenger showOneById(@PathVariable String id) {
        return passengerService.getById(id);
    }

    @PostMapping
    public Passenger insert(@RequestBody Passenger passenger) {
        return passengerService.create(passenger);
    }

    @PutMapping
    public Passenger edit(@RequestBody Passenger passenger) {
        return passengerService.update(passenger);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        passengerService.deleteById(id);
    }
}
