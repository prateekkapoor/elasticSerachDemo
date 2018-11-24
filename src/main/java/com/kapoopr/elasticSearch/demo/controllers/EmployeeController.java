package com.kapoopr.elasticSearch.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;

import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kapoopr.elasticSearch.demo.beans.Employee;
import com.kapoopr.elasticSearch.demo.services.bean.service.EmployeeService;

@RestController
@RequestMapping("/excelFileIndexDemo")
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@RequestMapping("/employee/id/{id}")
	public Page<Employee> getEmployees(@PathVariable("id") String id) {
		return employeeService.getEmployeesById(id);

	}

	@RequestMapping("/employee")
	public List<Employee> getEmployees() {
		List<Employee> employeesList = new ArrayList<Employee>();
		employeesList.add(new Employee(1, "lokesh"));
		return employeesList;
	}

	@RequestMapping("/employee/name/{name}")
	public Page<Employee> getEmployeesByName(@PathVariable("name") String name) {
		return employeeService.getEmployeesByName(name);

	}
	
	@RequestMapping(value="/employee/phraseSearch",method=RequestMethod.POST)
	public Page<Employee> getEmployeesByText(@RequestBody Map<String, String> payload) {
		return employeeService.getEmployeesByText(payload.get("name"),payload.get("text"));

	}

}
