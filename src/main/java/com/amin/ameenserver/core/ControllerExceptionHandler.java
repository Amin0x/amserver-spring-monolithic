package com.amin.ameenserver.core;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityExistsException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ErrorMessage> resourceNotFoundException(Exception ex){

        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EntityExistsException.class})
    public ResponseEntity<ErrorMessage> entityExistsException(Exception ex){

        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED.value());

        return new ResponseEntity<>(errorMessage, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorMessage> illegalArgumentException(Exception ex){

        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED.value());

        return new ResponseEntity<>(errorMessage, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
