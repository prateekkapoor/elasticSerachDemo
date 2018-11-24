package com.kapoopr.elasticSearch.demo.configuration;

import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.kapoopr.elasticSearch.demo.model.generator.factory.ModelGeneratorFactory;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.kapoopr.elasticSearch.demo.model.generators",
		"com.kapoopr.elasticSearch.demo.model.generator.factory" })
public class ModelGeneratorConfig {
	@Bean
	public ServiceLocatorFactoryBean myFactoryServiceLocatorFactoryBean() {
		ServiceLocatorFactoryBean bean = new ServiceLocatorFactoryBean();
		bean.setServiceLocatorInterface(ModelGeneratorFactory.class);
		return bean;
	}

	@Bean
	public ModelGeneratorFactory modelGeneratorFactory() {
		return (ModelGeneratorFactory) myFactoryServiceLocatorFactoryBean().getObject();
	}
}
