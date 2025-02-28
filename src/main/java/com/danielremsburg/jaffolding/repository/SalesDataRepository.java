package com.danielremsburg.jaffolding.repository;

import com.danielremsburg.jaffolding.model.SalesData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesDataRepository extends JpaRepository<SalesData, Long> {
    List<SalesData> findByCategory(String category);
    List<SalesData> findByProduct(String product);
    List<SalesData> findByMonth(String month);
}