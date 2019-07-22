package com.stockmarket.StockMarketSimulator.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockmarket.StockMarketSimulator.exception.CompanyOutOfSharesException;
import com.stockmarket.StockMarketSimulator.model.Company;
import com.stockmarket.StockMarketSimulator.model.Data;
import com.stockmarket.StockMarketSimulator.model.Share;
import com.stockmarket.StockMarketSimulator.repositories.CompanyRepository;
import com.stockmarket.StockMarketSimulator.setup.CompanyGenerator;
import com.stockmarket.StockMarketSimulator.view.View;

@Service
public class CompanyService {
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private CompanyGenerator companyGenerator; 
	
	@Autowired
	private Data data; 
	
	@Autowired
	private View view;
	
	/**
	 * This method populates the company list by calling the generator and setting the list.
	 */
	public void populateCompanies() {
		view.display("Generating companies...");
		List<Company> companies = companyGenerator.generateCompanies();
		
		data.setCompanies(companies);
		
		Map<Integer, Company> companyMap = new HashMap<>();
		data.getCompanies().forEach(company ->
			companyMap.put(company.getId(), company)
				);
		data.setCompaniesMap(companyMap);
		
		companyRepository.saveAll(companies);
	}

	/**
	 * This method sells for a company and updates all the other necessary values in the simulation
	 * @param company the company to sell a share for
	 * @return the soldShare
	 */
	public Share sellShare(Company company) {
		if (company.getShares().isEmpty()) {
			throw new CompanyOutOfSharesException("Company "+company.getName()+" has no shares left to sell."); // check if it's empty
		}else {
			company.incrementSharesSold(); // increment sharesSold
			
			company.incrementCapital(data.round(company.getSharePrice(),2)); 
	
			Share sold = company.getShares().remove(0); // remove the first share (ArrayList if not empty will always have item on index 0)
			
			sold.setPrice(company.getSharePrice()); // set price accordingly to current share price
			
			company.setHasSoldShare(true);
			
			if(company.getSharesSold()%10 == 0) {
				increasePrice(company);
			}
			
			updateCompanyMap(company);
			
			return sold; // return share
		}

	}
	
	/**
	 * Gets the cheapest available share
	 * @return a company with the cheapest available share
	 */
	public Company getCheapestAvailableShare() {
		Company company = null; //placeholder for the company comparison
		for(int x = 0; x < data.getCompanies().size(); x++) { //get the first company
			Company current = data.getCompanies().get(x);
			
			if(company == null && current.getShares().size() > 0) {
				company = current; //sets a company with an available share
			}
			if(company != null && current.getSharePrice() < company.getSharePrice() && !current.getShares().isEmpty()) {
				company = current; //sets a company with the lowest share
			}
		}
		
		return company; //returns the company that meets all the checks
	}
	
	
	/**
	 * This method is responsible for getting a Company be its ID
	 * @param id 
	 * @return the company ID which is an int id.
	 */
	public Company getCompanyFromDb(Integer id) {
		return companyRepository.getOne(id);
	}
	
	/**
	 * Returns a company from the simulation list using a hash map
	 * @param id company Id to get
	 * @return a company object
	 */
	public Company getCompanyFromId(int id) {
		return data.getCompaniesMap().get(id);
	}

	/**
	 * This method updates the company has map
	 * @param companythe company to set in the hash map
	 */
	public void updateCompanyMap(Company company) {
		data.getCompaniesMap().replace(company.getId(), company);
	}
	
	/**
	 * This method adds a company to the database
	 * @param company
	 */
	public void addCompany(Company company) {
		//company.getCompanyDetails();
		data.getCompanies().add(company);
		data.getCompaniesMap().put(company.getId(), company);
		companyRepository.save(company);
	}
	
	
	/**
	 * Method to get and return all Companies in the Database
	 * @return return a list of companies in the added in the data base.
	 */
	public List<Company> getAllCompaniesFromDb(){
		return companyRepository.findAll();
	}
	
	/**
	 * Method to get and return all Companies in the Database
	 * @return return a list of companies in the added in the data base.
	 */
	public List<Company> getAllCompanies(){
		return data.getCompanies();
	}
	
	/**
	 * This method is responsible for the update of the Company.
	 * @param company takes the Object Company
	 */
	public void updateCompany(Company company) {
		companyRepository.save(company);
	}
	
	/**
	 * Replaces the companies the database
	 */
	public void updateCompanies() {
		companyRepository.saveAll(data.getCompanies());
	}
	
	/**
	 * Removes a company from the database
	 * @param id company based on ID to remove
	 */
	public void deleteCompany(Company id) {
		companyRepository.delete(id);
	}
	
	/**
	 * This method will increase the price of a company after they have sold 10 shares
	 * @param c a company to increase the price for
	 */
	public void increasePrice(Company c) {
		boolean tenSharesSold = c.getSharesSold()%10==0; //check if 10 shares were sold
		
		if(tenSharesSold) { 
			view.display("Increase in price for "+c.getName());
			double newPrice = data.round((c.getSharePrice()*2),2); //increase price by 200%
			c.setSharePrice(newPrice);
		}
	}
	
	/**
	 * This method will decrease the price if a company has not sold a share after 10 transactions were made
	 * @param c a company to decrease the price for
	 */
	public void decreasePrice(Company c) {	
		double sharePriceRounded = data.round(c.getSharePrice(), 2);
		if(sharePriceRounded>0.00) {
			double newPrice = (c.getSharePrice()*0.98); //decrease price by 2%
			c.setSharePrice(data.round((newPrice),2));
		}
	}
	
	/**
	 * This method clears all the company details in the simulation and database to enable a rerun of the simulation
	 */
	public void clearCompanyTable() {
		
		companyRepository.deleteAll();
		data.setCompanies(new ArrayList<Company>());
		Company.lastId = 0;

	}
	

}