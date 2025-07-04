package com.javadiscordproject.presentation.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleEntityNotFound(EntityNotFoundException ex) {
		// 404
    }
	
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleIllegalArgument(IllegalArgumentException ex) {
        // 400
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        // 409 (Conflict)
    }
}
