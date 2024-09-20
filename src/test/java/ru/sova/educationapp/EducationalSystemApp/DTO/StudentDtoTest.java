package ru.sova.educationapp.EducationalSystemApp.DTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class StudentDtoTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldCreateStudentDTO() {
        List<VerificationWorkDTO> verificationWorkDTOS = new ArrayList<>();

        StudentDTO studentDTO = new StudentDTO(0, "testStudentName1", "testStudentPassword1",
                "testStudentEmail@email.ru", 16, verificationWorkDTOS);

        assertEquals("testStudentName1", studentDTO.getFullName());
        assertEquals("testStudentPassword1", studentDTO.getPassword());
        assertEquals("testStudentEmail@email.ru", studentDTO.getEmail());
        assertEquals(16, studentDTO.getAge());
        assertEquals(verificationWorkDTOS, studentDTO.getVerificationWorks());
    }

    @Test
    public void testInvalidStudentDTOWithoutName() {
        List<VerificationWorkDTO> verificationWorkDTOS = new ArrayList<>();

        StudentDTO studentDTO = new StudentDTO(0, null, "testStudentPassword1",
                "testStudentEmail@email.ru", 16, verificationWorkDTOS);
        Set<ConstraintViolation<StudentDTO>> violations = validator.validate(studentDTO);

        assertFalse(violations.isEmpty());
        assertEquals("please enter your full name", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidStudentDTOWithoutPassword() {
        List<VerificationWorkDTO> verificationWorkDTOS = new ArrayList<>();

        StudentDTO studentDTO = new StudentDTO(0, "testStudentName1", null,
                "testStudentEmail@email.ru", 16, verificationWorkDTOS);
        Set<ConstraintViolation<StudentDTO>> violations = validator.validate(studentDTO);

        assertFalse(violations.isEmpty());
        assertEquals("please enter the password", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidStudentDTOWithoutEmail() {
        List<VerificationWorkDTO> verificationWorkDTOS = new ArrayList<>();

        StudentDTO studentDTO = new StudentDTO(0, "testStudentName1", "testStudentPassword1",
                null, 16, verificationWorkDTOS);
        Set<ConstraintViolation<StudentDTO>> violations = validator.validate(studentDTO);

        assertFalse(violations.isEmpty());
        assertEquals("email cant be empty", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidStudentDTOWithInvalidEmail() {
        List<VerificationWorkDTO> verificationWorks = new ArrayList<>();
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

        Set<ConstraintViolation<StudentDTO>> violations = new HashSet<>();
        for (String email : emails) {
            StudentDTO studentDTO = new StudentDTO(0, "testStudentName1", "testStudentPassword1",
                    email, 16, verificationWorks);
            violations.add(validator.validate(studentDTO).stream().findAny().orElse(null));
        }

        assertEquals(violations.size(), emails.size());
        violations.forEach(violation -> assertEquals("invalid email format", violation.getMessage()));
    }

    @Test
    public void testInvalidStudentDTOWithInvalidName() {
        List<VerificationWorkDTO> verificationWorkDTOS = new ArrayList<>();

        StudentDTO studentDTO = new StudentDTO(0, "t", "testStudentPassword1",
                "testStudentEmail@email.ru", 16, verificationWorkDTOS);
        Set<ConstraintViolation<StudentDTO>> violations = validator.validate(studentDTO);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your name should have at least 2 symbols",
                violations.iterator().next().getMessage());

        studentDTO = new StudentDTO(0, "test Student Name Out Of Bounds11111111111111111111",
                "testStudentPassword1",
                "testStudentEmail@email.ru", 16, verificationWorkDTOS);
        violations = validator.validate(studentDTO);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your name should be shorter than 50 symbols",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidStudentDTOWithInvalidPassword() {
        List<VerificationWorkDTO> verificationWorkDTOS = new ArrayList<>();

        StudentDTO studentDTO = new StudentDTO(0, "testStudentName1", "test",
                "testStudentEmail@email.ru", 16, verificationWorkDTOS);
        Set<ConstraintViolation<StudentDTO>> violations = validator.validate(studentDTO);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your password should have at least 6 symbols",
                violations.iterator().next().getMessage());

        studentDTO = new StudentDTO(0, "testStudentName1",
                "test Student Password Out Of Bound111111111111111111",
                "testStudentEmail@email.ru", 16, verificationWorkDTOS);
        violations = validator.validate(studentDTO);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your password should be shorter than 50 symbols",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidStudentDTOtWithInvalidAge() {
        List<VerificationWorkDTO> verificationWorkDTOS = new ArrayList<>();

        StudentDTO studentDTO = new StudentDTO(0, "testStudentName1", "testStudentPassword1",
                "testStudentEmail@email.ru", -17, verificationWorkDTOS);
        Set<ConstraintViolation<StudentDTO>> violations = validator.validate(studentDTO);

        assertFalse(violations.isEmpty());
        assertEquals("your age should be greater then 0",
                violations.iterator().next().getMessage());

        studentDTO = new StudentDTO(0, "testStudentName1",
                "testStudentPassword1",
                "testStudentEmail@email.ru", 300, verificationWorkDTOS);
        violations = validator.validate(studentDTO);

        assertFalse(violations.isEmpty());
        assertEquals("your age shouldnt be greater than 120",
                violations.iterator().next().getMessage());
    }
}
