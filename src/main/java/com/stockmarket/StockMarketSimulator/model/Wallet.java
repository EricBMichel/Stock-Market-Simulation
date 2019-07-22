package com.stockmarket.StockMarketSimulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class Wallet {
	
	@Getter @Setter
	private List<Share> shares; // holds all shares of a wallet
	
	@Getter @Setter
	private Map<Integer, Integer> companiesShares; // maps companyID->amount of shares. 
	
	/**
	 * Prints out a formated list of a Wallet's details
	 */
	public void getWalletDetails() {
		
		System.out.println("WALLET DETAILS: \t");
		System.out.println("\tShares: ");
		for(int i=0;i<this.getShares().size();i++) {
			System.out.print("\t"+(i+1)+" Company ID: "+this.getShares().get(i).getCompanyId());
			System.out.printf("\t" + "\t$ %.2f",this.getShares().get(i).getPrice());
			System.out.print("\t\t"+this.getShares().get(i).getDateSold());
			System.out.println();
		};
	}
	
	public Wallet() {
		this.shares = new ArrayList<>();
		this.companiesShares = new HashMap<>();
	}
	
	
	


}

