package com.kapoopr.elasticSearch.demo.services.bean.service;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.kapoopr.elasticSearch.demo.beans.Employee;
import com.kapoopr.elasticSearch.demo.configuration.EmployeeRepository;

@Service
public class EmployeeService {

	private EmployeeRepository employeeRepository;

	@Autowired
	public void setBookRepository(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public Page<Employee> getEmployeesById(String id) {
		return employeeRepository.findById(id, new PageRequest(0, 10));
	}

	public Page<Employee> getEmployeesByName(String name) {
		// TODO Auto-generated method stub
		return employeeRepository.findByName(name, new PageRequest(0, 10));
	}

	public Page<Employee> getEmployeesByText(String name, String text) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.matchPhraseQuery(name, text).slop(1)).build();
		// return employeeRepository.search(searchQuery);
		return employeeRepository.search(QueryBuilders.matchPhraseQuery(name, text), new PageRequest(0, 10));

	}
}
