package com.kapoopr.elasticSearch.demo.model.generators;

import java.util.List;
import java.util.Map;

import com.kapoopr.elasticSearch.demo.model.Model;

/**
 * This class generates Model based on the Map.
 */
public interface ModelGenerator {
    public List<? extends Model> getDataModels(List<Map<String, Object>> data);

}
