package com.training.microservices.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.training.microservices.model.StatusRepo;

@RestControllerAdvice
public class InternalServerErrorExceptionHandler {

	@ExceptionHandler(InternalServerErrorException.class)
	public ResponseEntity<StatusRepo> internalServerError() {
		return new ResponseEntity<>(new StatusRepo("Failed!", "There is some issue at server side. Please check the log.", null), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
