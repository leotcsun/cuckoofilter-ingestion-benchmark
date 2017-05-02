package edu.brown.cs2270.benchmark;

public class Strategy3b extends Strategy3 {
	
	public Strategy3b(int dataSize) {
		super(dataSize);
	}

	@Override
	protected String getCreateTableSql() {
		return Schema.getIndexedSchema();
	}
}

