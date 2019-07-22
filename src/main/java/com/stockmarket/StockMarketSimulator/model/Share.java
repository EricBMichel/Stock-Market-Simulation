package com.stockmarket.StockMarketSimulator.model;

import java.util.Date;

public class Share {
	
	private int companyId;
	private double price;
	private Date dateSold;
	
	public Share(int companyId, double price) {
		this.companyId = companyId;
		this.price = price;
		this.dateSold = new Date();
	}
	
	/**
	 * @return the companyId
	 */
	public int getCompanyId() {
		return this.companyId;
	}

	/**
	 * 
	 * @return the dateSold
	 */
	public Date getDateSold() { 
		return this.dateSold;
	}
	
	/**
	 * @return the price
	 */
	public double getPrice() {
		return this.price;
	}
	
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	
	

}