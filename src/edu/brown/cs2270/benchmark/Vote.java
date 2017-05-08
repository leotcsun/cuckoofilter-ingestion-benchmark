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
        result = prime * result + ((contestant == null) ? 0 : contestant.hashCode());
        result = prime * result + ((phone == null) ? 0 : phone.hashCode());
        result = prime * result + ((voter == null) ? 0 : voter.hashCode());
//        System.out.println("hash code is: " + result);

        return result;
    }

	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vote other = (Vote) obj;
        if (contestant == null) {
            if (other.contestant != null)
                return false;
        } else if (!contestant.equals(other.contestant))
            return false;
        if (phone == null) {
            if (other.phone != null)
                return false;
        } else if (!phone.equals(other.phone))
            return false;
        if (voter == null) {
            if (other.voter != null)
                return false;
        } else if (!voter.equals(other.voter))
            return false;
        return true;
    }
}
