package org.drugis.solrtest;

import java.util.List;

public interface VersioningStore {
	public List<DatasetInfo> getDatasets();
	public List<StudyDoc> getStudies(String datasetUri);
}
