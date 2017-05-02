package edu.brown.cs2270.benchmark;

import org.apache.logging.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;;

public class BenchmarkRunner {
	
	private static final Logger LOG = LogManager.getLogger(BenchmarkRunner.class);
	
	private final BenchmarkStrategy strat;
	protected final CSVReader reader;
	
	public BenchmarkRunner(String csv, BenchmarkStrategy strat) throws FileNotFoundException {
		this.strat = strat;
		
		URL csvUrl = getClass().getResource(csv);
		this.reader = new CSVReader(new FileReader(csvUrl.getPath()));
	}
	
	public void run() throws SQLException, IOException {
	// setup table
		try {
			strat.setupSchema();
			
			// run benchmark
			long startTime = System.currentTimeMillis();
			long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			
			
			String[] nextLine;
			int counter = 0;
			while ((nextLine = reader.readNext()) != null) {
				counter++;
				Vote vote = Vote.fromCsv(nextLine);
				strat.processVote(vote);
			}
			
			long endTime = System.currentTimeMillis();
			long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			System.out.println("total millis: " + (endTime - startTime));
			System.out.println("memory usage: " + (endMemory - startMemory));
			
		} catch (Exception e) {
			throw e;
		} finally {
			strat.cleanSchema();
		}


	}
}
