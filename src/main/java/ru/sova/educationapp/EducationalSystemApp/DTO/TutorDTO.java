package ru.sova.educationapp.EducationalSystemApp.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

/**
 * Transfer Object сущности преподавателя
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TutorDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @NotEmpty(message = "please enter your full name")
    @Schema(description = "Ваше полное имя должно содержать от 2 до 50 символов")
    @Size(min = 2, message = "sorry, your name should have at least 2 symbols")
    @Size(max = 50, message = "sorry, your name should be shorter than 50 symbols")
    private String fullName;

    @NotEmpty(message = "please enter the password")
    @Schema(description = "Пароль должен содержать от 6 до 50 символов")
    @Size(min = 6, message = "sorry, your password should have at least 6 symbols")
    @Size(max = 50, message = "sorry, your password should be shorter than 50 symbols")
    private String password;

    @Email(message = "invalid email format")
    @Schema(description = "Электронная почта", example = "junior@example.com")
    @NotEmpty(message = "email cant be empty")
    private String email;

    @Min(value = 0, message = "your age should be greater then 0")
    @Max(value = 120, message = "your age shouldnt be greater than 120")
    @Schema(description = "Ваш возраст должен быть от 0 до 120 лет, и да, это скрытый эйджизм")
    private int age;

    @NotEmpty(message = "please enter the discipline")
    @Size(min = 2, message = "sorry, your name should have at least 2 symbols")
    @Size(max = 50, message = "sorry, your name should be shorter than 50 symbols")
    @Schema(description = "Дисциплина должна содержать от 2 до 50 символов")
    private String discipline;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Schema(description = "Список студентов у преподавателя")
    private List<StudentDTO> students;
}
