package edu.brown.cs2270.benchmark;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

public class Strategy4 extends BenchmarkStrategy {

    private final BloomFilter<Integer> filter;

    public Strategy4(int dataSize, float rate) {
        super();
        this.filter = BloomFilter.create(Funnels.integerFunnel(), dataSize, rate);
    }

    @Override
    public String getName() {
        return "Strategy 4";
    }

    @Override
    public String getDescription() {
        return "no index, use bloom filter for local lookup";
    }

    @Override
    protected String getCreateTableSql() {
        return Schema.getUnindexedSchema();
    }

    @Override
    public boolean shouldProcess(Connection conn, Vote vote) throws SQLException {
        return !filter.mightContain(vote.hashCode());
    }

    @Override
    public void processVote(Connection conn, Vote vote) throws SQLException, IOException {
        insertVote(conn, vote);
    }

    @Override
    public void updateStates(Vote vote) {
        filter.put(vote.hashCode());
    }

}
