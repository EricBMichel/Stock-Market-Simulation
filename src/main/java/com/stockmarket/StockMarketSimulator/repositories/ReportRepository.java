package com.stockmarket.StockMarketSimulator.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.stockmarket.StockMarketSimulator.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
	

}
