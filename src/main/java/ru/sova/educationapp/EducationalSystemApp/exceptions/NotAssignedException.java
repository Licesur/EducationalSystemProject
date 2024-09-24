package ru.sova.educationapp.EducationalSystemApp.exceptions;

/**
 * Исключение, пробрасываемое в случае ошибок назначения одной сущности к другой
 */
public class NotAssignedException extends RuntimeException{
    public NotAssignedException(String message) {super(message);}
}
