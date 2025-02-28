package com.danielremsburg.jaffolding.controller;

import com.danielremsburg.jaffolding.model.SalesData;
import com.danielremsburg.jaffolding.repository.SalesDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SalesDataController {
    
    @Autowired
    private SalesDataRepository salesDataRepository;
    
    @GetMapping("/sales")
    public List<SalesData> getAllSales() {
        return salesDataRepository.findAll();
    }
    
    @GetMapping("/sales/{id}")
    public ResponseEntity<SalesData> getSaleById(@PathVariable Long id) {
        Optional<SalesData> sale = salesDataRepository.findById(id);
        return sale.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping("/sales")
    public SalesData createSale(@RequestBody SalesData salesData) {
        return salesDataRepository.save(salesData);
    }
    
    @PutMapping("/sales/{id}")
    public ResponseEntity<SalesData> updateSale(@PathVariable Long id, @RequestBody SalesData salesData) {
        Optional<SalesData> existingSale = salesDataRepository.findById(id);
        
        if (existingSale.isPresent()) {
            SalesData updatedSale = existingSale.get();
            updatedSale.setProduct(salesData.getProduct());
            updatedSale.setCategory(salesData.getCategory());
            updatedSale.setSales(salesData.getSales());
            updatedSale.setRevenue(salesData.getRevenue());
            updatedSale.setMonth(salesData.getMonth());
            
            return ResponseEntity.ok(salesDataRepository.save(updatedSale));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/sales/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        Optional<SalesData> sale = salesDataRepository.findById(id);
        
        if (sale.isPresent()) {
            salesDataRepository.delete(sale.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/categories")
    public List<String> getCategories() {
        return Arrays.asList("Electronics", "Accessories", "Software", "Services");
    }
    
    @GetMapping("/products")
    public List<String> getProducts() {
        return Arrays.asList("Laptop", "Smartphone", "Headphones", "Monitor", "Keyboard", "Mouse", "Tablet", "Printer");
    }
    
    @GetMapping("/months")
    public List<String> getMonths() {
        return Arrays.asList("January", "February", "March", "April", "May", "June", 
                            "July", "August", "September", "October", "November", "December");
    }
}