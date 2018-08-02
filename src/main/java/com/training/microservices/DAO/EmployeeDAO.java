package com.training.microservices.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.microservices.model.Employee;

public interface EmployeeDAO extends JpaRepository<Employee, Integer> {

	Employee findEmployeeByUsername(String username);
	
}
