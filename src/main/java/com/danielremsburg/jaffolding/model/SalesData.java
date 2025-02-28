package com.danielremsburg.jaffolding.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SalesData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String product;
    private String category;
    private int sales;
    private double revenue;
    private String month;
    
    public SalesData() {
    }
    
    public SalesData(String product, String category, int sales, double revenue, String month) {
        this.product = product;
        this.category = category;
        this.sales = sales;
        this.revenue = revenue;
        this.month = month;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getProduct() {
        return product;
    }
    
    public void setProduct(String product) {
        this.product = product;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public int getSales() {
        return sales;
    }
    
    public void setSales(int sales) {
        this.sales = sales;
    }
    
    public double getRevenue() {
        return revenue;
    }
    
    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
    
    public String getMonth() {
        return month;
    }
    
    public void setMonth(String month) {
        this.month = month;
    }
}