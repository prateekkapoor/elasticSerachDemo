package com.kapoopr.elasticSearch.demo.repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Repository;

/**
 * This class is used to create elastic search index based on the java bean type.
 */
@Repository
public class CreateElasticIndex {
    @Autowired
    private ElasticsearchTemplate template;
    @Resource
    private Map<String, Class<?>> indexTypes;
    @Value(value = "${bean.type:Employee}")
    private String classType;

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public ElasticsearchTemplate getTemplate() {
        return template;
    }

    public void setTemplate(ElasticsearchTemplate template) {
        this.template = template;
    }

    public Map<String, Class<?>> getIndexTypes() {
        return indexTypes;
    }

    public void setIndexTypes(Map<String, Class<?>> indexTypes) {
        this.indexTypes = indexTypes;
    }

    @PostConstruct
    public void createElasticIndex() {
        Class<?> classType = getClassObject();
        template.createIndex(classType);

    }

    public Class<?> getClassObject() {
        return indexTypes.get(classType);
    }

    public void createIndex(HashMap<String, List<Map<String, Object>>> map) {
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(map).withIndexName("test-employee-test")
                .withId("1").withType("contact-Employee-type").build();

        template.bulkIndex(Arrays.asList(indexQuery));
        template.refresh("test-employee-test");
    }
}
