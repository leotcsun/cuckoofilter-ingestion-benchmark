package edu.brown.cs2270.benchmark;

public class Main {

    private final static String REMOTE_DB_PATH = "jdbc:postgresql://cs2270test.ckf69kqatzre.us-east-1.rds.amazonaws.com:5432/cs2270";
    private final static String USER_NAME = "leosun";
    private final static String PW = "90072000";
	private final static String LOCAL_DB_PATH = "jdbc:sqlite:db.db";

	private final static float FALSE_POS_RATE = (float) 0.001;

	// determines which csv file to read from
	private final static int DATA_SIZE = 100_000;
	private final static String CSV_PATH = String.format("data/votes-%d.csv", DATA_SIZE);

	// set to true if you want the benchmark to pause after each run
	private final static boolean PAUSE_BETWEEN_RUN = false;

	// set to true if AWS postgres is used, false if a local SQLite instance is used
	private final static boolean USE_REMOTE_DB = true;

	public static void main(String[] args) {
		BenchmarkStrategy[] strategies = {
				new Strategy1(),
				new Strategy1b(),
				new Strategy1c(),
				new Strategy2(),
				new Strategy3(DATA_SIZE, FALSE_POS_RATE),
				new Strategy4(DATA_SIZE, FALSE_POS_RATE),
		};

		for (int i = 0; i < strategies.length; i++) {
			BenchmarkStrategy s = strategies[i];
			try {
			    BenchmarkRunner runner;
			    if (USE_REMOTE_DB) {
			        runner = new BenchmarkRunner(REMOTE_DB_PATH, USER_NAME, PW, CSV_PATH, s);
			    } else {
			        runner = new BenchmarkRunner(LOCAL_DB_PATH, CSV_PATH, s);
			    }

				runner.run(DATA_SIZE);
				runner.complete();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				strategies[i] = null;
				System.gc();
			}

			if (PAUSE_BETWEEN_RUN) {
                System.out.println("Press any key to continue: ");
                try {
                    System.in.read();
                } catch (Exception e) {
                }
			}
		}

	}
}
