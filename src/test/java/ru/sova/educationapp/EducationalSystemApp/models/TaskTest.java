package ru.sova.educationapp.EducationalSystemApp.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TaskTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldCreateTask() {
        List<VerificationWork> verificationWorks = new ArrayList<>();

        Task task = new Task(1L, "test task definition 1", "test task answer 1",
                verificationWorks);

        assertEquals("test task definition 1", task.getDefinition());
        assertEquals("test task answer 1", task.getAnswer());
        assertEquals(verificationWorks, task.getVerificationWorks());
    }

    @Test
    public void testInvalidTaskWithInvalidDefinition() {
        String definition = "На доске написано несколько различных натуральных чисел. " +
                "Эти числа разбили на три группы, в каждой из которых оказалось хотя бы одно число. " +
                "К каждому числу из первой группы приписали справа цифру 6, " +
                "к каждому числу из второй группы приписали справа цифру 9, " +
                "а числа третьей группы оставили без изменений.\n" +
                "\n" +
                "а) Могла ли сумма всех этих чисел увеличиться в 9 раз?\n" +
                "\n" +
                "б) Могла ли сумма всех этих чисел увеличиться в 19 раз?\n" +
                "\n" +
                "в) В какое наибольшее число раз могла увеличиться сумма всех этих чисел? \n" +
                "\n" +
                "В качестве ответа запишите последовательные ответы пунктов а,б,в";
        List<VerificationWork> verificationWorks = new ArrayList<>();

        Task task = new Task(0L, definition, "test answer", verificationWorks);
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertFalse(violations.isEmpty());
        assertEquals("sorry, your definition is too large, please try to insert it in 500 symbols",
                violations.iterator().next().getMessage());

        task = new Task(0L, "?", "test answer", verificationWorks);
        violations = validator.validate(task);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your task should be at least 3 symbols length",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidTaskWithoutDefinition() {
        List<VerificationWork> verificationWorks = new ArrayList<>();

        Task task = new Task(0L, null, "test answer", verificationWorks);

        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertFalse(violations.isEmpty());
        assertEquals("please enter the definition for the task",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidTaskWithoutAnswer() {
        List<VerificationWork> verificationWorks = new ArrayList<>();

        Task task = new Task(0L, "test definition", "", verificationWorks);

        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertFalse(violations.isEmpty());
        assertEquals("please enter thee answer for the task",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidTaskWithInvalidAnswer() {
        List<VerificationWork> verificationWorks = new ArrayList<>();

        Task task = new Task(0L, "test definition",
                "test answer with more than 100 symbols " +
                        "// test answer with more than 100 symbols " +
                        "// test answer with more than 100 symbols", verificationWorks);

        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your answer should be shorter than 100 symbols",
                violations.iterator().next().getMessage());

    }
}
