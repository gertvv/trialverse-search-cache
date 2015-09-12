package org.drugis.solrtest;

import org.apache.solr.client.solrj.beans.Field;

public class DatasetInfo {
	@Field
	public String id, head, creator;
	
	public DatasetInfo(String id, String head, String creator) {
		this.id = id;
		this.head = head;
		this.creator = creator;
	}
	
	public DatasetInfo() { }
}