package edu.brown.cs2270.benchmark;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class BenchmarkRunner {
	
	
	private final BenchmarkStrategy strat;
	protected final CSVReader reader;
	protected final Connection conn;
	
	private long totalRead = 0;
	private long totalProcessed = 0;
	private long totalLookupTime = 0;
	private long totalProcessTime = 0;
	private long totalBookkeepingTime = 0;
	
	public BenchmarkRunner(String db, String csv, BenchmarkStrategy strat) throws FileNotFoundException, SQLException {
		this.strat = strat;
		
		URL dbUrl = this.getClass().getClassLoader().getResource(db);
		System.out.println(dbUrl);
		this.conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl.getPath());
		
		URL csvUrl = getClass().getResource(csv);
		System.out.println(csvUrl);
		this.reader = new CSVReader(new FileReader(csvUrl.getPath()));
	}
	
	public void run(int dataSize) throws SQLException, IOException {
		try {
			strat.setupSchema(conn);
			
			System.out.println(String.format("Running benchmark with strategy %s\n%s\n", strat.getName(), strat.getDescription()));
			long startTime = System.currentTimeMillis();
			long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			
			
			String[] nextLine;
			int counter = 0;
			while ((nextLine = reader.readNext()) != null) {
				counter++;
				
				if (counter % (dataSize / 10) == 0) {
					long memoryNow = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
					System.out.println(
							String.format("%d records processed after %d millisecond, memory %d",
								counter, System.currentTimeMillis() - startTime, memoryNow - startMemory));
					System.out.println(
							String.format("\t\t\t\t avg lookup: %s, avg process: %s, avg bookkeeping: %s",
									averageLookupTime(), averageProcessTime(), averageBookkeepingTime()));
							
				}
				Vote vote = Vote.fromCsv(nextLine);
				totalRead += 1;
				
				// record lookup time
				long lookupStart = System.currentTimeMillis();
				boolean shouldProcess = strat.shouldProcess(conn, vote);
				totalLookupTime += System.currentTimeMillis() - lookupStart;
				
				// record insertion time
				if (shouldProcess) {
					long processStart = System.currentTimeMillis();

					strat.processVote(conn, vote);

					totalProcessed += 1;
					totalProcessTime += System.currentTimeMillis() - processStart;
				}
				
				// record bookkeeping time
				long updateStart = System.currentTimeMillis();
				strat.updateStates(vote);
				totalBookkeepingTime += System.currentTimeMillis() - updateStart;
			}
			
			long endTime = System.currentTimeMillis();
			long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			System.out.println("total millis: " + (endTime - startTime));
			System.out.println("memory usage: " + (endMemory - startMemory));
			System.out.println("total lookup time: " + totalLookupTime);
			System.out.println("average lookup time: " + averageLookupTime());
			System.out.println("total database process time: " + totalProcessTime);
			System.out.println("average database process time: " + averageProcessTime());
			System.out.println("total bookkeeping time: " + totalBookkeepingTime);
			System.out.println("average bookkeeping time: " + averageBookkeepingTime());
			System.out.println("\n\n\n");
			
			
		} catch (Exception e) {
			System.out.println(e);
			throw e;
		} finally {
			strat.cleanSchema(conn);
		}
	}
	
	public void complete() {
		try {
			conn.close();
		} catch (Exception e) {
			// swallow
		}
	}
	
	private String averageLookupTime() {
		return String.format("%1$,.5f", totalBookkeepingTime * 1.0 / totalRead);
	}

	private String averageProcessTime() {
		return String.format("%1$,.5f", totalProcessTime * 1.0 / totalProcessed);
	}
	
	private String averageBookkeepingTime() {
		return String.format("%1$,.5f", totalBookkeepingTime * 1.0 / totalRead);
	}
}
