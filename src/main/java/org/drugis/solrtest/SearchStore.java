package org.drugis.solrtest;

import java.util.List;

public interface SearchStore {
	public List<DatasetInfo> getDatasets();
	public void remove(String datasetUri);
	public void update(DatasetInfo info, List<StudyDoc> docs);
	public List<StudyDoc> search(String q);
}