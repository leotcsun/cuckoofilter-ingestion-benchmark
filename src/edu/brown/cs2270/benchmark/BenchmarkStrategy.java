package edu.brown.cs2270.benchmark;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


import au.com.bytecode.opencsv.CSVReader;

public abstract class BenchmarkStrategy {
	
	protected final Connection conn;
	
	protected BenchmarkStrategy(String db) throws ClassNotFoundException, SQLException, FileNotFoundException {
		System.out.println("bootstrapping strategy " + this.getName());
		Class.forName("org.sqlite.JDBC");
		
		URL dbUrl = getClass().getResource(db);
		this.conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl.getPath());
	}
	
	public abstract String getName();
	
	public abstract String getDescription();
	
	protected abstract String getCreateTableSql();
	
	public void setupSchema() throws SQLException {
		System.out.println("Creating the votes table");
		final Statement stat = conn.createStatement();
		stat.executeUpdate("DROP TABLE IF EXISTS votes;");

		final String query = getCreateTableSql();
		stat.executeUpdate(query);
		System.out.println("Table created");
	}

	public void cleanSchema() throws SQLException {
		final Statement stat = conn.createStatement();
		stat.executeUpdate("DROP TABLE votes;");
		conn.close();
	}
	
	public abstract void processVote(Vote vote) throws SQLException, IOException;
	
	public void insertVote(Vote vote) throws SQLException {
		final String query = "INSERT OR IGNORE INTO votes (voter, voteFor, phone) VALUES (?, ?, ?);";
	
		PreparedStatement prep = conn.prepareStatement(query);
		prep.setString(1, vote.getVoter());
		prep.setString(2, vote.getVoteFor());
		prep.setString(3, vote.getPhoneNumber());
		
		prep.execute();
	}

	protected static Vote getVote(String[] data) {
		return new Vote(data[0], data[1], data[2]);
	}
}
