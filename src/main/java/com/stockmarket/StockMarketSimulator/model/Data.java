package com.stockmarket.StockMarketSimulator.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
public class Data {
	
	private @Getter @Setter List<Company> companies;
	private @Getter @Setter List<Investor> investors;
	private @Getter @Setter List<Transaction> transactions;
	private @Getter @Setter Map<Integer, Company> companiesMap;
	private @Getter @Setter Map<Integer, Investor> investorsMap;
	
	public Data() {
		super();
		this.companies = new ArrayList<>();
		this.investors = new ArrayList<>();
		this.transactions = new ArrayList<>();
		this.companiesMap = new HashMap<>();
		this.investorsMap = new HashMap<>();
	}
	
	/**
	 * A method to round of all monetary values
	 * @param value the amount to round off
	 * @param places the amount of 0s after the number
	 * @return the newly rounded value
	 */
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	


}
