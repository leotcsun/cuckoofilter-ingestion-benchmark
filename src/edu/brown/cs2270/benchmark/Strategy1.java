package edu.brown.cs2270.benchmark;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Strategy1 extends BenchmarkStrategy {

	@Override
	public String getName() {
		return "Strategy 1";
	}

	@Override
	public String getDescription() {
		return "no table index, ask for db for duplicates";
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
		final String query = "SELECT COUNT(*) FROM votes WHERE "
				+ "voter = ? AND contestant = ? AND phone = ?;";

		PreparedStatement prep = conn.prepareStatement(query);
		prep.setString(1, vote.getVoter());
		prep.setString(2, vote.getContestant());
		prep.setString(3, vote.getPhone());

		ResultSet result = prep.executeQuery();
		return result.next() && result.getInt(1) == 0;
	}

	@Override
	public void updateStates(Vote vote) {
		return;
	}
}
