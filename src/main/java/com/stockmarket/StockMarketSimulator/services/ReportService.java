package com.stockmarket.StockMarketSimulator.services;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockmarket.StockMarketSimulator.model.Report;
import com.stockmarket.StockMarketSimulator.model.TradingDay;
import com.stockmarket.StockMarketSimulator.repositories.ReportRepository;
import com.stockmarket.StockMarketSimulator.view.report.ReportFileSingletonFactory;
import com.stockmarket.StockMarketSimulator.view.report.ReportFile;
import com.stockmarket.StockMarketSimulator.view.report.ReportType;

/**
 * Class to handle the Report generation and persistence.
 * @author Gustavo Lessa (https://github.com/gustavolessa23/)
 *
 */
@Service
public class ReportService {
	
	
	@Autowired
	private ReportRepository reportRepository; //to handle DB operations
	
	@Autowired
	private TradingDay td; // to retrieve the state from the simulation
	
	/**
	 * Saves a new entry to the report table, after creating using the builder.
	 */
	public void saveReportToDb() {
		
		Report report = Report.builder()
				.highestCapital(td.getHighestCapital().get(0).getCapital())
				.highestCompanies(td.getHighestNumberOfCompanies().get(0).getNumberOfCompaniesInvestedIn())
				.highestShares(td.getHighestNumberOfShares().get(0).getTotalNumberOfSharesBought())
				.lowestCapital(td.getLowestCapital().get(0).getCapital())
				.lowestCompanies(td.getLowestNumberOfCompanies().get(0).getNumberOfCompaniesInvestedIn())
				.lowestShares(td.getLowestNumberOfShares().get(0).getTotalNumberOfSharesBought())
				.numberOfTransactions(td.getTotalNumberOfTransactions())
				.build();
		
		Report savedReport = reportRepository.save(report);
		
	}
	
	/**
	 * Generates a PDF file of the report current on display.
	 * @param content
	 * @param path
	 */
	public void generatePdfReport(String content, String path) {
		// retrieve a ReportFile for PDF, passing content and path, using the Factory Pattern, and correcting the extension if needed.
		ReportFile file = ReportFileSingletonFactory.getInstance().create(ReportType.PDF, (path.endsWith(".pdf") ? path : path+".pdf"), content);
		try {
			file.generate(); // generate file
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Generates a Docx file of the report current on display.
	 * @param content
	 * @param path
	 */
	public void generateDocxReport(String content, String path) {
		// retrieve a ReportFile for DOCX, passing content and path, using the Factory Pattern, and correcting the extension if needed.
		ReportFile file = ReportFileSingletonFactory.getInstance().create(ReportType.DOCX, (path.endsWith(".doc") || path.endsWith(".docx") ? path : path+".docx"), content);
		try {
			file.generate();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates a Txt file of the report current on display.
	 * @param content
	 * @param path
	 */
	public void generateTxtReport(String content, String path) {
		
		// retrieve a ReportFile for TXT, passing content and path, using the Factory Pattern, and correcting the extension if needed.
		ReportFile file = ReportFileSingletonFactory.getInstance().create(ReportType.TXT, (path.endsWith(".txt") ? path : path+".txt"), content);
		try {
			file.generate();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
