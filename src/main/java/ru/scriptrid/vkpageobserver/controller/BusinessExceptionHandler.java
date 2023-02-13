package ru.scriptrid.vkpageobserver.controller;

import ru.scriptrid.vkpageobserver.exceptions.UserDoesNotHaveAPageException;
import ru.scriptrid.vkpageobserver.exceptions.PageAlreadyExistsException;
import ru.scriptrid.vkpageobserver.exceptions.PageNotFoundInDatabaseException;
import ru.scriptrid.vkpageobserver.exceptions.PageNotFoundInVkException;
import ru.scriptrid.vkpageobserver.exceptions.UsernameAlreadyExistsException;
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

    @ExceptionHandler(PageNotFoundInDatabaseException.class)
    public ResponseEntity<Void> onPageNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(PageNotFoundInVkException.class)
    public ResponseEntity<Void> onPageNotFoundInVk() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(PageAlreadyExistsException.class)
    public ResponseEntity<Void> onPageExists() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(UserDoesNotHaveAPageException.class)
    public ResponseEntity<Void> onUserHasNotThePage() {
        return ResponseEntity.notFound().build();
    }
}

