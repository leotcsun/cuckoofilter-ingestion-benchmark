package edu.brown.cs2270.benchmark;

import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;;

public class BenchmarkRunner {
	
	private static final Logger LOG = LogManager.getLogger(BenchmarkRunner.class);
	
	private final BenchmarkStrategy strat;
	
	public BenchmarkRunner(BenchmarkStrategy strat) {
		this.strat = strat;
	}
	
	public void run() throws SQLException, IOException {
	// setup table
		try {
			strat.setupSchema();
			
			// run benchmark
			long startTime = System.currentTimeMillis();
			long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			strat.runBenchmark();
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
