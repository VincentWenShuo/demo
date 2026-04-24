package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ProblemDetail handleIllegalArgument(IllegalArgumentException exception) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
		problemDetail.setDetail(exception.getMessage());
		return problemDetail;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidation(MethodArgumentNotValidException exception) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		String message = exception.getBindingResult().getFieldErrors().stream()
			.findFirst()
			.map(error -> error.getDefaultMessage() == null ? "Validation failed." : error.getDefaultMessage())
			.orElse("Validation failed.");
		problemDetail.setDetail(message);
		return problemDetail;
	}
}
