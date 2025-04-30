package edu.panchoshna.lab5.request;/*
    @author User
    @project lab5
    @class PassengerCreateRequest
    @version 1.0.0
    @since 30.04.2025 - 22.38 
*/

public record PassengerCreateRequest (String firstName, String lastName, String phoneNumber, String email, int age){
}
