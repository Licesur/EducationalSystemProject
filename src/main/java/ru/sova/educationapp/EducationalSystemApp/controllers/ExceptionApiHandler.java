package ru.sova.educationapp.EducationalSystemApp.controllers;

import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.sova.educationapp.EducationalSystemApp.exceptions.*;

@RestControllerAdvice
public class ExceptionApiHandler {
    /**
     * Исключение, пробрасываемое в случае ошибок создания сущности
     */
    @ExceptionHandler(NotCreatedException.class)
    public ResponseEntity<ErrorMessage> handleNotCreatedException(NotCreatedException e) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorMessage(e.getMessage()));
    }
    /**
     * Исключение, пробрасываемое в случае ошибок обновления сущности
     */
    @ExceptionHandler(NotUpdatedException.class)
    public ResponseEntity<ErrorMessage> handleNotUpdatedException(NotUpdatedException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_MODIFIED)
                .body(new ErrorMessage(e.getMessage()));
    }
    /**
     * Исключение, пробрасываемое в случае ошибок назначения одной сущности к сущности
     */
    @ExceptionHandler(NotAssignedException.class)
    public ResponseEntity<ErrorMessage> handleNotAssignedException(NotAssignedException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_MODIFIED)
                .body(new ErrorMessage(e.getMessage()));
    }
    /**
     * Исключение, пробрасываемое в случае ошибок исключения сущности из содержания другой
     */
    @ExceptionHandler(NotExcludedException.class)
    public ResponseEntity<ErrorMessage> handleNotExcludedException(NotExcludedException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_MODIFIED)
                .body(new ErrorMessage(e.getMessage()));
    }
}
