package org.drugis.solrtest;

import org.apache.solr.client.solrj.beans.Field;

public class StudyDoc {
	@Field
	public final String id, dataset, graph, study, title, description;
	
	public StudyDoc(String dataset, String graph, String study, String title, String description) {
		this.dataset = dataset;
		this.graph = graph;
		this.study = study;
		this.title = title;
		this.description = description;
		this.id = dataset + "/data?graph=" + graph;
	}
}