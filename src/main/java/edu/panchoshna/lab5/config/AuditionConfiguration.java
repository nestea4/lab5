package edu.panchoshna.lab5.config;/*
    @author User
    @project lab5
    @class AuditionConfiguration
    @version 1.0.0
    @since 24.04.2025 - 00.44 
*/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@Configuration
public class AuditionConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }
}