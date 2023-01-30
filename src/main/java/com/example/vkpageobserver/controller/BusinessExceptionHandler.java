package com.example.vkpageobserver.controller;

import com.example.vkpageobserver.exсeptions.PageNotFoundException;
import com.example.vkpageobserver.exсeptions.UsernameAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BusinessExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Void> onUserExist() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(PageNotFoundException.class)
    public ResponseEntity<Void> onPageNotFound() {
        return ResponseEntity.notFound().build();
    }
}
