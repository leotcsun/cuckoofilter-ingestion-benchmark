package edu.brown.cs2270.benchmark;

public class Schema {

	public static String getUnindexedSchema() {
		return "CREATE TABLE votes ("
				+ "voteId SERIAL PRIMARY KEY, "
				+ "voter VARCHAR(255), "
				+ "contestant VARCHAR(255), "
				+ "phone VARCHAR(10));";
	}
	
	public static String getIndexedSchema() {
		return "CREATE TABLE votes ("
				+ "voteId SERIAL PRIMARY KEY, "
				+ "voter VARCHAR(255), "
				+ "contestant VARCHAR(255), "
				+ "phone VARCHAR(10));"
				+ "CREATE INDEX voter_phone_index "
				+ "ON votes (voter, phone);";
	}
}
