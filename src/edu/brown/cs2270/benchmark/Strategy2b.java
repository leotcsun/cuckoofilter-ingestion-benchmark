package edu.brown.cs2270.benchmark;

public class Strategy2b extends Strategy2 {
	
	@Override
	protected String getCreateTableSql() {
		return Schema.getIndexedSchema();
	}
}
