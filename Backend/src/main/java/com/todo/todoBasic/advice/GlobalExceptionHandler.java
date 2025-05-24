package com.todo.todoBasic.advice;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.todo.todoBasic.exceptions.InvalidTodoException;
import com.todo.todoBasic.exceptions.TodoNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  
  @ExceptionHandler(TodoNotFoundException.class)
  public ResponseEntity<String> handleTodoNotFoundException(TodoNotFoundException tnf){
    return new ResponseEntity<>(tnf.getMessage(),HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidTodoException.class)
  public ResponseEntity<String> handleInvalidTodoException(InvalidTodoException ite){
    return new ResponseEntity<>(ite.getMessage(), HttpStatus.BAD_REQUEST);
  }


}
