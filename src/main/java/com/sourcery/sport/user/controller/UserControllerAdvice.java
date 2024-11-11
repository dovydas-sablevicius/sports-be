package com.sourcery.sport.user.controller;

import com.sourcery.sport.global.ErrorResponse;
import com.sourcery.sport.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice(assignableTypes = UserControllerAdvice.class)
public class UserControllerAdvice {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception,
                                                                   WebRequest request) {
    ErrorResponse response = new ErrorResponse("User not found", request.getDescription(false));
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }
}