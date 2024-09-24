package ru.sova.educationapp.EducationalSystemApp.exceptions;

/**
 * Исключение, пробрасываемое в случае ошибок исключения одной сущности из содержания другой
 */
public class NotExcludedException extends RuntimeException{
    public NotExcludedException(String message) {super(message);}
}
