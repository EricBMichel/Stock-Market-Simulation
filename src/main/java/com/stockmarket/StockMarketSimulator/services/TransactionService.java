package com.stockmarket.StockMarketSimulator.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockmarket.StockMarketSimulator.exception.CompanyOutOfSharesException;
import com.stockmarket.StockMarketSimulator.exception.InvestorOutOfFundsException;
import com.stockmarket.StockMarketSimulator.model.Company;
import com.stockmarket.StockMarketSimulator.model.Data;
import com.stockmarket.StockMarketSimulator.model.Investor;
import com.stockmarket.StockMarketSimulator.model.Share;
import com.stockmarket.StockMarketSimulator.model.Transaction;
import com.stockmarket.StockMarketSimulator.view.View;

@Service
public class TransactionService {

	@Autowired 
	private Data data;

	@Autowired 
	private InvestorService investorService;

	@Autowired 
	private CompanyService companyService;
	
	@Autowired
	private View view;

	/**
	 * This method creates a transaction between a company and investor and creates a new transaction object
	 * @param company the company that is trading
	 * @param investor the investor that is trading
	 * @throws InvestorOutOfFundsException Investor has no more funds
	 * @throws CompanyOutOfSharesException Company has no more shares
	 */
	public void executeTransaction(Company company, Investor investor) throws InvestorOutOfFundsException, CompanyOutOfSharesException{
		if(investor.getBudget()<company.getSharePrice()) { //check if investors budget is less than share price
			throw new InvestorOutOfFundsException("Investor "+investor.getId()+", budget: "+investor.getBudget()+" cannot afford Company "+company.getId()+" share, priced "+company.getSharePrice());
		} else if (company.getNumberOfSharesAvailable() <= 0){ //check if company has shares
			throw new CompanyOutOfSharesException("Company "+company.getId()+" has no more shares to sell!");
		} else {
			Share share = companyService.sellShare(company); //company sells the share
			investorService.buyShare(investor, share);	//investor buys the share
			Transaction transaction = new Transaction(company, investor); //transaction is made from that company and investor
			data.getTransactions().add(transaction); //transaction is added to the simulation list

			afterTenTransactionsVerification(transaction); //check transaction is 10th transaction from before
		}
	}

	/**
	 * This method creates the check if an investor is eligible to but a share
	 * @param investor The investor
	 * @param companyIds The company 
	 * @return true if the check is viable
	 * @return false if the check is inviable
	 */
	public boolean tryTransaction(Investor investor, List<Integer> companyIds) {
		while(!companyIds.isEmpty()) {
			Company randomCompany = companyService.getCompanyFromId(companyIds.remove(0));
			try {
				executeTransaction(randomCompany, investor);
				return true;
			}catch(InvestorOutOfFundsException i){
				view.display(i.getMessage());
			}catch( CompanyOutOfSharesException c) {
				view.display(c.getMessage());
			}	
		}
		return false;
	}

	/**
	 * This method checks if the 10th transaction has occurred and makes the necessary changes to the prices of the companies
	 * @param transaction The transaction to check
	 */
	public void afterTenTransactionsVerification(Transaction transaction) {
		if(transaction.getTransactionId()%10==0) { //checks after every 10 transactions
			
			List<Company> companies = new ArrayList<>();
			for(int i = 0;i<data.getCompanies().size();i++) {
				Company company = data.getCompanies().get(i);
				if(company.getHasSoldShare()==false && !company.getShares().isEmpty()){ //check if company has sold share AND still has shares to sell
					companyService.decreasePrice(company); //decrease the price if company has not sold share
					companies.add(company);
				}
				company.setHasSoldShare(false); //set all the companies soldShare state back to false
			}
			
			if(!companies.isEmpty()) view.display("Decrease in price for "+companies.size()+
					(companies.size() == 1 ? " company." : " companies."));
				
		}
	}
	
	/**
	 * This method returns the simulations transaction lists
	 * @return the transaction list
	 */
	public List<Transaction> getAllTransactions(){
		return data.getTransactions();
	}

	/**
	 * This method creates a new simulation transaction list and resets the id of the transaction objects
	 */
	public void clearTransactions() {
		data.setTransactions(new ArrayList<Transaction>());
		Transaction.resetId();
	}

}
