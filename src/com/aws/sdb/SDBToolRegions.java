/*
 * Provider Region name with city
 */

package com.aws.sdb;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class SDBToolRegions {
	public static  final String[] SDBToolRegions = new String[] {
		"GovCloud",
		"US EAST 1 (Virgina)", 
		"US WEST 1 (N.California)", 
		"US WEST 2 (Oregon)", 
		"EU WEST 1 (Ireland)", 
		"AP Southeast 1 (Singapore)", 
		"AP Southeast 2 (Sydney)", 
		"AP Northeast 1 (Tokyo)", 
		"SA East 1 (Sao Paulo)", 
		"CN North 1 (Beijing)"
	};
	
	/*
	 * Get Region by index
	 */
	public static Region getRegion(int index){
		Regions[] regions = Regions.values();
		if(index > regions.length) index = 1;  //out of bound, default us-east-1
		return Region.getRegion(regions[index]);
	}

}
