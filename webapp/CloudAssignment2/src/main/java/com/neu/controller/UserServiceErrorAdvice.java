package com.neu.controller;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.neu.exceptionHandler.UserNotFoundException;
import com.neu.exceptionHandler.UserServiceException;
import com.neu.exceptionHandler.UserServiceValidationException;

@ControllerAdvice
public class UserServiceErrorAdvice {
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class})
    public void handle(UserNotFoundException e) {}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({UserServiceException.class, SQLException.class, NullPointerException.class})
    public void handle() {}
	
	 @ResponseStatus(HttpStatus.BAD_REQUEST)
	 @ExceptionHandler({UserServiceValidationException.class})
	 public void handle(UserServiceValidationException e) {}

}
