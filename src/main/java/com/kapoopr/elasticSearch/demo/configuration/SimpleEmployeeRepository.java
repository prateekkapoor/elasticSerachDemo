package com.kapoopr.elasticSearch.demo.configuration;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;

@Component
public class SimpleEmployeeRepository {
	@Value(value = "${elastic.index.name:employees}")
	private String indexName;
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	@Value(value = "${elastic.index.Type:Employee}")
	private String indexType;

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	public ElasticsearchTemplate getElasticsearchTemplate() {
		return elasticsearchTemplate;
	}

	public void setElasticsearchTemplate(ElasticsearchTemplate elasticsearchTemplate) {
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public SearchHit[] searchText(String name, String text, Class<?> classType) {

		SearchResponse searchResponse = elasticsearchTemplate.getClient().prepareSearch(indexName).setTypes(indexType)
				.setQuery(QueryBuilders.matchPhraseQuery(name, text)).execute().actionGet();
		return searchResponse.getHits().getHits();

	}

}
