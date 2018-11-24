package com.kapoopr.elasticSearch.demo.beans;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.kapoopr.elasticSearch.demo.model.Model;

/**
 * 
 * @author pkapo7
 *
 */
@Document(indexName = "employees", type = "Employee", shards = 1, replicas = 0)
public class Employee extends Model {

	@Id
	private int id;
	private String name;
	private String details;

	public Employee(Integer id) {
		super(id);
	}

	public Employee() {
	}

	public Employee(Integer id, String name) {
		super(id);
		this.id = id;
		this.name = name;
	}
	public Employee(Integer id, String name,String details) {
		super(id);
		this.id = id;
		this.name = name;
		this.details=details;
	}
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
