package com.stockmarket.StockMarketSimulator.services;

import org.springframework.stereotype.Service;

@Service
/**
 * This class contains methods used to validate user input such as  
 * int with defined range, yes/no, PPS number, e-mail address and phone number.
 * @author Gustavo Lessa
 */
public class ValidationService {
	
	

	/**
	 * This method checks if a String is 'Yes' or 'No', accepting 'n' and 'y', case insensitive.
	 * @param answer (String)
	 * @return true if the input is 'Y' or 'yes', case insensitive, false otherwise.
	 */
	public boolean checkForYes(String answer) throws IllegalArgumentException {

		if(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) // if String is y or yes
			return true; // return true
		else if(answer.equalsIgnoreCase("n") || answer.equalsIgnoreCase("no")) // if String is n or no
			return false; // return false
		else // if String isn't any of above options
			throw new IllegalArgumentException(); // throw exception.
	}

	/**
	 * This method checks for a integer input between a specific boundary.
	 * @param number (int)
	 * @param lowerBoundary (int)
	 * @param upperBoundary (int)
	 * @return true if integer is between the specified boundary.
	 */
	public boolean checkForInt(int number, int lowerBoundary, int upperBoundary){
		if(number>=lowerBoundary && number<=upperBoundary) // if number is inside boundaries
			return true; // return true
		else // if it isn't
			return false; // return false
	}

}
