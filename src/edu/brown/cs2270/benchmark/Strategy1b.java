package edu.brown.cs2270.benchmark;

public class Strategy1b extends Strategy1 {
	
	@Override
	protected String getCreateTableSql() {
		return Schema.getIndexedSchema();
	}

}
