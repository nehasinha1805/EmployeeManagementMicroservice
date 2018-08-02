package com.training.microservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.training.microservices.exception.EmployeeNotFoundException;
import com.training.microservices.model.Credentials;
import com.training.microservices.model.Employee;
import com.training.microservices.model.StatusRepo;
import com.training.microservices.service.EmployeeService;

@RestController
@RequestMapping("/EmpMgt")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("/getAllEmpDetails")
	public StatusRepo getAllEmployeeController() {
		return employeeService.getAllEmployeeService();
	}
	
	@GetMapping(value="/getByEmpId/{empId}")
	public StatusRepo getEmployeeController(@PathVariable(value = "empId") int empId) {
		return employeeService.getEmployeeService(empId);
	}
	
	@PutMapping("/addEmp")
	public StatusRepo addEmployeeController(@RequestBody Employee e) {
		return employeeService.addEmployeeService(e);
	}
	
	@PostMapping("/checkLogin")
	public StatusRepo checkLoginController(@RequestBody @Validated Credentials cred, BindingResult result) {
		if(result.hasErrors() || cred.getUser() == null || cred.getPass() == null) {
			throw new EmployeeNotFoundException("Wrong username or password!");
		} else {
			return employeeService.checkLoginService(cred);
		}
	}
	
	@DeleteMapping("/deleteEmp/{empId}")
	public StatusRepo deleteEmployeeController(@PathVariable(value = "empId") int empId) {
		return employeeService.deleteEmployeeService(empId);
	}
}
