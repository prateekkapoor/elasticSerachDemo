package com.kapoopr.elasticSearch.demo.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "prefix")
@Component
public class PropertiesConfiguration {

}

