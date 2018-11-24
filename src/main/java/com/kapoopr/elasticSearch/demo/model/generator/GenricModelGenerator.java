package com.kapoopr.elasticSearch.demo.model.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GenricModelGenerator<T> {
	public List<T> getData(List<Map<String, Object>> data, Class<?> classType) {
		return getDataModel(data, classType);
	}

	@SuppressWarnings("unchecked")
	public List<T> getDataModel(List<Map<String, Object>> rows, Class<?> classType) {
		List<T> models = new ArrayList<T>();
		for (Map<String, Object> row : rows) {
			final ObjectMapper mapper = new ObjectMapper(); // jackson's
															// objectmapper
			final T model = (T) mapper.convertValue(row, classType);
			models.add(model);
		}
		return models;

	}
}
