package cuckoofilter;

public class CuckooFilter {
	private FilterTable table;
	private static final double DEFAULT_FP = 0.01;
	private final static int entriesPerBucket = 4;
	private static int numBuckets;
	private static final int maxNumKicks = 100;
	private static final int bitSize = 8;
	
	private CuckooFilter(FilterTable table){
		this.table = table;
	}
	
	public static class Builder{
		private final int maxKeys;
		private double fpp = DEFAULT_FP;
		
		public Builder(int maxKeys){
			this.maxKeys = maxKeys;
		}
		
		public Builder withFalsePositiveRate(double fpp){
			this.fpp = fpp;
			return this;
		}
		
		public CuckooFilter build(){
			int bucket_size = entriesPerBucket;
			numBuckets = maxKeys/bucket_size;
			FilterTable table = FilterTable.create(numBuckets, bucket_size);
			return new CuckooFilter(table);
		}
	}
	
	public static int fingerprint(int item){
		int unusedBits = Integer.SIZE - bitSize;
		int ret = (item << unusedBits) >>> unusedBits;
		if (ret == 0) {
			ret += 1;
		}
		return ret;
	}
	public static int hash1(int item){
		return (item >>> 4) % numBuckets;
	}
	public static int hash2(int i1, int fingerPrint){
		int altIndex = i1^fingerPrint;
		if(altIndex < 0) altIndex = ~altIndex;
		return altIndex % numBuckets;
	}
	
	public boolean mightContain(int item){
		int fingerPrint = fingerprint(item);
		int i1 = hash1(item);
		int i2 = hash2(i1, fingerPrint);
		
//		if (fingerPrint == 0) {
//			System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
//			System.out.println(item);
//			System.out.println(fingerPrint);
//			System.out.println(i1);
//			System.out.println(i2);
//			System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
//		}
		
		if (table.findFingerprint(i1, fingerPrint) || table.findFingerprint(i2, fingerPrint)) {

			return true;
		}

		return false;
	}
	
	public boolean put(int item){
		int fingerPrint = fingerprint(item);
		int i1 = hash1(item);
		int i2 = hash2(i1, fingerPrint);
		if(table.insertToBucket(i1, fingerPrint) || table.insertToBucket(i2, fingerPrint))
			return true;
		//relocate
		int i = i1;
		for(int k = 0; k < maxNumKicks; k++){
			int victim = table.coverToBucket(i, fingerPrint);
			i = hash2(i1, fingerprint(victim));
			if(table.insertToBucket(i1, fingerPrint) || table.insertToBucket(i2, fingerPrint))
				return true;
		}
		return false;
		
	}
}
