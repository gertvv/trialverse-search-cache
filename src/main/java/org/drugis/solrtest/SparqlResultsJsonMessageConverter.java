package org.drugis.solrtest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;


public class SparqlResultsJsonMessageConverter extends MappingJackson2HttpMessageConverter {
	public static final MediaType RESULTS_JSON = MediaType.valueOf("application/sparql-results+json");

	public SparqlResultsJsonMessageConverter() {
		super();
		setSupportedMediaTypes(Collections.singletonList(RESULTS_JSON));
	}
}
