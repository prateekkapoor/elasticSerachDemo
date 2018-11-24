package com.kapoopr.elasticSearch.demo.services.elastic.writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapoopr.elasticSearch.demo.beans.Employee;
import com.kapoopr.elasticSearch.demo.model.Model;
import com.kapoopr.elasticSearch.demo.model.generator.factory.ModelGeneratorFactory;
import com.kapoopr.elasticSearch.demo.model.generators.ModelGenerator;
import com.kapoopr.elasticSearch.demo.repository.CreateElasticIndex;
import com.kapoopr.elasticSearch.demo.repository.IndexService;
import com.kapoopr.elasticSearch.demo.util.FileUtil;

/**
 * 
 * @author pkapo7
 *
 */
@Component
@Scope("prototype")
public class ElasticWriter {

	final static Logger LOGGER = Logger.getLogger(ElasticWriter.class);
	@Autowired
	private FileUtil fileUtil;
	@Value(value = "${json.root.name:employee}")
	private String jsonRootName;
	@Value(value = "${elastic.index.name:employees}")
	private String indexName;
	@Value(value = "${elastic.index.Type:Employee}")
	private String indexType;
	@Autowired
	private CreateElasticIndex createElasticIndex;
	@Autowired
	private IndexService indexService;
	@Autowired
	@Qualifier("modelGeneratorFactory")
	ModelGeneratorFactory modelGeneratorFactory;

	public ModelGeneratorFactory getModelGeneratorFactory() {
		return modelGeneratorFactory;
	}

	public void setModelGeneratorFactory(ModelGeneratorFactory modelGeneratorFactory) {
		this.modelGeneratorFactory = modelGeneratorFactory;
	}

	public IndexService getIndexService() {
		return indexService;
	}

	public void setIndexService(IndexService indexService) {
		this.indexService = indexService;
	}

	public CreateElasticIndex getCreateElasticIndex() {
		return createElasticIndex;
	}

	public void setCreateElasticIndex(CreateElasticIndex createElasticIndex) {
		this.createElasticIndex = createElasticIndex;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	public String getJsonRootName() {
		return jsonRootName;
	}

	public void setJsonRootName(String jsonRootName) {
		this.jsonRootName = jsonRootName;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public void writeData(String file, String type) throws JsonParseException, JsonMappingException, IOException {
		String query = "";
		HashMap<String, List<Map<String, Object>>> map = fileUtil.fileToJson(file);
		Map<String, Object> header = new HashMap<String, Object>();
		header.put("index", indexName);
		header.put("_index", Calendar.getInstance().getTimeInMillis());
		header.put("_type", indexType);
		List<Map<String, Object>> list = map.get(jsonRootName);
		String headerStr = mapToJson(header);
		for (Map<String, Object> object : list) {
			query += headerStr + "\n";
			query += mapToJson(object) + "\n";
		}
		createElasticIndex.createIndex(map);
		LOGGER.debug("query:" + query);
	}

	public void writeDataModel(String file, String type) throws JsonParseException, JsonMappingException, IOException {
		HashMap<String, List<Map<String, Object>>> map = fileUtil.fileToJson(file);
		List<Map<String, Object>> list = map.get(jsonRootName);
		List<Employee> employees = new ArrayList<>();
		for (Map<String, Object> object : list) {
			Employee e = new Employee((Integer) object.get("id"));
			e.setId((Integer) object.get("id"));
			e.setName((String) object.get("name"));
			e.setDetails((String) object.get("details"));
			employees.add(e);
		}
		try {
			indexService.bulkIndex(employees);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public void writeGenericDataModel(String file, String type)
			throws JsonParseException, JsonMappingException, IOException {
		HashMap<String, List<Map<String, Object>>> map = fileUtil.fileToJson(file);
		List<Map<String, Object>> list = map.get(jsonRootName);
		ModelGenerator modelGenerator = modelGeneratorFactory.getModelGenerator(indexType);
		List<? extends Model> models = modelGenerator.getDataModels(list);

		try {
			indexService.bulkIndex(models);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private String mapToJson(Map<String, Object> map) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString;
		try {
			jsonString = mapper.writeValueAsString(map);
		} catch (IOException e) {
			jsonString = "fail";
		}
		return jsonString;
	}
}
