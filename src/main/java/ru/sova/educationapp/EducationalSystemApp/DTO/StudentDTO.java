package ru.sova.educationapp.EducationalSystemApp.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Transfer Object сущности студента
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class StudentDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @NotEmpty(message = "please enter your full name")
    @Schema(description = "Ваше полное имя должно содержать от 2 до 50 символов")
    @Size(min = 2, message = "sorry, your name should have at least 2 symbols")
    @Size(max = 50, message = "sorry, your name should be shorter than 50 symbols")
    private String fullName;

    @Schema(description = "Пароль должен содержать от 6 до 50 символов")
    @NotEmpty(message = "please enter the password")
    @Size(min = 6, message = "sorry, your password should have at least 6 symbols")
    @Size(max = 50, message = "sorry, your password should be shorter than 50 symbols")
    private String password;

    @Schema(description = "Электронная почта", example = "junior@example.com")
    @Email(message = "invalid email format")
    @NotEmpty(message = "email cant be empty")
    private String email;

    @Min(value = 0, message = "your age should be greater then 0")
    @Max(value = 120, message = "your age shouldnt be greater than 120")
    @Schema(description = "Ваш возраст должен быть от 0 до 120 лет, и да, это скрытый эйджизм")
    private int age;

    @EqualsAndHashCode.Exclude
    @Schema(description = "Список работ, назначенных ученику к выполнению")
    private List<VerificationWorkDTO> verificationWorks = new ArrayList<>();

}
