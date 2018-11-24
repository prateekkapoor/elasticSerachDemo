package com.kapoopr.elasticSearch.demo.configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.kapoopr.elasticSearch.demo.beans.BMCError;
import com.kapoopr.elasticSearch.demo.beans.Employee;

@Configuration
@EntityScan(basePackages = {"com.kapoopr.elasticSearch.demo.repository"})
@EnableAutoConfiguration
@EnableElasticsearchRepositories(basePackages = "{com.kapoopr.elasticSearch.demo.repository.employee}")
public class ElasticConfiguration {
    @Value("${spring.data.elasticsearch.cluster-name:my-application}")
    private String clusterName;
    @Value("${spring.data.elasticsearch.cluster-nodes:localhost,9300}")
    private String clusterNodes;

    /**
     * Configuration of java POJO to elastic search index
     *
     * @return Map<String, Class<?>>
     */
    @Bean
    public Map<String, Class<?>> indexTypes() {
        Map<String, Class<?>> map = new HashMap<>();
        map.put("Employee", Employee.class);
        map.put("BMC", BMCError.class);
        // etc
        return map;
    }

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate() throws UnknownHostException {
        String server = clusterNodes.split(",")[0];
        Integer port = Integer.parseInt(clusterNodes.split(",")[1]);
        Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).build();
        Client client = TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(server), port));
        return new ElasticsearchTemplate(client);

    }
}
