/*
 * class for operating AWS SimpleDB
 */

package com.aws.sdb;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeleteDomainRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.ListDomainsResult;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

public class SDBDao {
	Region region;
	AWSCredentials credentials;
	AmazonSimpleDBClient sdb;
	
	public SDBDao() {
		region = Region.getRegion(Regions.US_EAST_1);
		connection();
	}

	public SDBDao(Region region) {
		connection();
		this.region = region;
	}

	private void connection() {
		credentials = null;
		try{
			credentials = new ProfileCredentialsProvider("admin").getCredentials();
		} catch (Exception e){
			
		}
		sdb = new AmazonSimpleDBClient(credentials);
	}
	
	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region){
		this.region = region;
		sdb.setRegion(region);
	}
	
	public void setRegion(String region){
		this.region = Region.getRegion(Regions.valueOf(region));
		sdb.setRegion(this.region);
	}
	
	public List<String> getDomainsList() throws Exception {
		setRegion(region);
		ListDomainsResult listDomains = sdb.listDomains();
		return listDomains.getDomainNames();
	}

	public List<Item> select(String sql) throws Exception {
		SelectResult sr = null;
		sr = sdb.select(new SelectRequest(sql));
		if(sr == null) return new ArrayList<Item>();
		return sr.getItems();
	}
	
	public void putAttribute(String domain, String item, String attrName, String attrValue, boolean replace) throws Exception{
		ReplaceableAttribute rAttribute = new ReplaceableAttribute(attrName, attrValue, replace);
		List<ReplaceableAttribute>attributes = new ArrayList<ReplaceableAttribute>();
		attributes.add(rAttribute);
		sdb.putAttributes(new PutAttributesRequest(domain, item, attributes));
	}
	
	public void createDomain(String domain) throws Exception{
		sdb.createDomain(new CreateDomainRequest(domain));
	}
	public void deleteDomain(String domain) throws Exception {
		sdb.deleteDomain(new DeleteDomainRequest(domain));
	}
	
}
