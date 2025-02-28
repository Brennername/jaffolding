package com.danielremsburg.jaffolding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class JaffoldingApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(JaffoldingApplication.class, args);
    }
    
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("mode", "Spring Boot");
        return status;
    }
}