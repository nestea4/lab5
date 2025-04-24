package edu.panchoshna.lab5.config;/*
    @author User
    @project lab5
    @class AuditorAwareImpl
    @version 1.0.0
    @since 24.04.2025 - 00.45 
*/

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        //return Optional.of("admin");
        return Optional.of(System.getProperty("user.name"));
    }
}
