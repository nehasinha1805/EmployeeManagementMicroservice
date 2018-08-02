package com.training.microservices.exception;

public class InternalServerErrorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InternalServerErrorException(String exception) {
		super(exception);
	}
}
