package edu.brown.cs2270.benchmark;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class Strategy2 extends BenchmarkStrategy {

	private final Set<String> set = new HashSet<String>();

	@Override
	public String getName() {
		return "Strategy 2";
	}

	@Override
	public String getDescription() {
		return "no index, lookup in a local hashset";
	}

	@Override
	protected String getCreateTableSql() {
		return Schema.getUnindexedSchema();
	}

	@Override
	public void processVote(Connection conn, Vote vote) throws SQLException, IOException {
		insertVote(conn, vote);
	}

	@Override
	public boolean shouldProcess(Connection conn, Vote vote) throws SQLException {
		return !set.contains(String.format("%s.%s", vote.getVoter(), vote.getPhone()));
	}

	@Override
	public void updateStates(Vote vote) {
		set.add(String.format("%s.%s", vote.getVoter(), vote.getPhone()));
	}
}
