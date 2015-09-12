package org.drugis.solrtest;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;

public class SearchStoreImpl implements SearchStore {
	SolrClient datasetsClient = new HttpSolrClient("http://localhost:8983/solr/datasets");
	SolrClient studiesClient = new HttpSolrClient("http://localhost:8983/solr/studies");

	@Override
	public List<DatasetInfo> getDatasets() {
		SolrQuery query = new SolrQuery("*:*");
		try {
			QueryResponse response = datasetsClient.query(query);
			return response.getBeans(DatasetInfo.class);
		} catch (SolrServerException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void remove(String datasetUri) {
		try {
			studiesClient.deleteByQuery("dataset:\"" + datasetUri + "\"");
			datasetsClient.deleteById(datasetUri);
			datasetsClient.commit();
			studiesClient.commit();
		} catch (SolrServerException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(DatasetInfo info, List<StudyDoc> docs) {
		try {
			remove(info.id);
			for (StudyDoc doc: docs) {
				studiesClient.addBean(doc);
			}
			datasetsClient.addBean(info);
			studiesClient.commit();
			datasetsClient.commit();
		} catch (SolrServerException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<StudyDoc> search(String q) {
		SolrQuery query = new SolrQuery(q);
		try {
			QueryResponse response = studiesClient.query(query);
			return response.getBeans(StudyDoc.class);
		} catch (SolrServerException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}