package ru.sova.educationapp.EducationalSystemApp.exceptions;

/**
 * Исключение, пробрасываемое в случае ошибок обновления сущности
 */
public class NotUpdatedException extends RuntimeException{
    public NotUpdatedException(String message) {super(message);}
}
