package com.stockmarket.StockMarketSimulator.exception;

/**
 * 
 * @author gustavolessa1
 *
 */
public class InvestorHasInvestedInAllCompaniesException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6468896000823529563L;

	public InvestorHasInvestedInAllCompaniesException(String errorMessage) {
		super(errorMessage);
	}
	
}
