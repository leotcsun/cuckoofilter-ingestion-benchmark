package edu.brown.cs2270.benchmark;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import cuckoofilter.*;

public class Strategy3b extends BenchmarkStrategy
{
	private final CuckooFilter filter;

	public Strategy3b(int dataSize, float rate) {
		super();
		this.filter = new CuckooFilter.Builder(dataSize)
				.withFalsePositiveRate(rate)
				.build();
	}

	@Override
	public String getName() {
		return "Strategy 3b";
	}

	@Override
	public String getDescription() {
		return "no index, use local dd cuckoo filter for lookup";
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
		return !filter.mightContain(vote.hashCode());
	}

	@Override
	public void updateStates(Vote vote) {
		filter.put(vote.hashCode());
	}
}
