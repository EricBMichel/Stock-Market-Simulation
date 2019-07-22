package com.stockmarket.StockMarketSimulator.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

import com.stockmarket.StockMarketSimulator.services.CompanyService;
import com.stockmarket.StockMarketSimulator.services.InvestorService;
import com.stockmarket.StockMarketSimulator.services.TransactionService;
import com.stockmarket.StockMarketSimulator.view.View;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class that represents a trading day at stock market.
 * @author Gustavo Lessa
 *
 */
@Component
public class TradingDay {

	@Autowired
	private CompanyService companyService;  // company operations

	@Autowired
	private InvestorService investorService; // investor operations

	@Autowired
	private TransactionService transactionService; // transaction operations

	@Autowired
	private View view; // display feature
	
	@Getter @Setter
	private boolean simulationFinished = false;

	/**
	 * Method to run a simulation of a trading day.
	 * @param List<Company> companyList
	 * @param List<Investor> invList
	 */
	public void trade(List<Company> companyList, List<Investor> invList) {

		view.display("Starting trading day...");
		while(simulationCanContinue()) { // pretty intuitive, I believe (while simulation can continue)

			//chooses a random investor
			Investor randomInvestor = investorService.getRandomInvestor();

			//get random still available and affordable company that is unique for chosen investor, if existent, or a random available and affordable one
			List<Integer> companyIds = investorService.getDesirableCompaniesForInvestor(randomInvestor);

			//if list is empty, go back to the loop declaration
			if (companyIds.isEmpty()) continue;

			//try transactions until one is successful or return false if non is possible.
			transactionService.tryTransaction(randomInvestor, companyIds);

		}

		view.display("Simulation ended"); // display message
		this.simulationFinished = true;

	}

	/**
	 * Checks if the simulation can continue, by checking if there is still a possible transaction to be made.
	 * @return true if simulation can continue.
	 */
	public boolean simulationCanContinue() {

		Investor highestBudget = investorService.getHighestBudget(); // get investor with highest budget
		Company cheapestShare = companyService.getCheapestAvailableShare(); // get company with cheapest share

		if(cheapestShare == null || highestBudget == null) return false; // if any is null

		if(cheapestShare.getSharePrice() > highestBudget.getBudget()) return false; // if investor can't afford share, return false

		return true; 

	}

	/**
	 * Get company/companies with highest capital.
	 * @return List<Company> companies with highest capital
	 */
	public List<Company> getHighestCapital(){
		List<Company> companies = new ArrayList<>(); // create new ArrayList

		companies.add(companyService.getAllCompanies().get(0)); // add first existent company to the list

		for (int x = 1; x<companyService.getAllCompanies().size(); x++) { // iterate through list
			Company current = companyService.getAllCompanies().get(x); //get current company
			if (current.getCapital()>companies.get(0).getCapital()) { // if current company's capital is greater than previous one
				companies.clear();										// clear the list
				companies.add(current);									// add current to the list
			} else if (current.getCapital()==companies.get(0).getCapital()) { // if capital is the same
				companies.add(current); // add current to the list, without clearing first
			}
		}
		return companies; // return list
	}

	/**
	 * Get company/companies with lowest capital.
	 * @return
	 */
	public List<Company> getLowestCapital(){
		List<Company> companies = new ArrayList<>(); // create new ArrayList

		companies.add(companyService.getAllCompanies().get(0)); // add first existent company to the list

		for (int x = 1; x<companyService.getAllCompanies().size(); x++) { // iterate through list
			Company current = companyService.getAllCompanies().get(x); //get current company
			if (current.getCapital()<companies.get(0).getCapital()) { // if current company's capital is smaller than previous one
				companies.clear();										// clear the list
				companies.add(current);									// add current to the list
			} else if (current.getCapital()==companies.get(0).getCapital()) { // if capital is the same
				companies.add(current); // add current to the list, without clearing first
			}
		}
		return companies; // return list
	}

	/**
	 * Get investors with the highest number of shares.
	 * @return List<Investor>
	 */
	public List<Investor> getHighestNumberOfShares(){
		List<Investor> investors = new ArrayList<>(); // create new list

		investors.add(investorService.getAllInvestors().get(0)); // add first investor to the list

		for (int x = 1; x<investorService.getAllInvestors().size(); x++) { // iterate through all investors
			Investor current = investorService.getAllInvestors().get(x); // get current investor
			if (current.getTotalNumberOfSharesBought()>investors.get(0).getTotalNumberOfSharesBought()) { // if current bought more shares than the previous
				investors.clear(); // clear list
				investors.add(current); // add current
			} else if (current.getTotalNumberOfSharesBought()==investors.get(0).getTotalNumberOfSharesBought()) { // if current has same amount of shares than previous
				investors.add(current); // just add current, without clearing the list first.
			}
		}
		return investors; // return list
	}

	/**
	 * Get investors with the lowest number of shares.
	 * @return List<Investor>
	 */
	public List<Investor> getLowestNumberOfShares(){
		List<Investor> investors = new ArrayList<>(); // create new list

		investors.add(investorService.getAllInvestors().get(0)); // add first investor to the list

		for (int x = 1; x<investorService.getAllInvestors().size(); x++) { // iterate through all investors
			Investor current = investorService.getAllInvestors().get(x); // get current investor
			if (current.getTotalNumberOfSharesBought()<investors.get(0).getTotalNumberOfSharesBought()) { // if current bought less shares than the previous
				investors.clear(); // clear list
				investors.add(current); // add current
			} else if (current.getTotalNumberOfSharesBought()==investors.get(0).getTotalNumberOfSharesBought()) { // if current has same amount of shares than previous
				investors.add(current); // just add current, without clearing the list first.
			}
		}
		return investors; // return list
	}

	/**
	 * Get investor who bought shares from the highest number of companies.
	 * @return List<Investor>
	 */
	public List<Investor> getHighestNumberOfCompanies(){
		List<Investor> investors = new ArrayList<>(); // create list

		investors.add(investorService.getInvestorById(1)); // add first investor to list

		for (int x = 1; x<investorService.getAllInvestors().size(); x++) { // iterate through list
			Investor current = investorService.getAllInvestors().get(x); // get current investor
			
			// if current investor invested in a higher number of companies
			if (investorService.getAmountOfCompaniesInvestedIn(current)>investorService.getAmountOfCompaniesInvestedIn(investors.get(0))) {
				investors.clear(); // clear list
				investors.add(current); // add current investor to list
			
			// if current investor invested in the same amount of companies
			} else if (investorService.getAmountOfCompaniesInvestedIn(current)==investorService.getAmountOfCompaniesInvestedIn(investors.get(0))) {
				investors.add(current); // only add to the list, without clearing first.
			}
		}
		return investors; // return list
	}

	/**
	 * Get investor who bought shares from the lowest number of companies.
	 * @return List<Investor>
	 */
	public List<Investor> getLowestNumberOfCompanies(){
		List<Investor> investors = new ArrayList<>(); // create list

		investors.add(investorService.getInvestorById(1)); // add first investor to list

		for (int x = 1; x<investorService.getAllInvestors().size(); x++) { // iterate through list
			Investor current = investorService.getAllInvestors().get(x); // get current investor
			
			// if current investor invested in a lower number of companies
			if (investorService.getAmountOfCompaniesInvestedIn(current)<investorService.getAmountOfCompaniesInvestedIn(investors.get(0))) {
				investors.clear(); // clear list
				investors.add(current); // add current investor to list
			
			// if current investor invested in the same amount of companies
			} else if (investorService.getAmountOfCompaniesInvestedIn(current)==investorService.getAmountOfCompaniesInvestedIn(investors.get(0))) {
				investors.add(current); // only add to the list, without clearing first.
			}
		}
		return investors; // return list
	}

	/**
	 * Get total number of transactions
	 * @return long
	 */
	public long getTotalNumberOfTransactions() {
		return transactionService.getAllTransactions().size(); // get list of transactions from service and get its size.
	}

}

