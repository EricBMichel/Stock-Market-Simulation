package com.stockmarket.StockMarketSimulator.exception;

/**
 * Exception to be thrown when a company sells a non-existing share.
 * @author Gustavo Lessa
 *
 */
public class CompanyOutOfSharesException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CompanyOutOfSharesException(String errorMessage) {
		super(errorMessage);
	}

}
