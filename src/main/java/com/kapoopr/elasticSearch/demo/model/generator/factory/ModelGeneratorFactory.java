package com.kapoopr.elasticSearch.demo.model.generator.factory;

import com.kapoopr.elasticSearch.demo.model.generators.ModelGenerator;

public interface ModelGeneratorFactory {
	public ModelGenerator getModelGenerator(String modelType);

}
