package edu.brown.cs2270.benchmark;

import java.sql.Connection;

public class Strategy1c extends Strategy1 {

    @Override
    public String getName() {
        return "Strategy 1c";
    }

    @Override
    public String getDescription() {
        return "indexed db, upsert insert";
    }

    @Override
    protected String getCreateTableSql() {
        return Schema.getUniqueIndexedSchema();
    }

    @Override
    public boolean shouldProcess(Connection conn, Vote vote) {
        return true;
    }

    @Override
    public void insertVote(Connection conn, Vote vote) {
        try {
            super.insertVote(conn, vote);
        } catch (Exception e) {
            // ignore insert violation
        }
    }
}
