package com.kapoopr.elasticSearch.demo.configuration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.kapoopr.elasticSearch.demo.beans.Employee;

/**
 * Employee repository
 */
public interface EmployeeRepository extends ElasticsearchRepository<Employee, String> {
    Page<Employee> findByName(String name, Pageable pageable);

    Page<Employee> findById(String id, Pageable pageable);
}
