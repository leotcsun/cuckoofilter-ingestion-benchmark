package edu.brown.cs2270.benchmark;

public class Main {
	
	private final static String DB_PATH = "db.db";
	private final static int DATA_SIZE = 5000;
	private final static String CSV_PATH = String.format("data/votes-%d.csv", DATA_SIZE);
	
	
	public static void main(String[] args) {
		BenchmarkStrategy[] strategies = {
				new Strategy1(),
				new Strategy2(),
				new Strategy3(DATA_SIZE)
		};
		
		for (int i = 0; i < strategies.length; i++) {
			BenchmarkStrategy s = strategies[i];
			try {
				BenchmarkRunner runner = new BenchmarkRunner(DB_PATH, CSV_PATH, s);
				
				runner.run(DATA_SIZE);
				runner.complete();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				strategies[i] = null;
				System.gc();
			}
		}

	}
}
