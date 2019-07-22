package com.stockmarket.StockMarketSimulator.view.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class TxtReport extends ReportFile {

	public TxtReport() {
		super();
	}
	
	public TxtReport(String path, String content) {
		super(path, content);
	}

	@Override
	public void generate() throws IOException {

//	    BufferedWriter writer = new BufferedWriter(new FileWriter(new File(this.path)));
//	    writer.write(this.content);
//	     
//	    writer.close();

		PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.path)));
		
		List<String> text = convertContentToList();	
		
		for(int x = 0; x < text.size(); x++) {
			writer.print(text.get(x));
			writer.println();
		}
		
		writer.flush();
		writer.close();
		
	}

}
