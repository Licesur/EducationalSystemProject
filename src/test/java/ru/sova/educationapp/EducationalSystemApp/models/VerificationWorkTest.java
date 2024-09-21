package ru.sova.educationapp.EducationalSystemApp.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sova.educationapp.EducationalSystemApp.DTO.StudentDTO;
import ru.sova.educationapp.EducationalSystemApp.DTO.TutorDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class VerificationWorkTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldCreateVerificationWork() {
        List<Student> students = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();
        VerificationWork verificationWork = new VerificationWork(1L, "testVerificationWorkTitle1",
                LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                tasks, students);

        assertEquals("testVerificationWorkTitle1", verificationWork.getTitle());
        assertEquals(students, verificationWork.getStudents());
        assertEquals(tasks, verificationWork.getTasks());
        assertEquals(LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                verificationWork.getDeadline());
        assertEquals(LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                verificationWork.getAssignationDatetime());
    }

    @Test
    public void testInvalidVerificationWorkWithInvalidTitle() {
        List<Student> students = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();

        VerificationWork verificationWork = new VerificationWork(1L, "test verification work title " +
                "// test verification work title // test verification work title // test verification work title",
                LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                tasks, students);
        Set<ConstraintViolation<VerificationWork>> violations = validator.validate(verificationWork);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your title should be shorter than 100 symbols",
                violations.iterator().next().getMessage());

        verificationWork = new VerificationWork(1L, "?",
                LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                tasks, students);
        violations = validator.validate(verificationWork);


        assertFalse(violations.isEmpty());
        assertEquals("sorry, your title should have at least 2 symbols",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidVerificationWorkWithoutTitle() {
        List<Student> students = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();

        VerificationWork verificationWork = new VerificationWork(1L, null,
                LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                tasks, students);
        Set<ConstraintViolation<VerificationWork>> violations = validator.validate(verificationWork);

        assertFalse(violations.isEmpty());
        assertEquals("please enter the title of the work",
                violations.iterator().next().getMessage());
    }
}
