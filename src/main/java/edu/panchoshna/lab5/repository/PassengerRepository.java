package edu.panchoshna.lab5.repository;/*
    @author User
    @project lab5
    @class PassengerRepository
    @version 1.0.0
    @since 14.04.2025 - 23.09 
*/

import edu.panchoshna.lab5.model.Passenger;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends MongoRepository<Passenger, String> {
}
