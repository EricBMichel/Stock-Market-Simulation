package com.stockmarket.StockMarketSimulator.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockmarket.StockMarketSimulator.exception.InvestorHasInvestedInAllCompaniesException;
import com.stockmarket.StockMarketSimulator.model.Data;
import com.stockmarket.StockMarketSimulator.model.Investor;
import com.stockmarket.StockMarketSimulator.model.Share;
import com.stockmarket.StockMarketSimulator.repositories.InvestorRepository;
import com.stockmarket.StockMarketSimulator.setup.InvestorGenerator;
import com.stockmarket.StockMarketSimulator.view.View;

/**
 * Investor service class, performing operations to Investor entity.
 * @author Gustavo Lessa
 *
 */
@Service
public class InvestorService {

	@Autowired
	private InvestorRepository investorRepository;

	@Autowired
	private InvestorGenerator investorGenerator;	
	
	@Autowired
	private CompanyService companyService;

	@Autowired
	private Data data; 

	@Autowired
	private View view;
	
	/**
	 * Method to populate the investor list using generator.
	 */
	public void populateInvestors() {
		view.display("Generating investors...");
		List<Investor> investors = investorGenerator.generateInvestors();
		data.setInvestors(investors);
		investorRepository.saveAll(investors);
	}

	/**
	 * Method to reset investors.
	 */
	public void clearInvestors() {
		 
		investorRepository.deleteAll(); // delete all from DB
		data.setInvestors(new ArrayList<Investor>()); // delete all from memory
		Investor.lastId = 0; // set lastId as 0
	}

	/**
	 * Method to get a random investor
	 * @return random investor
	 */
	public Investor getRandomInvestor() {
		Random rG = new Random();
		return data.getInvestors().get(rG.nextInt(data.getInvestors().size()));
	}
	
	/**
	 * Return list of all investors.
	 * @return list of investors
	 */
	public List<Investor> getAllInvestors() {
		return data.getInvestors();
	}
	
	/**
	 * Return Investor, searching by ID.
	 * @param id
	 * @return investor
	 */
	public Investor getInvestorById(int id) {
		return data.getInvestors().get(id-1);
	}
	
	/**
	 * Update all investors, using the whole list of investors in the system.
	 */
	public void updateInvestors() {
		investorRepository.saveAll(data.getInvestors());
	}
	
	/**
	 * Method to add an investor to the DB using JPA and Hibernate.
	 * @param investor
	 */
	public void addInvestorToDb(Investor investor) {
		investorRepository.save(investor);
	}

	/**
	 * Method to get and return all Investors in the Database.
	 * @return return a list of investors from database.
	 */
	public List<Investor> getAllInvestorsFromDb(){
		return investorRepository.findAll();
	}

	// ----------- SHARES ---------------------

	/**
	 * Method to get the list of shares an investor has.
	 * @param investor
	 * @return
	 */
	public List<Share> getShares(Investor investor){
		return investor.getWallet().getShares();
	}

	/**
	 * Method to get the amount of shares an investor has.
	 * @param investor
	 * @return
	 */
	public int getAmountOfShares(Investor investor) {
		return investor.getWallet().getShares().size();
	}

	/**
	 * Method for an investor to buy a share.
	 * @param investor
	 * @param share
	 */
	public void buyShare(Investor investor, Share share) {
		investor.setBudget(Data.round(investor.getBudget()-share.getPrice(),2));// decrement budget by share price
		investor.getWallet().getShares().add(share); // add share to wallet
		addCompanyId(investor, share.getCompanyId()); // add companyId to the map
		investor.incrementSharesBought();;	// increment investor's share counter
	}

	/**
	 * Method to add a new entry to an investor's map of amount of shares (value) for each company ID (key)
	 * @param investor
	 * @param companyId
	 * @return
	 */
	private void addCompanyId(Investor investor, int companyId) {
		// set the value for key = companyId to 1 or increment existing value.
		investor.getWallet().getCompaniesShares().merge(companyId, 1, Integer::sum);  // add
		
		// update the instance variable of this investor.
		investor.setNumberOfCompaniesInvestedIn(getAmountOfCompaniesInvestedIn(investor));
	}

	/**
	 * Method to return a list of company IDs from the shares an investor owns.
	 * @param investor
	 * @return
	 */
	public List<Integer> getCompaniesIds(Investor investor) {
		List<Integer> companyIds = new ArrayList<>(); // create new list

		// for each entry of the map, add the key (id) to the list.
		investor.getWallet().getCompaniesShares().forEach((key, value) -> {
			companyIds.add(key); 
		});

		return companyIds; // return list
	}

	/**
	 * Method to return the number of companies and investor has invested in.
	 * @param investor
	 * @return
	 */
	public int getAmountOfCompaniesInvestedIn(Investor investor) {
		// the the map size (key is companyId).
		return investor.getWallet().getCompaniesShares().size();
	}

	/**
	 * Method to return a list of the company IDs an investor hasn't invested in.
	 * @param investor
	 * @param numberOfCompanies
	 * @return
	 */
	public List<Integer> getRemainingCompaniesIds(Investor investor, int numberOfCompanies) throws InvestorHasInvestedInAllCompaniesException{
		// create a list
		List<Integer> remaining = new ArrayList<>();
		// if investor has shares from all companies, throw exception.
		if (investor.getWallet().getCompaniesShares().size() == numberOfCompanies) {
			throw new InvestorHasInvestedInAllCompaniesException("Investor "+investor.getId()+" has invested in all companies!");
		} else { 
			for (int x = 1; x <= numberOfCompanies; x++) { // for each company
				// if the investor doens't have this company in his wallet, add company to list
				if (!investor.getWallet().getCompaniesShares().containsKey(x)) remaining.add(x);
			}	
		}
		// return list
		return remaining;
	}
	
	/**
	 * Method to return a list of desirable company IDs for an investor, not considered if share is not affordable or not available.
	 * @param investor
	 * @return list of desirable companies ID.
	 */
	public List<Integer> getDesirableCompaniesForInvestor(Investor investor) {
		List<Integer> desirableCompanyIds = new ArrayList<>();

		try {
			//get the list of company IDs the investor has no shares of.
			desirableCompanyIds = getRemainingCompaniesIds(investor, data.getCompanies().size());
		} catch (InvestorHasInvestedInAllCompaniesException e) { // if investor invested in all companies:
			
			desirableCompanyIds = getCompaniesIds(investor);
		}
		
		//remove all companies with no shares left
		desirableCompanyIds.removeIf(id -> companyService.getCompanyFromId(id).getShares().size() == 0);
		// remove all companies that investor can't afford
		desirableCompanyIds.removeIf(id -> companyService.getCompanyFromId(id).getSharePrice() > investor.getBudget());
		// shuffle the list 
		Collections.shuffle(desirableCompanyIds);
		// return it
		return desirableCompanyIds;
	}
	
	/**
	 * Method to retrieve an investor with highest budget.
	 * @return Investor with highest budget
	 */
	public Investor getHighestBudget() {
		Investor investor = data.getInvestors().get(0);
		for(int x = 1; x < data.getInvestors().size(); x++) {
			Investor current = data.getInvestors().get(x);
			if(current.getBudget() > investor.getBudget()) {
				investor = current;
			}
		}
		return investor;
	}
	

}

