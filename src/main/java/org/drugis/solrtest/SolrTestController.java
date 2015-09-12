package org.drugis.solrtest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = {SolrAutoConfiguration.class})
@RestController
@ComponentScan
public class SolrTestController {
    @RequestMapping("/")
    @ResponseBody
    Object home() {
    	SolrClient solrClient = new HttpSolrClient("http://localhost:8983/solr/gettingstarted");
    	
    	RestTemplate rest = new RestTemplate();
    	List<Map<String, String>> datasets = rest.getForObject("http://localhost:8080/datasets", List.class);
    	Map<String, List> result = new HashMap<String, List>();
    	for (Map<String, String> dataset : datasets) {
    		String uri = dataset.get("id");
    		String head = dataset.get("head");
    		Map<String, String> urlVariables = new HashMap<String, String>();
    		urlVariables.put("query",
    				"PREFIX ont: <http://trials.drugis.org/ontology#>\n" + 
    				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
    				"SELECT * WHERE { GRAPH ?graph { ?study a ont:Study ; rdfs:label ?title ; rdfs:comment ?description } }");
    		try {
    			HttpHeaders headers = new HttpHeaders();
    			headers.setAccept(Arrays.asList(SparqlResultsJsonMessageConverter.RESULTS_JSON));

    			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
				ResponseEntity<Map> forObject = rest.exchange(uri + "/query?query={query}",
    					HttpMethod.GET, entity, Map.class, urlVariables);
    					
				List<StudyDoc> docs = simplify(forObject.getBody(), uri);
				result.put(uri, docs);
				for (StudyDoc doc : docs) {
					solrClient.addBean(doc);
				}
    		} catch (Exception e) {
    			System.err.println("Failed on " + uri);
    			e.printStackTrace();
    		}
    	}
    	try {
			solrClient.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
//    	return datasets;
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

	public static void main(String[] args) throws Exception {
        SpringApplication.run(SolrTestController.class, args);
    }

}