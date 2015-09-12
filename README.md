Solr search for jena-es datasets
================================

Prototype implementation of a search layer on top of jena-es, for Trialverse.

To run, you need:

 * jena-es running on localhost:8080, preferably with some Trialverse datasets.
 * solr 5.3.0 started with `solr start -s ./jena-es-solr`
 * the cache running on localhost:8081

Try e.g. `http://localhost:8081/?q=title:Fava`.

Know issues
-----------

 * The schema.xml for studies needs to set the combination of fields to search by default, so `*:Fava` and `Fava` would be a valid queries.

 * The index is now updated before each query. Performance would be better if the index were updated periodically.

 * Ideally each study should only have an id, not an id and a graph property, but this would mean changing Trialverse to make graph IDs globally unique.
