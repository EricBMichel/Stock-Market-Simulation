package com.stockmarket.StockMarketSimulator.services;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockmarket.StockMarketSimulator.model.Company;
import com.stockmarket.StockMarketSimulator.model.Data;
import com.stockmarket.StockMarketSimulator.model.Investor;
import com.stockmarket.StockMarketSimulator.model.TradingDay;
import com.stockmarket.StockMarketSimulator.model.Transaction;
import com.stockmarket.StockMarketSimulator.view.GUI;
import com.stockmarket.StockMarketSimulator.view.View;


@Service
public class SimulationService {


	@Autowired
	private CompanyService companyService; 

	@Autowired
	private InvestorService investorService;

	@Autowired
	private AsyncService asyncService; 

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private TradingDay td;

	@Autowired
	private Data data;

	@Autowired
	private View view;

	@Autowired
	private ReportService reportService;

	@Autowired
	private GUI gui;

	/**
	 * Starts the initial simulation
	 */
	public void start() {

		view.display("Starting simulation...");
		companyService.clearCompanyTable(); //ensures that all repositories and lists are cleared
		investorService.clearInvestors();

		generateObjects(); //async method to generate the companies and investors

		td.trade(data.getCompanies(), data.getInvestors()); //run the trade

		if(td.isSimulationFinished()) { 
			gui.simulationFinished(true); //allow the buttons to be active	

			companyService.updateCompanies(); //update the repositories and lists
			investorService.updateInvestors();
		
			reportService.saveReportToDb();	//save reports to database
		}
	
	}

	/**
	 * Restarts the simulation after the initial simulation has run
	 */
	public void restart() {

		td.setSimulationFinished(false);
		transactionService.clearTransactions();

		start();

	}


	/*
	 * @Async function to generate companies at the same time
	 */
	public void generateObjects() {

		Future<String> futureResult =  asyncService.genComapanies();
		Future<String> futureResult2 = asyncService.genInvestors();

		try {
			String result = futureResult.get();
			String result2 = futureResult2.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Displays the transaction list to the user
	 * @return the list as a string
	 */
	public String displayTransaction() {
		StringBuilder sb = new StringBuilder();

		sb.append("\nCompany/Companies with Highest capital:");
		for(Transaction t : data.getTransactions()) {
			sb.append(t.toString());
		}
		return sb.toString();

	}

	/**
	 * Displays the company with the highest capital
	 * @return the list as a string
	 */
	public String highestCapital() {
		StringBuilder sb = new StringBuilder();

		sb.append("\nCompany/Companies with Highest capital:");
		for(Company c : td.getHighestCapital()) {
			sb.append("\n");
			sb.append(c.getId());
			sb.append(" - Name: "); 
			sb.append(c.getName()); 
			sb.append(" - Capital: €");
			sb.append(Data.round(c.getCapital(),2));
		}
		return sb.toString();

	}

	/**
	 * Displays the companies with the lowest capital
	 * @return the list as a string
	 */
	public String lowestCapital() {
		StringBuilder sb = new StringBuilder();

		sb.append("\nCompany/Companies with Lowest capital:");
		for(Company c : td.getLowestCapital()) {
			sb.append("\n");
			sb.append(c.getId());
			sb.append(" - Name: ");
			sb.append(c.getName());
			sb.append(" - Capital: €");
			sb.append(Data.round(c.getCapital(),2));
		}
		return sb.toString();

	}

	/**
	 * Displays the investors with the highest number of shares
	 * @return the list as a string
	 */
	public String highestNumberOfShares() {
		StringBuilder sb = new StringBuilder();

		sb.append("\nInvestor(s) with highest number of shares:");
		for(Investor i : td.getHighestNumberOfShares()) {
			sb.append("\n");
			sb.append(i.getId());
			sb.append(" - Name: ");
			sb.append(i.getName());
			sb.append(" - Shares: ");
			sb.append(i.getTotalNumberOfSharesBought());
		}
		return sb.toString();

	}
	
	/**
	 * Displays the investors with the lowest number of shares
	 * @return the list as a string
	 */
	public String lowestNumberOfShares() {
		StringBuilder sb = new StringBuilder();

		sb.append("\nInvestor(s) with lowest number of shares:");
		for(Investor i : td.getLowestNumberOfShares()) {
			sb.append("\n");
			sb.append(i.getId());
			sb.append(" - Name: ");
			sb.append(i.getName());
			sb.append(" - Shares: ");
			sb.append(i.getTotalNumberOfSharesBought());
		}
		return sb.toString();

	}

	/**
	 * Displays the investors that have invested in the most amount of companies
	 * @return the list as a string
	 */
	public String highestNumberOfCompanies() {
		StringBuilder sb = new StringBuilder();

		sb.append("\nInvestor(s) with highest number of companies:");
		for(Investor i : td.getHighestNumberOfCompanies()) {
			sb.append("\n");
			sb.append(i.getId());
			sb.append(" - Name: ");
			sb.append(i.getName());
			sb.append(" - Companies: ");
			sb.append(investorService.getAmountOfCompaniesInvestedIn(i));
		}
		return sb.toString();

	}

	/**
	 * Displays the investors that have invested in the  least amount of companies
	 * @return the list as a string
	 */
	public String lowestNumberOfCompanies() {
		StringBuilder sb = new StringBuilder();

		sb.append("\nInvestor(s) with lowest number of companies:");
		for(Investor i : td.getLowestNumberOfCompanies()) {
			sb.append("\n");
			sb.append(i.getId());
			sb.append(" - Name: ");
			sb.append(i.getName());
			sb.append(" - Companies: ");
			sb.append(investorService.getAmountOfCompaniesInvestedIn(i));
		}
		return sb.toString();

	}

	/**
	 * Displays the total amount of transactions
	 * @return the number as a string
	 */
	public String totalTransactions() {
		StringBuilder sb = new StringBuilder();

		sb.append("\nTotal number of transactions: ");
		sb.append(td.getTotalNumberOfTransactions());
		return sb.toString();

	}

	/**
	 * Displays all the companies and their details in a table
	 * @return the list as a string
	 */
	public String allCompanies() {
		StringBuilder sb = new StringBuilder();

		List<Company> companies = companyService.getAllCompanies();

		sb.append("\nCompanies report: ");
		for(int x = 0; x < companies.size(); x++) {
			sb.append("\n");
			sb.append(companies.get(x).getId());
			sb.append(" - "+companies.get(x).getName());
			sb.append(" - Capital: €"+companies.get(x).getCapital());
			sb.append(" - Shares sold: ");
			sb.append(companies.get(x).getSharesSold());
			sb.append(" - Shares left: ");
			sb.append((companies.get(x).getInitialShares() - companies.get(x).getSharesSold()));
			sb.append(" Share price: €");
			sb.append(companies.get(x).getSharePrice());
		}
		return sb.toString();
	}

	/**
	 * Displays all the investor and their details in a table
	 * @return the list as a string
	 */
	public String allInvestors() {
		StringBuilder sb = new StringBuilder();

		sb.append("\nInvestors report");
		for(Investor i : investorService.getAllInvestors()) {
			sb.append("\n");
			sb.append(i.getId());
			sb.append(" - Name: ");
			sb.append(i.getName());
			sb.append(" - Companies: ");
			sb.append(investorService.getAmountOfCompaniesInvestedIn(i));
			sb.append(" - Shares: ");
			sb.append(i.getTotalNumberOfSharesBought());
			sb.append(" - Initial Budget: €");
			sb.append(i.getInitialBudget());
			sb.append(" - Final Budget: €");
			sb.append(i.getTotalNumberOfSharesBought());
		}
		return sb.toString();
	}

	/**
	 * Displays all the transactions and thier details in a table
	 * @return the list as a string
	 */
	public String allTransactions() {
		StringBuilder sb = new StringBuilder();

		sb.append("\nTransactions report");
		for(Transaction i : transactionService.getAllTransactions()) {
			sb.append("\n");
			sb.append(i.getTransactionId());
			sb.append(" - Investor: ");
			sb.append(i.getInvestor().getName());
			sb.append(" - Company: ");
			sb.append(i.getCompany().getName());
			sb.append(" - Date: ");
			sb.append(i.getDate().toString());
		}
		return sb.toString();
	}

	/**
	 * Displays the full report
	 * @return the report as a string
	 */
	public String fullReport() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n----------REPORT----------");
		sb.append("\nCOMPANIES:");
		sb.append(highestCapital());
		sb.append("\n");
		sb.append(lowestCapital());
		sb.append("\n\nINVESTORS");
		sb.append(highestNumberOfShares());
		sb.append("\n");
		sb.append(lowestNumberOfShares());
		sb.append("\n");
		sb.append(highestNumberOfCompanies());
		sb.append("\n");
		sb.append(lowestNumberOfCompanies());
		sb.append("\n\nTRANSACTIONS");
		sb.append(totalTransactions());
		return sb.toString();
	}

	/**
	 * Generates the PDF report
	 * @param content the content to full the report
	 * @param path the path in which to save the report
	 */
	public void generatePdfReport(String content, String path) {
		reportService.generatePdfReport(content, path);
	}

	/**
	 * Generates the Word Document report
	 * @param content the content to full the report
	 * @param path the path in which to save the report
	 */
	public void generateDocxReport(String content, String path) {
		reportService.generateDocxReport(content, path);
	}

	/**
	 * Generates the text file report
	 * @param content the content to full the report
	 * @param path the path in which to save the report
	 */
	public void generateTxtReport(String content, String path) {
		reportService.generateTxtReport(content, path);
	}
}
