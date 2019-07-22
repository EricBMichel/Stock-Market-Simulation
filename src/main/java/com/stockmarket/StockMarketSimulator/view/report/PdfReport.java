package com.stockmarket.StockMarketSimulator.view.report;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.stereotype.Component;

/**
 * Class responsible for saving a PDF file.
 * @author Gustavo Lessa
 *
 */
@Component
public class PdfReport extends ReportFile {

	/**
	 * Constructor required by Spring Boot.
	 */
	private PdfReport() {
		super();
	}
	
	/**
	 * Constructor to create a new PDF report, passing a path and a content.
	 * @param path
	 * @param content
	 */
	public PdfReport(String path, String content) {
		super(path, content);
	}

	/**
	 * Generate PDF file.
	 */
	@Override
	public void generate() throws IOException {
		System.out.println("trying to save at: "+this.path);
		PdfWriter writer = new PdfWriter(this.path);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf);
		document.add(new Paragraph(this.content));
		document.close();
	}
	

}
