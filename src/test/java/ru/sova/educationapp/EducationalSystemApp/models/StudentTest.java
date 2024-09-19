package ru.sova.educationapp.EducationalSystemApp.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sova.educationapp.EducationalSystemApp.repositories.StudentRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {


    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldCreateStudent() {
        List<VerificationWork> verificationWorks = new ArrayList<>();
        List<Tutor> tutors = new ArrayList<>();

        Student student = new Student("testStudentName1", "testStudentPassword1",
                "testStudentEmail@email.ru", 16 , tutors , verificationWorks);

        assertEquals("testStudentName1", student.getFullName());
        assertEquals("testStudentPassword1", student.getPassword());
        assertEquals("testStudentEmail@email.ru", student.getEmail());
        assertEquals(16, student.getAge());
        assertEquals(tutors, student.getTutors());
        assertEquals(verificationWorks, student.getVerificationWorks());
    }

    @Test
    public void testInvalidStudentWithoutName() {
        List<VerificationWork> verificationWorks = new ArrayList<>();
        List<Tutor> tutors = new ArrayList<>();

        Student student = new Student(null, "testStudentPassword1",
                "testStudentEmail@email.ru", 16 , tutors , verificationWorks);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertFalse(violations.isEmpty());
        assertEquals("please enter your full name", violations.iterator().next().getMessage());
    }
    @Test
    public void testInvalidStudentWithoutPassword() {
        List<VerificationWork> verificationWorks = new ArrayList<>();
        List<Tutor> tutors = new ArrayList<>();

        Student student = new Student("testStudentName1", null,
                "testStudentEmail@email.ru", 16 , tutors , verificationWorks);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertFalse(violations.isEmpty());
        assertEquals("please enter the password", violations.iterator().next().getMessage());
    }
    @Test
    public void testInvalidStudentWithoutEmail() {
        List<VerificationWork> verificationWorks = new ArrayList<>();
        List<Tutor> tutors = new ArrayList<>();

        Student student = new Student("testStudentName1", "testStudentPassword1",
                null, 16 , tutors , verificationWorks);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertFalse(violations.isEmpty());
        assertEquals("email cant be empty", violations.iterator().next().getMessage());
    }
    @Test
    public void testInvalidStudentWithInvalidEmail() {
        List<VerificationWork> verificationWorks = new ArrayList<>();
        List<Tutor> tutors = new ArrayList<>();
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

        Set<ConstraintViolation<Student>> violations = new HashSet<>();
        for (String email : emails) {
            Student student = new Student("testStudentName1", "testStudentPassword1",
                    email, 16 , tutors , verificationWorks);
            violations.add(validator.validate(student).stream().findAny().orElse(null));
        }

        assertEquals(violations.size(), emails.size());
        violations.forEach(violation -> assertEquals("invalid email format", violation.getMessage()));
    }
    @Test
    public void testInvalidStudentWithInvalidName() {
        List<VerificationWork> verificationWorks = new ArrayList<>();
        List<Tutor> tutors = new ArrayList<>();

        Student student = new Student("t", "testStudentPassword1",
                "testStudentEmail@email.ru", 16 , tutors , verificationWorks);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your name should have at least 2 symbols",
                violations.iterator().next().getMessage());

        student = new Student("test Student Name Out Of Bounds11111111111111111111",
                "testStudentPassword1",
                "testStudentEmail@email.ru", 16 , tutors , verificationWorks);
        violations = validator.validate(student);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your name should be shorter than 50 symbols",
                violations.iterator().next().getMessage());
    }
    @Test
    public void testInvalidStudentWithInvalidPassword() {
        List<VerificationWork> verificationWorks = new ArrayList<>();
        List<Tutor> tutors = new ArrayList<>();

        Student student = new Student("testStudentName1", "test",
                "testStudentEmail@email.ru", 16 , tutors , verificationWorks);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your password should have at least 6 symbols",
                violations.iterator().next().getMessage());

        student = new Student("testStudentName1",
                "test Student Password Out Of Bound111111111111111111",
                "testStudentEmail@email.ru", 16 , tutors , verificationWorks);
        violations = validator.validate(student);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your password should be shorter than 50 symbols",
                violations.iterator().next().getMessage());
    }
    @Test
    public void testInvalidStudentWithInvalidAge() {
        List<VerificationWork> verificationWorks = new ArrayList<>();
        List<Tutor> tutors = new ArrayList<>();

        Student student = new Student("testStudentName1", "testStudentPassword1",
                "testStudentEmail@email.ru", -17 , tutors , verificationWorks);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertFalse(violations.isEmpty());
        assertEquals("your age should be greater then 0",
                violations.iterator().next().getMessage());

        student = new Student("testStudentName1",
                "testStudentPassword1",
                "testStudentEmail@email.ru", 300 , tutors , verificationWorks);
        violations = validator.validate(student);

        assertFalse(violations.isEmpty());
        assertEquals("your age shouldnt be greater than 120",
                violations.iterator().next().getMessage());
    }
}
