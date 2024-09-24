package ru.sova.educationapp.EducationalSystemApp.exceptions;

/**
 * Исключение, пробрасываемое в случае ошибок создания сущности
 */
public class NotCreatedException extends RuntimeException{
    public NotCreatedException(String message) {super(message);}
}
