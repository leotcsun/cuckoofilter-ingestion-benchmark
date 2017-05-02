package edu.brown.cs2270.benchmark;

public class Vote {
	
	private final String voter;
	private final String contestant;
	private final String phone;
	
	public Vote(String voter, String contestant, String phone) {
		this.voter = voter;
		this.contestant = contestant;
		this.phone = phone;
	}
	
	public String getVoter() {
		return voter;
	}

	public String getContestant() {
		return contestant;
	}

	public String getPhone() {
		return phone;
	}
	
	public static Vote fromCsv(String[] line) {
		return new Vote(line[0], line[1], line[2]);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((voter == null) ? 0 : voter.hashCode());
		result = prime * result + ((contestant == null) ? 0 : contestant.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		return result;
	}

}
