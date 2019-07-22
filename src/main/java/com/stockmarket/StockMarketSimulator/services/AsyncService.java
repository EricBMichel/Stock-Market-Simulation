package com.stockmarket.StockMarketSimulator.services;

import java.util.concurrent.Future;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class AsyncService{

	@Autowired
	private SimulationService simulationService;

	@Autowired
	private CompanyService companyService; 

	@Autowired
	private InvestorService investorService;


	@Async 
	public Future<String> genComapanies () {
		companyService.populateCompanies();
		return null;	
	}

	@Async
	public Future<String> genInvestors () {	
		investorService.populateInvestors();
		return null;	
	}

	@Async
	public Future<String> restartThread(){
		simulationService.restart();
		return null;
	}


	@Async
	public Future<String> asyncSaveInvestors () {	
		investorService.updateInvestors();
		return null;	
	}

	@Async
	public Future<String> asyncSaveCompanies () {	
		companyService.updateCompanies();
		return null;	
	}



}
