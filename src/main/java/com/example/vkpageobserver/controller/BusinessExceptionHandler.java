package com.example.vkpageobserver.controller;

import com.example.vkpageobserver.exceptions.PageAlreadyExists;
import com.example.vkpageobserver.exceptions.PageNotFoundException;
import com.example.vkpageobserver.exceptions.UsernameAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BusinessExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Void> onUserExists() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(PageNotFoundException.class)
    public ResponseEntity<Void> onPageNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(PageAlreadyExists.class)
    public ResponseEntity<Void> onPageExists() {
        return ResponseEntity.badRequest().build();
    }
}
