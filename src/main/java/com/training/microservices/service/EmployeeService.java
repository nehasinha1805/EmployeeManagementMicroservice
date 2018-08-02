package com.training.microservices.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.training.microservices.DAO.EmployeeDAO;
import com.training.microservices.exception.EmployeeNotFoundException;
import com.training.microservices.exception.InternalServerErrorException;
import com.training.microservices.model.Credentials;
import com.training.microservices.model.Employee;
import com.training.microservices.model.StatusRepo;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeDAO employeeDAO;

	// to get all employees in db
	@Cacheable("employee.all")
	public StatusRepo getAllEmployeeService() {
		try {
			List<Employee> employeeList = new ArrayList<>();
			employeeDAO.findAll().forEach(employeeList::add);
			return new StatusRepo("Success!", "Details of all employees.",
					employeeList.toArray(new Employee[employeeList.size()]));
		} catch (Exception e) {
			throw new EmployeeNotFoundException("No Employee Found");
		}
	}

	// to get a single employee data
	@Cacheable("employee.byId")
	public StatusRepo getEmployeeService(int userId) {
		try {
			return new StatusRepo("Success!", "Details of employee.",
					new Employee[] { employeeDAO.findById(userId).get() });
		} catch (Exception e) {
			throw new EmployeeNotFoundException("No Employee Found");
		}
	}

	// to add an employee to db
	public StatusRepo addEmployeeService(Employee employee) {
		try {
			return new StatusRepo("Success!", "Employee added.", new Employee[] { employeeDAO.save(employee) });
		} catch (Exception e) {
			throw new InternalServerErrorException("Internal Server Error");
		}
	}

	// delete an employee from db
	@CacheEvict("employee.byId")
	public StatusRepo deleteEmployeeService(int userId) {
		try {
			employeeDAO.deleteById(userId);
			return new StatusRepo("Success", "Employee deleted successfully!", null);
		} catch (Exception e) {
			throw new EmployeeNotFoundException("No Employee Found");
		}
	}

	public StatusRepo checkLoginService(Credentials cred) {
		try {
			if (cred == null || cred.getUser() == null || cred.getPass() == null) {

				return new StatusRepo("Failure!", "Internal Server Error.", null);

			} else {
				Employee res = employeeDAO.findEmployeeByUsername(cred.getUser());
				if (cred.getPass().equals(res.getPassword()))
					return new StatusRepo("Success!", "Employee has authenticated successfully", null);
			}
		} catch (Exception e) {
			throw new InternalServerErrorException("Internal Server Error");
		}

		return new StatusRepo("Failure!", "Employee data is not authenticated.", null);

	}

}
