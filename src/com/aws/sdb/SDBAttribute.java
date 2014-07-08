/*
 * This class instead of  com.amazonaws.services.simpledb.model.Attribute
 * Use List<Sting> values instead of Sting value;
 */

package com.aws.sdb;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;

import com.amazonaws.services.simpledb.model.Attribute;

public class SDBAttribute {

	private String name;
	private List<String>values;

	public SDBAttribute() {
		this.name = "";
		values = new ArrayList<String>();
	}

	public SDBAttribute(String name, List<String> values) {
		this.name = name;
		this.values = values;
	}
	
	public SDBAttribute(Attribute attribute) {
		this.name = attribute.getName();
		this.values = new ArrayList<String>();
		values.add(attribute.getValue());
	}
	
	public void addValue(String value) {
		this.values.add(value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getValues() {
		Collections.sort(values, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}			
		});
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

}
