package com.stockmarket.StockMarketSimulator.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.stockmarket.StockMarketSimulator.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
	

}
