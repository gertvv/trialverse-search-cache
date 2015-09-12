package org.drugis.solrtest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolrCache {
	private VersioningStore versioning;
	private SearchStore search;

	public SolrCache(VersioningStore versioning, SearchStore search) {
		this.versioning = versioning;
		this.search = search;
	}
	
	public void update() {
		List<DatasetInfo> currentDatasets = versioning.getDatasets();
		List<DatasetInfo> cachedDatasets = search.getDatasets();
		
		Map<String, String> cachedVersions = new HashMap<String, String>();
		for (DatasetInfo info : cachedDatasets) {
			cachedVersions.put(info.id, info.head);
		}
		
		// new or updated datasets
		for (DatasetInfo info : currentDatasets) {
			String cachedVersion = cachedVersions.get(info.id);
			cachedVersions.remove(info.id);
			if (!info.head.equals(cachedVersion)) {
				search.update(info, versioning.getStudies(info.id));
			}
		}
		
		// removed datasets
		for (String id : cachedVersions.keySet()) {
			search.remove(id);
		}
	}
}
