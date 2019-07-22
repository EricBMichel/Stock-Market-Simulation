package com.stockmarket.StockMarketSimulator.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stockmarket.StockMarketSimulator.model.Investor;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, Integer>{
	

}