package edu.brown.cs2270.benchmark;

public class Strategy1b extends Strategy1 {

    @Override
    public String getName() {
        return "Strategy 1b";
    }

    @Override
    public String getDescription() {
        return "table has index, db lookup to check for duplicate";
    }

	@Override
	protected String getCreateTableSql() {
		return Schema.getIndexedSchema();
	}
}
