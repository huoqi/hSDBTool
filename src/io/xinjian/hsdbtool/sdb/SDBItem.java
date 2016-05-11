/*
 * This class instead of 'com.amazonaws.services.simpledb.model.Item'
 * Use 'List<SDBAttribute> attributes' instead of 'List<Attribute> attributes'
 * And turn class 'Item' into 'SDBItem'
 */

package io.xinjian.hsdbtool.sdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;

public class SDBItem {
	private String name;
	private List<SDBAttribute> attributes;
	
	public SDBItem() {
		this.name = "";
		this.attributes = new ArrayList<SDBAttribute>();
	}
	public SDBItem(Item item) {
		this.name = item.getName();
		Map<String, SDBAttribute>attrsMap = new HashMap<String, SDBAttribute>();
		for(Attribute attr : item.getAttributes()){  //merge same attribute name's value into a List
			SDBAttribute sdba;
			if(attrsMap.containsKey(attr.getName())) {
				sdba = attrsMap.get(attr.getName());
				sdba.addValue(attr.getValue());
			}
			else sdba = new SDBAttribute(attr);
			attrsMap.put(sdba.getName(), sdba);
		}

		attributes = new ArrayList<SDBAttribute>();
		for(SDBAttribute sdba : attrsMap.values()){
				this.attributes.add(sdba);
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<SDBAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<SDBAttribute> attributes) {
		this.attributes = attributes;
	}

}
