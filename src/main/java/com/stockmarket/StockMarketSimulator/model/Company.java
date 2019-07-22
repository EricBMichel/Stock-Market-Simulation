package com.stockmarket.StockMarketSimulator.model;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


@Entity
public class Company {
	
//	@Id
//	@GeneratedValue
	@Id
	private final int id;
	public static int lastId = 0;
	private String name;
	private double sharePrice;
	@Column(scale=2)
	private double capital;
	private int sharesSold;
	private int initialShares;
	private double initialCapital;
	private double initialSharePrice;
	
	@Transient
	private boolean hasSoldShare;
	
	@Transient
	private List<Share> shares;

	private Company(CompanyBuilder builder) {
		super();
		this.id = ++lastId;
		this.name = builder.name;
		this.sharePrice = builder.sharePrice;
		this.capital = builder.capital;
		this.sharesSold = builder.sharesSold;
		this.hasSoldShare = builder.hasSoldShare;

		this.initialCapital = builder.initialCapital;
		this.initialSharePrice = builder.initialSharePrice;
		this.initialShares = builder.initialShares;
		this.shares = new ArrayList<>(); 
		
		
		for(int x = 0; x < builder.shares; x++) 
			shares.add(new Share(this.id, this.sharePrice)); 
		//ipo(builder.shares); // Initial Public Offering -> to create the shares
		
	}
	
	private Company() {
		super();
		this.id = 0;
		this.name = "";
		this.sharePrice = 0;
		this.capital = 0;
		this.sharesSold = 0;
		this.hasSoldShare = false;

		this.initialCapital = 0;
		this.initialSharePrice = 0;
		this.initialShares = 0;
		this.shares = new ArrayList<>();
		
		ipo(this.getShares().size()); // Initial Public Offering -> to create the shares

	}
	
	/**
	 * Sets the initial public offering for a company (amount of shares and price)
	 * @param numberOfShares the number of shares a company starts with
	 */
	private void ipo(int numberOfShares) {
	//	shares = new ArrayList<>(); // new list to hold the Share objects created
		
		for(int x = 0; x < numberOfShares; x++) 
			shares.add(new Share(this.id, this.sharePrice)); // create the chosen number of shares
	}
	
	

	/**
	 * @return the initialShares
	 */
	public int getInitialShares() {
		return initialShares;
	}

	/**
	 * @return the initialCapital
	 */
	public double getInitialCapital() {
		return initialCapital;
	}

	/**
	 * @return the initialSharePrice
	 */
	public double getInitialSharePrice() {
		return initialSharePrice;
	}

	/**
	 * @return the company id
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @return the company name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the list of shares
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return the total number of shares available
	 */
	public List<Share> getShares() {
		return shares;
	}
	
	/**
	 * 
	 * @return the share price
	 */
	public int getNumberOfSharesAvailable() {
		return shares.size();
	}

	/**
	 * 
	 * @param name the name to set
	 */
	public double getSharePrice() {
		return sharePrice;
	}

	/**
	 * 
	 * @param sharePrice the share price to set
	 */
	public void setSharePrice(double sharePrice) {
		this.sharePrice = sharePrice;
	}
	
	/**
	 * 
	 * @param soldShare the sold share if sold
	 */
	public void setHasSoldShare(boolean soldShare) {
		this.hasSoldShare = soldShare;
	}

	/**
	 * 
	 * @return the capital of the company
	 */
	public double getCapital() {
		return capital;
	}

	/**
	 * 
	 * @return the total shares sold by a company
	 */
	public int getSharesSold() {
		return sharesSold;
	}
	

	/**
	 * 
	 * @return if a company has sold a share
	 */
	public boolean getHasSoldShare() {
		return hasSoldShare;
	}
	
	/**
	 * Prints a formated list of company details
	 */
	public void getCompanyDetails() {
		DecimalFormat df = new DecimalFormat("#.00");
		
		System.out.println("----------COMPANY----------");
		System.out.println("COMPANY ID: \t" + this.getId());
		System.out.println("COMPANY NAME: \t" + this.getName());
		System.out.println("CAPITAL: $" + df.format(this.getCapital()));
		System.out.println("SHARE DETAILS");
		System.out.println("\tAvailable: " + this.getNumberOfSharesAvailable());
		System.out.println("\tSold: " + this.getSharesSold());
		System.out.println("\tPrice: $" + df.format(this.getSharePrice()));	
		System.out.println();
	}

	/**
	 * Increases the total shares sold
	 */
	public void incrementSharesSold() {
		this.sharesSold++;
	}
	
	/**
	 *
	 * @param amount the amount to increase the total capital
	 */
	public void incrementCapital(double amount) {
	    BigDecimal bd = new BigDecimal(this.capital+amount);
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
		this.capital = bd.doubleValue();;
	}

	
	public static class CompanyBuilder{
		private String name;
		private int shares;
		private double sharePrice;
		private double capital;
		private int sharesSold;
		private boolean hasSoldShare;
		private int initialShares;
		private double initialCapital;
		private double initialSharePrice;
		
		public CompanyBuilder(String name) {
			super();
			this.name = name;
			this.sharePrice = 0;
			this.capital = 0;
			this.sharesSold = 0;
			this.hasSoldShare = false;
			this.initialCapital = 0;
			this.initialSharePrice = 0;
			this.initialShares = 0;
		}


		/**
		 * @param name the name to set
		 */
		public CompanyBuilder setName(String name) {
			this.name = name;
			return this;
		}


		/**
		 * @param shares the shares to set
		 */
		public CompanyBuilder setShares(int shares) {
			this.initialShares = shares;
			this.shares = shares;
			return this;
		}


		/**
		 * @param sharePrice the sharePrice to set
		 */
		public CompanyBuilder setSharePrice(double sharePrice) {
			this.initialSharePrice = sharePrice;
			this.sharePrice = sharePrice;
			return this;
		}


		/**
		 * @param capital the capital to set
		 */
		public CompanyBuilder setCapital(double capital) {
			this.initialCapital = capital;
			this.capital = capital;
			return this;
		}


		/**
		 * @param sharesSold the sharesSold to set
		 */
		public CompanyBuilder setSharesSold(int sharesSold) {
			this.sharesSold = sharesSold;
			return this;
		}
		
		/**
		 * @param sold set the hasSoldShare
		 */
		public CompanyBuilder setHasSoldShare(boolean sold) {
			this.hasSoldShare = sold;
			return this;
		}
	
		/**
		 * 
		 * @param initialCapital set the initialCapital
		 */
		public CompanyBuilder setInitialCapital(double initialCapital) {
			this.initialCapital = initialCapital;
			return this;
		}
		
		/**
		 * 
		 * @param initialSharePrice set the initialSharePrice
		 */
		public CompanyBuilder setInitialInitialSharePrice(double initialSharePrice) {
			this.initialSharePrice = initialSharePrice;
			return this;
		}
		
		/**
		 * 
		 * @param initialShares set the total initialShares
		 */
		public CompanyBuilder setInitialShares(int initialShares) {
			this.initialShares = initialShares;
			return this;
		}
		
		/**
		 * 
		 * @return a newly initialized company
		 */
		public Company build() {
			return new Company(this);
		}
		
		
		
	}
	
}