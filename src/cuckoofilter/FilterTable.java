package cuckoofilter;

import java.lang.Math;

public class FilterTable {
	private final int numBuckets;
	private final int entriesPerBucket;
	private final static int bitSize = 8;
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
//		if (bucketIndex == 174) {
//			System.out.println(bits[bucketIndex]);
//		}
		if(bits[bucketIndex] == 0){
			bits[bucketIndex] = fingerPrint;
		}
		else if(bits[bucketIndex] < Math.pow(2, 3*bitSize)){
			bits[bucketIndex] = (bits[bucketIndex] << bitSize ) + fingerPrint;
		}
		else return false;
		return true;
	}
	
	boolean findFingerprint(int index, int fingerPrint){
		int target = bits[index];
		int set = (int) (Math.pow(2,bitSize)) - 1;
		for(; target > 0; target >>= bitSize){
			if((target & set) == fingerPrint) return true;
		}
		return false;
	}
	int coverToBucket(int bucketIndex, int fingerPrint){
		int set = (int) (Math.pow(2,bitSize)) - 1;
		int victim = bits[bucketIndex] & set;
		bits[bucketIndex] = ((bits[bucketIndex] >>> bitSize) << bitSize)+fingerPrint;
		return victim;
	}
}
