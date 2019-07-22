package com.stockmarket.StockMarketSimulator.view.report;

import org.springframework.stereotype.Component;

/**
 * Factory class for report file.
 * @author Gustavo Lessa
 *
 */
@Component
public class ReportFileSingletonFactory {
	
	private static ReportFileSingletonFactory instance; // only instance to be created.
	
	/**
	 * Private constructor, only allowing this class to self instanciate.
	 */
	private ReportFileSingletonFactory() {
		
	}

	/**
	 * Create a file, passing a type (Enum), path and content.
	 * @param type
	 * @param path
	 * @param content
	 * @return ReportFile of chosen type, path, and content.
	 */
	public ReportFile create(ReportType type, String path, String content) {
		
		if(type == ReportType.DOCX) {
			return new DocxReport(path, content);
		} else if(type == ReportType.PDF) {
			return new PdfReport(path, content);
		} else if(type == ReportType.TXT) {
			return new TxtReport(path, content);
		} else {
			return null;
		}
		
	}
	
	/**
	 * Method to return the ReportFileSingletonFactory instance.
	 * @return ReportFileSingletonFactory instance
	 */
	public static ReportFileSingletonFactory getInstance() {
		if(instance == null) {
			instance = new ReportFileSingletonFactory();
		}
		return instance;
	}
	
}
