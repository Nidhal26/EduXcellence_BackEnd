package com.EduXcellence.EduXcellenceBackEnd.Security;

import com.EduXcellence.EduXcellenceBackEnd.Service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.Provider;

@Configuration
public class AppConfig {
    private MongoTemplate mongoTemplate;
    @Bean
    public ServiceAdministrateur serviceAdministrateur() {
        return new ServiceAdministrateur();
    }
    @Bean
    public ServiceAuthentification serviceAuthentification() {
        return new ServiceAuthentification(mongoTemplate);
    }
    @Bean
    public ServiceParticipant serviceParticipant(){
        return new ServiceParticipant();
    }

    @Bean
    public ServiceFormateur serviceFormateur() {
        return new ServiceFormateur();
    }

    @Bean
    public ServiceFormation serviceFormation() {
        return new ServiceFormation();
    }

    @Bean
    public ServicePayement ServicePayement() {
        return new ServicePayement();
    }

    @Bean
    public ServiceEvaluation ServiceEvaluation() {
        return new ServiceEvaluation();
    }

    @Bean
    public ServiceAttestation ServiceAttestation() {
        return new ServiceAttestation();
    }

    @Bean
    public AuthenticationFilter authenticationFilter() { return new AuthenticationFilter(); }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
