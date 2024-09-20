package ru.sova.educationapp.EducationalSystemApp.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TutorTest {


    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldCreateTutor() {
        List<Student> students = new ArrayList<>();

        Tutor tutor = new Tutor(1, "testTutorName1", "testTutorPassword1",
                "testTutorEmail@email.ru", 23, "testDiscipline", students);

        assertEquals("testTutorName1", tutor.getFullName());
        assertEquals("testTutorPassword1", tutor.getPassword());
        assertEquals("testTutorEmail@email.ru", tutor.getEmail());
        assertEquals("testDiscipline", tutor.getDiscipline());
        assertEquals(23, tutor.getAge());
        assertEquals(students, tutor.getStudents());
    }

    @Test
    public void testInvalidTutorWithoutName() {
        List<Student> students = new ArrayList<>();

        Tutor tutor = new Tutor(1, null, "testTutorPassword1",
                "testTutorEmail@email.ru", 23, "testDiscipline", students);
        Set<ConstraintViolation<Tutor>> violations = validator.validate(tutor);

        assertFalse(violations.isEmpty());
        assertEquals("please enter your full name", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidTutorWithoutPassword() {
        List<Student> students = new ArrayList<>();

        Tutor tutor = new Tutor(1, "testTutorName1", null,
                "testTutorEmail@email.ru", 23, "testDiscipline", students);
        Set<ConstraintViolation<Tutor>> violations = validator.validate(tutor);

        assertFalse(violations.isEmpty());
        assertEquals("please enter the password", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidTutorWithoutEmail() {
        List<Student> students = new ArrayList<>();

        Tutor tutor = new Tutor(1, "testTutorName1", "testTutorPassword1",
                null, 23, "testDiscipline", students);
        Set<ConstraintViolation<Tutor>> violations = validator.validate(tutor);

        assertFalse(violations.isEmpty());
        assertEquals("email cant be empty", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidTutorWithoutDiscipline() {
        List<Student> students = new ArrayList<>();

        Tutor tutor = new Tutor(1, "testTutorName1", "testTutorPassword1",
                "testTutorEmail@email.ru", 23, null, students);
        Set<ConstraintViolation<Tutor>> violations = validator.validate(tutor);

        assertFalse(violations.isEmpty());
        assertEquals("please enter the discipline", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidTutorWithInvalidEmail() {
        List<Student> students = new ArrayList<>();
        List<String> emails = new ArrayList<>();
// Наполняем список некорректными адресами электронной почты
        emails.add("plainaddress"); // Нет символа @
        emails.add("missingdomain@"); // Нет домена
        emails.add("@missingusername.com"); // Нет имени пользователя
        emails.add("username@.com"); // Нет доменного имени
        emails.add("username@domain..com"); // Два точки подряд
        emails.add("user name@domain.com"); // Пробел в имени пользователя
        emails.add("username@domain,com"); // Запятая вместо точки
        emails.add("username@-domain.com"); // Дефис в начале домена
        emails.add("username@domain-.com"); // Дефис в конце домена
        emails.add("username@.domain.com"); // Точка в начале домена
        emails.add("username@domain.com,com");// Два окончания домена с запятой
        emails.add("dsadeweawe.com"); // Нет имени домена с @

        Set<ConstraintViolation<Tutor>> violations = new HashSet<>();
        for (String email : emails) {
            Tutor tutor = new Tutor(1, "testTutorName1", "testTutorPassword1",
                    email, 23, "testDiscipline", students);
            violations.add(validator.validate(tutor).stream().findAny().orElse(null));
        }

        assertEquals(violations.size(), emails.size());
        violations.forEach(violation -> assertEquals("invalid email format", violation.getMessage()));
    }

    @Test
    public void testInvalidTutorWithInvalidName() {
        List<Student> students = new ArrayList<>();

        Tutor tutor = new Tutor(1, "t", "testTutorPassword1",
                "testStudentEmail@email.ru", 23, "testDiscipline", students);
        Set<ConstraintViolation<Tutor>> violations = validator.validate(tutor);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your name should have at least 2 symbols",
                violations.iterator().next().getMessage());

        tutor = new Tutor(1, "test Student Name Out Of Bounds11111111111111111111",
                "testTutorPassword1", "testStudentEmail@email.ru", 23,
                "testDiscipline", students);
        violations = validator.validate(tutor);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your name should be shorter than 50 symbols",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidTutorWithInvalidPassword() {
        List<Student> students = new ArrayList<>();

        Tutor tutor = new Tutor(1, "testTutorName1", "test",
                "testStudentEmail@email.ru", 23, "testDiscipline", students);
        Set<ConstraintViolation<Tutor>> violations = validator.validate(tutor);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your password should have at least 6 symbols",
                violations.iterator().next().getMessage());

        tutor = new Tutor(1, "testTutorName1",
                "test Student Password Out Of Bound111111111111111111",
                "testStudentEmail@email.ru", 23, "testDiscipline", students);
        violations = validator.validate(tutor);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your password should be shorter than 50 symbols",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidTutorWithInvalidAge() {
        List<Student> students = new ArrayList<>();

        Tutor tutor = new Tutor(1, "testTutorName1", "testTutorPassword1",
                "testStudentEmail@email.ru", -23, "testDiscipline", students);
        Set<ConstraintViolation<Tutor>> violations = validator.validate(tutor);

        assertFalse(violations.isEmpty());
        assertEquals("your age should be greater then 0",
                violations.iterator().next().getMessage());

        tutor = new Tutor(1, "testTutorName1", "testTutorPassword1",
                "testStudentEmail@email.ru", 123, "testDiscipline", students);
        violations = validator.validate(tutor);

        assertFalse(violations.isEmpty());
        assertEquals("your age shouldnt be greater than 120",
                violations.iterator().next().getMessage());
    }
}
