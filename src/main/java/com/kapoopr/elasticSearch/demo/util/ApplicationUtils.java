package com.kapoopr.elasticSearch.demo.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ApplicationUtils {
	public List<String> getProperties(Class<?> classType) {
		List<String> properties = new ArrayList<String>();
		for (Field f : classType.getClass().getDeclaredFields()) {
			properties.add(f.getName());
		}
		return properties;
	}
}
