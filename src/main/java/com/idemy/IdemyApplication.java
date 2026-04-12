package com.idemy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class IdemyApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdemyApplication.class, args);
    }

}
