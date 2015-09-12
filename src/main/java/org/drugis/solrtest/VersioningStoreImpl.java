package org.drugis.solrtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class VersioningStoreImpl implements VersioningStore {

	private RestTemplate rest = new RestTemplate();

	@Override
	public List<DatasetInfo> getDatasets() {
		List<Map<String, String>> datasets = rest.getForObject("http://localhost:8080/datasets", List.class);
		List<DatasetInfo> result = new ArrayList<>();
		for (Map<String, String> dataset : datasets) {
			result.add(new DatasetInfo(dataset.get("id"), dataset.get("head"), dataset.get("creator")));
		}
		return result;
	}

	@Override
	public List<StudyDoc> getStudies(String datasetUri) {
		List<StudyDoc> result = new ArrayList<>();
		
		Map<String, String> urlVariables = new HashMap<String, String>();
		urlVariables.put("query",
				"PREFIX ont: <http://trials.drugis.org/ontology#>\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
				"SELECT * WHERE { GRAPH ?graph { ?study a ont:Study ; rdfs:label ?title ; rdfs:comment ?description } }");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(SparqlResultsJsonMessageConverter.RESULTS_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		
		try {
			ResponseEntity<Map> forObject = rest.exchange(datasetUri + "/query?query={query}",
					HttpMethod.GET, entity, Map.class, urlVariables);
					
			return simplify(forObject.getBody(), datasetUri);
		} catch (Exception e) {
			System.err.println("Failed on " + datasetUri);
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

    private List<StudyDoc> simplify(Map<String, Object> body, String datasetId) {
    	List<Map<String, Map<String, String>>> raw = (List<Map<String, Map<String, String>>>) ((Map)body.get("results")).get("bindings");
    	List<StudyDoc> info = new ArrayList<StudyDoc>();
    	for (Map<String, Map<String, String>> x : raw) {
    		StudyDoc studyInfo = new StudyDoc(
    				datasetId,
    				x.get("graph").get("value"),
    				x.get("study").get("value"),
    				x.get("title").get("value"),
    				x.get("description").get("value"));
    		info.add(studyInfo);
    	}
		return info;
	}
}
