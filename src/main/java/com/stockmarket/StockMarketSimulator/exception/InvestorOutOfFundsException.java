package com.stockmarket.StockMarketSimulator.exception;

public class InvestorOutOfFundsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6468896000823529563L;

	public InvestorOutOfFundsException(String errorMessage) {
		super(errorMessage);
	}
	
}
