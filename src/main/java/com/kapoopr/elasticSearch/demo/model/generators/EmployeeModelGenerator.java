package com.kapoopr.elasticSearch.demo.model.generators;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.kapoopr.elasticSearch.demo.beans.Employee;
import com.kapoopr.elasticSearch.demo.model.generator.GenricModelGenerator;

/**
 * This EmployeeModelGenerator.
 */
@Component("Employee")
public class EmployeeModelGenerator implements ModelGenerator {

    @Override
    public List<Employee> getDataModels(List<Map<String, Object>> data) {
        GenricModelGenerator<Employee> model = new GenricModelGenerator<Employee>();
        return model.getData(data, Employee.class);

    }

}
