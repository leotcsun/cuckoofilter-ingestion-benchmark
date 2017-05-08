package cuckoofilter;

public class FilterTable {
	private final int numBuckets;
	private final int entriesPerBucket;
	private int []bits;
	
	private FilterTable(int numBuckets, int entriesPerBucket){
		this.numBuckets = numBuckets;
		this.entriesPerBucket = entriesPerBucket;
		this.bits = new int [numBuckets];
	}
	
	public static FilterTable create(int numBuckets, int entriesPerBucket){
		return new FilterTable(numBuckets, entriesPerBucket);
	}
	
	boolean insertToBucket(int bucketIndex, int fingerPrint){
		if (bucketIndex == 174) {
			System.out.println(bits[bucketIndex]);
		}
		if(bits[bucketIndex] == 0){
			bits[bucketIndex] = fingerPrint;
		}
		else if(bits[bucketIndex] < 8192){
			bits[bucketIndex] = (bits[bucketIndex] << 4 ) + fingerPrint;
		}
		else return false;
		return true;
	}
	
	boolean findFingerprint(int index, int fingerPrint){
		int target = bits[index];
		int set = 15;
		for(; target > 0; target >>= 4){
			if((target & set) == fingerPrint) return true;
		}
		return false;
	}
	int coverToBucket(int bucketIndex, int fingerPrint){
		int victim = bits[bucketIndex] & 15;
		bits[bucketIndex] = ((bits[bucketIndex] >>> 4) << 4)+fingerPrint;
		return victim;
	}
}
