package canvas;

import java.awt.CheckboxGroup;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import custom.processor.CSSLoader;
import custom.processor.PropertyLoader;
import custom.tags.CustomForm;
import nodes.NodeCreator;
import processor.BinaryWriter;
import standard.form.Button;
import standard.form.CheckBoxGroup;
import standard.form.InputBox;
import standard.form.Label;
import standard.form.RadioButton;
import standard.form.RadioGroup;
import standard.form.Select;
import standard.form.SubmitButton;
import standard.link.Image;
import standard.link.Link;
import standard.list.StandardList;
import standard.table.TD;
import standard.table.TR;
import standard.table.Table;
import standard.text.Cite;
import standard.text.Heading;
import standard.text.Paragraph;
import structure.Brush;

public class Painter extends Brush{
	volatile static int totalDataWritten;
	private static Logger logger = LoggerFactory.getLogger(Brush.class);
	static  List<CSVRecord> records;
	static Table table;
	static int remaining;
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub\
		totalDataWritten=0;
		logger.info("Starting batch");
		CSVParser csvParser = null;
		PropertyLoader propertyLoader=new PropertyLoader();
		try {
			csvParser = new CSVParser(new FileReader((propertyLoader.getProperty("CSV"))), CSVFormat.DEFAULT);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Canvas canvas = new Canvas();
		Brush.setPageTitle(propertyLoader.getProperty("TITLE"));
		clearNodes();
		records=csvParser.getRecords();

		table=new Table();
		table.setStyle("table");
		logger.info("starting for "+args[0]);
		if(args[0].equals("1")) {
			logger.info("Using single thread to write ");
			writeUsingSingleThread();
		}
		else {
			logger.info("Using multi thread to write there could be a change in ordering");
			writeUsingMultiThread();
		}
	    
	  
	    logger.info("writing to html file");
	    addNode(table.createPairNode());
		System.out.println("Batch ended");
	    canvas.paint();
	    System.exit(0);
	}
	public static synchronized void addCurrent(Table table) {
		List<String> data=new ArrayList<>();
		table.addData(data,"tr","td");
	}
	public static void writeUsingSingleThread() {
		int counter=records.size();
		for(CSVRecord i :  records) {
		    table.addData(i.toList(),"tr","td");
		    logger.info("Remaining are : "+counter);
		    counter--;
		}
	}
	public static void writeUsingMultiThread() {
		  table.addHeading(records.get(0).toList());
		  ExecutorService executor= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);
		  int prev=0;
		  remaining=records.size();
		    for(int i=1;i<records.size();i=i+Math.min(100, remaining)) {
		    	remaining--;
		    	final int k=i;
		    	
		    	final int files=i+100;
		    	
		    	logger.info("Booting Thread "+" "+prev);
		    	 executor.execute(()->{
		    			for(int j=k;j<files;j++) {
		    				
		    				try {
		    				
	    					
	    				    table.addData(records.get(j).toList(),"tr","td");
	    				    logger.info("record number "+j);
	    				    totalDataWritten++;
		    				}
		    				catch(Exception e) {
		    					
		    				}
	    				    
	    				}
		    			 logger.info("Added data to file :  Remaining " + (records.size()-(prev+files))+" Written total "+totalDataWritten+" data into file");
		 	    });

		    	logger.info("Thread " + i+" initialized");
		    }
		 

		    
		    executor.shutdown();
		    try {
				while (!executor.awaitTermination(1, TimeUnit.HOURS)) {}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        System.out.println("Finished all threads");
	      
	}

}
