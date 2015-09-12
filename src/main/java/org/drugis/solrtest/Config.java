package org.drugis.solrtest;
import java.util.List;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;


@Configuration
public class Config extends WebMvcAutoConfigurationAdapter {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new SparqlResultsJsonMessageConverter());
        super.configureMessageConverters(converters);
    }
}
