package edu.brown.cs2270.benchmark;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import au.com.bytecode.opencsv.CSVReader;


public class BenchmarkRunner {
	private final BenchmarkStrategy strat;
	protected final CSVReader reader;
	protected final Connection conn;

	// total measurements are used to print out overall performance averages
	private long totalRead = 0;
	private long totalProcessed = 0;
	private long totalLookupTime = 0;
	private long totalProcessTime = 0;
	private long totalBookkeepingTime = 0;

	// current measurements are used to print out averages within a fraction of the total ingestion (ie. every 10%)
	private long currentLookupTime = 0;
	private long currentProcessTime = 0;
	private long currentBookkeepingTime = 0;
	private long currentProcessed = 0;
	private long currentRead = 0;

	public BenchmarkRunner(String remoteDb, String username, String pw, String csv, BenchmarkStrategy strat) throws FileNotFoundException, SQLException {
		this.strat = strat;

		DriverManager.registerDriver(new org.postgresql.Driver());
		this.conn = DriverManager.getConnection(remoteDb, username, pw);
		System.out.println("database connection is " + this.conn);

		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(csv);
		System.out.println("data file stream is " + stream);
		this.reader = new CSVReader(new BufferedReader(new InputStreamReader(stream)));
	}

	public BenchmarkRunner(String localDb, String csv, BenchmarkStrategy strat) throws SQLException {
	    this.strat = strat;

	    this.conn = DriverManager.getConnection(localDb);
        System.out.println("database connection is " + this.conn);

        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(csv);
        System.out.println("data file stream is " + stream);
        this.reader = new CSVReader(new BufferedReader(new InputStreamReader(stream)));
    }

	public void run(int dataSize) throws SQLException, IOException {
		try {
			strat.setupSchema(conn);

			System.out.println(String.format("Running benchmark with strategy %s\n%s\n", strat.getName(), strat.getDescription()));
			long startTime = System.nanoTime() / 1000;
			long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

			String[] nextLine;
			int counter = 0;
			while ((nextLine = reader.readNext()) != null) {
				counter++;

				// print out some stats once every 10% of the data has been processed
				if (counter % (dataSize / 10) == 0) {
					long memoryNow = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
					System.out.println(
							String.format("%d records processed after %d millisecond, memory %d",
								counter, System.nanoTime() / 1000 - startTime, memoryNow - startMemory));
					System.out.println(
							String.format("\t\t\t\t avg lookup: %s, avg process: %s, avg bookkeeping: %s",
									currentLookupTime(), currentProcessTime(), currentBookkeepingTime()));

					currentLookupTime = 0;
					currentProcessTime = 0;
					currentBookkeepingTime = 0;

					currentRead = 0;
					currentProcessed = 0;
				}
				Vote vote = Vote.fromCsv(nextLine);
				totalRead += 1;
				currentRead += 1;

				// record lookup time
				long lookupStart = System.nanoTime() / 1000;
				// decide if we should insert this record
				boolean shouldProcess = strat.shouldProcess(conn, vote);
				totalLookupTime += System.nanoTime() / 1000 - lookupStart;
				currentLookupTime += System.nanoTime() / 1000 - lookupStart;

				// record insertion time
				if (shouldProcess) {
					long processStart = System.nanoTime() / 1000;

					strat.processVote(conn, vote);

					totalProcessed += 1;
					totalProcessTime += System.nanoTime() / 1000 - processStart;
					currentProcessed += 1;
					currentProcessTime += System.nanoTime() / 1000 - processStart;
				}

				// record bookkeeping time
				long updateStart = System.nanoTime() / 1000;
				// update internal structures (hashset, filters etc)
				strat.updateStates(vote);
				totalBookkeepingTime += System.nanoTime() / 1000 - updateStart;
				currentBookkeepingTime += System.nanoTime() / 1000 - updateStart;
			}

			long endTime = System.nanoTime() / 1000;
			long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			System.out.println("total millis: " + (endTime - startTime));
			System.out.println("memory usage: " + (endMemory - startMemory));
			System.out.println("\n\n");
			System.out.println("total lookup time: " + totalLookupTime);
			System.out.println("\t\t\taverage lookup time: " + averageLookupTime());
			System.out.println("total database process time: " + totalProcessTime);
			System.out.println("\t\t\taverage database process time: " + averageProcessTime());
			System.out.println("total bookkeeping time: " + totalBookkeepingTime);
			System.out.println("\t\t\taverage bookkeeping time: " + averageBookkeepingTime());

			int insertCount = strat.checkAllInserts(conn);
			System.out.println(String.format("Total of %d records in the database", insertCount));
			int duplicateCount = strat.checkForDuplicates(conn);
			System.out.println(String.format("\t\t\tFound %d duplicates in the database", duplicateCount));
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

	private String currentLookupTime() {
		return String.format("%1$,.5f", currentLookupTime * 1.0 / currentRead);
	}

	private String currentProcessTime() {
		return String.format("%1$,.5f", currentProcessTime * 1.0 / currentProcessed);
	}

	private String currentBookkeepingTime() {
		return String.format("%1$,.5f", currentBookkeepingTime * 1.0 / currentRead);
	}

	private String averageLookupTime() {
		return String.format("%1$,.5f", totalLookupTime * 1.0 / totalRead);
	}

	private String averageProcessTime() {
		return String.format("%1$,.5f", totalProcessTime * 1.0 / totalProcessed);
	}

	private String averageBookkeepingTime() {
		return String.format("%1$,.5f", totalBookkeepingTime * 1.0 / totalRead);
	}
}
