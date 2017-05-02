package edu.brown.cs2270.benchmark;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import com.github.mgunlogson.cuckoofilter4j.CuckooFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;


public class Strategy1 extends BenchmarkStrategy {
	
	private final CuckooFilter<byte[]> filter = new CuckooFilter.Builder<>(Funnels.byteArrayFunnel(), 2000000).build();

	protected Strategy1(String db) throws ClassNotFoundException, SQLException, FileNotFoundException {
		super(db);
	}

	@Override
	public String getName() {
		return "Strategy 1";
	}
	
	@Override
	public String getDescription() {
		return "1 Client inserting votes into table, no index, primary key auto-increment";
	}

	@Override
	protected String getCreateTableSql() {
		return "CREATE TABLE votes ("
				+ "voteId INTEGER PRIMARY KEY, "
				+ "voter VARCHAR(255), "
				+ "voteFor VARCHAR(255), "
				+ "phone VARCHAR(10));";
	}

	@Override
	public void processVote(Vote vote) throws SQLException, IOException {
//		
//		String[] nextLine;
//		while ((nextLine = reader.readNext()) != null)  {
//			Vote vote = getVote(nextLine);
//			System.out.println(vote);
//			insertVote(vote);
//		}
		
//		for (int i = 0; i < 200; i++) {
//			Vote vote = new Vote("voter", "votedfor", "1234567");
//			filter.put(String.format("%s.%s.%s", vote.getVoter(), vote.getVoteFor(), vote.getPhoneNumber()).getBytes());
//			filter.mightContain(String.format("%s.%s.%s", vote.getVoter(), vote.getVoteFor(), vote.getPhoneNumber()).getBytes());
//		}
		
//		for (int i = 0; i < 1000; i++) {
//			if (i != 0 && i % 100 == 0) {
//				System.out.println(String.format("%d records inserted...", i));
//			}
//			Vote vote = new Vote("voter", "votedfor", "1234567");
//			insertVote(vote);
//		}
	}
}
