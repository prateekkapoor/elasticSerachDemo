package com.kapoopr.elasticSearch.demo.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.kapoopr.elasticSearch.demo.services.file.watcher", "com.kapoopr.elasticSearch.demo.util",
        "com.kapoopr.elasticSearch.demo.services.file.parser", "com.kapoopr.elasticSearch.demo.services.elastic.writer",
        "com.kapoopr.elasticSearch.demo.repository", "com.kapoopr.elasticSearch.demo.configuration", "com.kapoopr.elasticSearch.demo.controllers",
        "com.kapoopr.elasticSearch.demo.beans", "com.kapoopr.elasticSearch.demo.services.bean.service", "com.kapoopr.elasticSearch.demo.job"})
/**
 * Spring boot start up class
 */
public class SpringBootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }
}
