package com.danielremsburg.jaffolding;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application configuration.
 * This class is renamed to avoid conflict with the annotation.
 */
@SpringBootApplication
public class JaffoldingSpringBootApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(JaffoldingSpringBootApplication.class, args);
    }
}