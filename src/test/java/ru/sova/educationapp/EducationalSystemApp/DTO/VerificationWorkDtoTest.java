package ru.sova.educationapp.EducationalSystemApp.DTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class VerificationWorkDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldCreateVerificationWorkDTO() {
        List<TaskDTO> taskDTOS = new ArrayList<>();

        VerificationWorkDTO verificationWorkDTO = new VerificationWorkDTO(1, "testVerificationWorkTitle1",
                taskDTOS);

        assertEquals("testVerificationWorkTitle1", verificationWorkDTO.getTitle());
        assertEquals(taskDTOS, verificationWorkDTO.getTasks());
    }
    @Test
    public void testInvalidVerificationWorkDTOWithInvalidTitle() {
        List<TaskDTO> taskDTOS = new ArrayList<>();

        VerificationWorkDTO verificationWorkDTO = new VerificationWorkDTO(1, "test verification work title " +
                "// test verification work title // test verification work title // test verification work title",
                taskDTOS);
        Set<ConstraintViolation<VerificationWorkDTO>> violations = validator.validate(verificationWorkDTO);

        assertFalse(violations.isEmpty());
        assertEquals("sorry, your title should be shorter than 100 symbols",
                violations.iterator().next().getMessage());

        verificationWorkDTO = new VerificationWorkDTO(1, "?", taskDTOS);
        violations = validator.validate(verificationWorkDTO);


        assertFalse(violations.isEmpty());
        assertEquals("sorry, your title should have at least 2 symbols",
                violations.iterator().next().getMessage());
    }
    @Test
    public void testInvalidVerificationWorkWithoutTitle() {
        List<TaskDTO> taskDTOS = new ArrayList<>();

        VerificationWorkDTO verificationWorkDTO = new VerificationWorkDTO(1, null,
                taskDTOS);
        Set<ConstraintViolation<VerificationWorkDTO>> violations = validator.validate(verificationWorkDTO);

        assertFalse(violations.isEmpty());
        assertEquals("please enter the title of the work",
                violations.iterator().next().getMessage());
    }

}
