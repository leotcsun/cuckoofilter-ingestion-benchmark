package edu.brown.cs2270.benchmark;

public class Vote {
	
	private final String voter;
	private final String voteFor;
	private final String phoneNumber;
	
	public Vote(String voter, String voteFor, String phoneNumber) {
		this.voter = voter;
		this.voteFor = voteFor;
		this.phoneNumber = phoneNumber;
	}
	
	public String getVoter() {
		return voter;
	}

	public String getVoteFor() {
		return voteFor;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

}
