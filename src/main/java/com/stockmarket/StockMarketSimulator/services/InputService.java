package com.stockmarket.StockMarketSimulator.services;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stockmarket.StockMarketSimulator.view.View;

@Service
/**
 * Class representing the input, using Scanner and ValidationService classes.
 * @author Gustavo Lessa
 *
 */
public class InputService {
	
	@Autowired
	private ValidationService validate;
	
	@Autowired
	private View view;

	private Scanner scan; // Scanner to retrieve input.

	/**
	 * Constructor that initializes variables.
	 */
	public InputService() {
		scan = new Scanner(System.in);
	}

	/**
	 * Method responsible for checking if the input is a String
	 * @return String input
	 */
	public String getNextString() {
		String line = "";
		while(line.isEmpty())
			line = scan.nextLine();
		
		return line;
	}

	/**
	 * Method responsible for checking if the input is an Integer, considering a limit.
	 * @param limit
	 * @return validated input.
	 */
	public int getNextInt(int limit) {
		int typedInt = -100;
		try{
			while(typedInt == -100) // while variable doesn't change
				typedInt = scan.nextInt(); // retrieve next int and save to that variable.

			if(!validate.checkForInt(typedInt, 1, limit)) { // if the validation returns false
				view.displayError("\n*** The option should be and integer between 1" // display error messagge
						+ " and "+limit+". ***\n");
				view.displayError("Please try again:");
				return getNextInt(limit); // call method again
				
			} else { // if validation is successful
				return typedInt; // return validated int
			}
			
		} catch(InputMismatchException e){ // if input is not an integer
			view.displayError("\n*** Input was not an integer. ***\nPlease try again.\n"); // display message
			scan.nextLine(); // flush scanner
			return getNextInt(limit); // recursively call this method again.
		}
	}


	/**
	 * Method responsible for retrieving a validated Y/N answer from user.
	 * @return true if user answered yes, false otherwise.
	 */
	public boolean isYes() {
		String answer = "";
		try{
			while(answer.isEmpty())
				answer = scan.nextLine(); // get next line of input

			return validate.checkForYes(answer); // return validated answer, true if user input is yes or y.

		} catch(IllegalArgumentException e){ // if something different was typed.
			view.displayError("\\n*** Please type Yes(Y) or No(N) only. ***\\n"); // display message
			return isYes(); // call itself 
		}
	}


}
