package ru.sova.educationapp.EducationalSystemApp.DTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TaskDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldCreateTaskDTO() {

        TaskDTO taskDTO = new TaskDTO(1L, "test task definition 1", "test task answer 1");

        assertEquals("test task definition 1", taskDTO.getDefinition());
        assertEquals("test task answer 1", taskDTO.getAnswer());
    }

    @Test
    public void testInvalidTaskDTOWithInvalidDefinition() {
        String definition = """
                На доске написано несколько различных натуральных чисел.\s
                Эти числа разбили на три группы, в каждой из которых оказалось хотя бы одно число.\s
                К каждому числу из первой группы приписали справа цифру 6,\s
                к каждому числу из второй группы приписали справа цифру 9,\s
                а числа третьей группы оставили без изменений.

                а) Могла ли сумма всех этих чисел увеличиться в 9 раз?

                б) Могла ли сумма всех этих чисел увеличиться в 19 раз?

                в) В какое наибольшее число раз могла увеличиться сумма всех этих чисел?

                В качестве ответа запишите последовательные ответы пунктов а,б,в""";
        TaskDTO taskDTO = new TaskDTO(0L, definition, "test answer");
        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your definition is too large, please try to insert it in 500 symbols",
                violations.iterator().next().getMessage());

        taskDTO = new TaskDTO(0L, "?", "test answer");
        violations = validator.validate(taskDTO);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your task should be at least 3 symbols length",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidTaskDTOWithoutDefinition() {
        TaskDTO taskDTO = new TaskDTO(0L, null, "test answer");

        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);

        assertFalse(violations.isEmpty());
        assertEquals("please enter the definition for the task",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidTaskDTOWithoutAnswer() {
        TaskDTO taskDTO = new TaskDTO(0L, "test definition", "");

        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);

        assertFalse(violations.isEmpty());
        assertEquals("please enter thee answer for the task",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidTaskDTOWithInvalidAnswer() {
        TaskDTO taskDTO = new TaskDTO(0L, "test definition",
                "test answer with more than 100 symbols " +
                        "// test answer with more than 100 symbols " +
                        "// test answer with more than 100 symbols");

        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your answer should be shorter than 100 symbols",
                violations.iterator().next().getMessage());

    }

}
