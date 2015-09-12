package org.drugis.solrtest;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {SolrAutoConfiguration.class})
@RestController
@ComponentScan
public class SolrTestController {
    @RequestMapping("/")
    @ResponseBody
    List<StudyDoc> home(@RequestParam String q) {
    	VersioningStore versioning = new VersioningStoreImpl();
    	SearchStore search = new SearchStoreImpl();
    	SolrCache cache = new SolrCache(versioning, search);
    	// For convenience, update before each query.
    	// Probably should run periodically instead.
    	cache.update();
    	return search.search(q);
    }

	public static void main(String[] args) throws Exception {
        SpringApplication.run(SolrTestController.class, args);
    }
}