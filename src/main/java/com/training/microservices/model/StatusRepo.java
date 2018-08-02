package com.training.microservices.model;

public class StatusRepo {
	
	String status;
	
	String message;
	
	Employee[] data;

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public Employee[] getData() {
		return data;
	}

	public StatusRepo(String status, String message, Employee[] data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}
}
