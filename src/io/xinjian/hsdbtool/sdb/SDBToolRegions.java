/*
 * Provider Region name with city
 */

package io.xinjian.hsdbtool.sdb;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class SDBToolRegions {
	public static final String[] SDBToolRegions = new String[] {
		"US EAST 1 (Virgina)", 
		"US WEST 1 (N.California)", 
		"US WEST 2 (Oregon)", 
		"EU WEST 1 (Ireland)", 
		"AP Northeast 1 (Tokyo)", 
		"AP Southeast 1 (Singapore)", 
		"AP Southeast 2 (Sydney)", 
		"SA East 1 (Sao Paulo)"
	};
	
	public static final String[] SDBToolEndpoints = new String[] {
			"us-east-1",
			"us-west-1",
			"us-west-2",
			"eu-west-1",
			"ap-northeast-1",
			"ap-southeast-1",
			"ap-southeast-2",
			"sa-east-1"
	};
	
	/*
	 * Get Region by index
	 */
	public static Region getRegion(int index){
		if(index > SDBToolRegions.length) index = 2;  //out of bound, default US WEST 2 (Oregon)
		String endpoint = SDBToolEndpoints[index];
		return Region.getRegion(Regions.fromName(endpoint));
	}
	
	/*
	 * Get Region by endpoint
	 */
	public static Region getRegion(String endpoint){
		return Region.getRegion(Regions.fromName(endpoint));
	}

}
