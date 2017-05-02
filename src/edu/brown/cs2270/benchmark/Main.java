package edu.brown.cs2270.benchmark;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
	
	private final static String DB_PATH = "/data/db.db";
	private final static String CSV_PATH = "/data/votes.csv";
	
	private static final Logger LOG = LogManager.getLogger(Main.class);
	
	public static void main(String[] args) {
		try {
			BenchmarkStrategy strat = new Strategy1(DB_PATH, CSV_PATH);
			BenchmarkRunner runner = new BenchmarkRunner(strat);
			runner.run();
		} catch (Exception e) {
			LOG.error(String.format("Unable to boostrap strategy, %s", e));
			e.printStackTrace();
		}
	}

}
