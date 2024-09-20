package ru.sova.educationapp.EducationalSystemApp.controllers;

import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.sova.educationapp.EducationalSystemApp.udtil.*;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(NotCreatedException.class)
    public ResponseEntity<ErrorMessage> handleNotCreatedException(NotCreatedException e) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(NotUpdatedException.class)
    public ResponseEntity<ErrorMessage> handleNotUpdatedException(NotUpdatedException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_MODIFIED)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(NotAssignedException.class)
    public ResponseEntity<ErrorMessage> handleNotAssignedException(NotAssignedException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_MODIFIED)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(NotExcludedException.class)
    public ResponseEntity<ErrorMessage> handleNotExcludedException(NotExcludedException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_MODIFIED)
                .body(new ErrorMessage(e.getMessage()));
    }
}
