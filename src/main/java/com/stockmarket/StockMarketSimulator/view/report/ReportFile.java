package com.stockmarket.StockMarketSimulator.view.report;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract class representing a report file to be saved.
 * @author Gustavo Lessa
 *
 */
public abstract class ReportFile {
	
	protected String path; // path to save file
	protected String content; // content of the file
	
	public abstract void generate() throws IOException; // abstract method to generate file
	
	/**
	 * Method to create a list of strings from the content, to be used when line breaks are not
	 * detected automatically.
	 * @return List<String<
	 */
	protected List<String> convertContentToList(){ 
		
		return Arrays.asList(this.content.split("\\r?\\n"));
	}
	
	/**
	 * Constructor required by SpringBoot (No Args Constructor)
	 */
	protected ReportFile() {
		
	}
	
	/**
	 * Constructor to create a new ReportFile, passing a path and a content.
	 * @param path
	 * @param content
	 */
	public ReportFile(String path, String content) {
		this.path = path;
		this.content = content;
	}
	
	/**
	 * Set path.
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * Get path.
	 * @return
	 */
	public String getPath() {
		return this.path;
	}
	/**
	 * Set content.
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * Get content.
	 * @return
	 */
	public String getContenth() {
		return this.content;
	}

}
