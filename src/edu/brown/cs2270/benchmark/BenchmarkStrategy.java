package edu.brown.cs2270.benchmark;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public abstract class BenchmarkStrategy {

	public abstract String getName();

	public abstract String getDescription();

	protected abstract String getCreateTableSql();

	public void setupSchema(Connection conn) throws SQLException {
		System.out.println("Creating the votes table");
		final Statement stat = conn.createStatement();
		stat.executeUpdate("DROP TABLE IF EXISTS votes;");

		final String query = getCreateTableSql();
		stat.executeUpdate(query);
		stat.close();
		System.out.println("Table created");
	}

	public void cleanSchema(Connection conn) throws SQLException {
		final Statement stat = conn.createStatement();
		stat.executeUpdate("DROP TABLE IF EXISTS votes;");
		stat.close();
	}

	public abstract boolean shouldProcess(Connection conn, Vote vote) throws SQLException;

	public abstract void processVote(Connection conn, Vote vote) throws SQLException, IOException;

	public abstract void updateStates(Vote vote);

	public void insertVote(Connection conn, Vote vote) throws SQLException {
		final String query = "INSERT INTO votes (voter, contestant, phone) VALUES (?, ?, ?);";

		PreparedStatement prep = conn.prepareStatement(query);
		prep.setString(1, vote.getVoter());
		prep.setString(2, vote.getContestant());
		prep.setString(3, vote.getPhone());

		prep.executeUpdate();
		prep.close();
	}

	public int checkAllInserts(Connection conn) throws SQLException {
	    final String query = "SELECT COUNT(*) FROM votes;";

	    final PreparedStatement prep = conn.prepareStatement(query);
	    final ResultSet result = prep.executeQuery();

	    if (result.next()) {
	        return result.getInt(1);
	    } else {
	        return 0;
	    }
	}

	public int checkForDuplicates(Connection conn) throws SQLException {
	    final String query = "SELECT voter, phone, COUNT(*) FROM votes GROUP BY voter, phone HAVING COUNT(*) > 1;";

	    final PreparedStatement prep = conn.prepareStatement(query);
	    final ResultSet result = prep.executeQuery();

	    int duplicateCount = 0;
	    while (result.next()) {
	        System.out.println(result.getString(1) + result.getString(2) + " " + result.getInt(3));
	        duplicateCount += result.getInt(3);
	    }

	    return duplicateCount;
	}
}
