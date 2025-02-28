package com.danielremsburg.jaffolding.config;

import com.danielremsburg.jaffolding.model.SalesData;
import com.danielremsburg.jaffolding.repository.SalesDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private SalesDataRepository salesDataRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize with sample data if the repository is empty
        if (salesDataRepository.count() == 0) {
            salesDataRepository.save(new SalesData("Laptop", "Electronics", 120, 120000, "January"));
            salesDataRepository.save(new SalesData("Smartphone", "Electronics", 200, 100000, "January"));
            salesDataRepository.save(new SalesData("Headphones", "Accessories", 150, 15000, "January"));
            salesDataRepository.save(new SalesData("Monitor", "Electronics", 80, 24000, "January"));
            salesDataRepository.save(new SalesData("Keyboard", "Accessories", 100, 5000, "January"));
            salesDataRepository.save(new SalesData("Laptop", "Electronics", 130, 130000, "February"));
            salesDataRepository.save(new SalesData("Smartphone", "Electronics", 180, 90000, "February"));
            salesDataRepository.save(new SalesData("Headphones", "Accessories", 170, 17000, "February"));
            salesDataRepository.save(new SalesData("Monitor", "Electronics", 85, 25500, "February"));
            salesDataRepository.save(new SalesData("Keyboard", "Accessories", 110, 5500, "February"));
        }
    }
}