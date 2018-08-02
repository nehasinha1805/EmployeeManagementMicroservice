package com.training.microservices.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.training.microservices.model.StatusRepo;

@RestControllerAdvice
public class EmployeeNotFoundExceptionHandler {

	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<StatusRepo> employeeNotFound() {
		return new ResponseEntity<>(new StatusRepo("Failed!", "No Employee Found", null), HttpStatus.BAD_REQUEST);
		
	}
}
