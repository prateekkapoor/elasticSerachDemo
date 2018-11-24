package com.kapoopr.elasticSearch.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapoopr.elasticSearch.demo.model.Model;

@Service
public class IndexService {
	@Value("${index.batch.size:10}")
	private Integer batchSize;
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	@Resource
	private Map<String, Class<?>> indexTypes;
	@Value(value = "${bean.type:employee}")
	private String classType;
	@Value(value = "${index.name:employees}")
	private String indexName;

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public Map<String, Class<?>> getIndexTypes() {
		return indexTypes;
	}

	public void setIndexTypes(Map<String, Class<?>> indexTypes) {
		this.indexTypes = indexTypes;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public Integer getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(Integer batchSize) {
		this.batchSize = batchSize;
	}

	@PostConstruct
	public void voidCreateIndex() {

		if (!elasticsearchTemplate.indexExists(indexName)) {
			elasticsearchTemplate.createIndex(indexName);
		}
	}

	public long bulkIndex(List<? extends Model> models) throws Exception {
		int counter = 0;
		try {
			List<IndexQuery> queries = new ArrayList<IndexQuery>();
			for (Model model : models) {
				IndexQuery indexQuery = new IndexQuery();
				indexQuery.setId(model.getId().toString());
				indexQuery.setSource(javaToJson(model));
				indexQuery.setIndexName(indexName);
				indexQuery.setType(classType);
				queries.add(indexQuery);
				if (counter % batchSize == 0) {
					elasticsearchTemplate.bulkIndex(queries);
					queries.clear();
					System.out.println("bulkIndex counter : " + counter);
				}
				counter++;
			}
			if (queries.size() > 0) {
				elasticsearchTemplate.bulkIndex(queries);
			}
			elasticsearchTemplate.refresh(indexName);
			System.out.println("bulkIndex completed.");
		} catch (Exception e) {
			System.out.println("IndexerService.bulkIndex e;" + e.getMessage());
			throw e;
		}
		return -1;
	}

	public Class<?> getClassObject() {
		return indexTypes.get(classType);
	}

	private String javaToJson(Model emp) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(emp);
	}
}