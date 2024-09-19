package ru.sova.educationapp.EducationalSystemApp.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Objects;

public class StudentDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

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
    @Schema(description = "Список работ, назначенных ученику к выполнению")
    private List<VerificationWorkDTO> verificationWorks;


    @Override
    public String toString() {
        return "Student{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", verificationWorks=" + verificationWorks +
                '}';
    }

    public StudentDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentDTO that = (StudentDTO) o;
        return id == that.id && age == that.age && fullName.equals(that.fullName) && password.equals(that.password) && email.equals(that.email) && Objects.equals(verificationWorks, that.verificationWorks);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + fullName.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + age;
        result = 31 * result + Objects.hashCode(verificationWorks);
        return result;
    }

    public StudentDTO(int id, String fullName,
                      String password,
                      String email, int age,
                      List<VerificationWorkDTO> verificationWorks) {
        this.id = id;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.age = age;
        this.verificationWorks = verificationWorks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotEmpty(message = "please enter your full name")
    @Size(min = 2, message = "sorry, your name should have at least 2 symbols")
    @Size(max = 50, message = "sorry, your name should be shorter than 50 symbols") String getFullName() {
        return fullName;
    }

    public void setFullName(@NotEmpty(message = "please enter your full name")
                            @Size(min = 2, message = "sorry, your name should have at least 2 symbols")
                            @Size(max = 50, message = "sorry, your name should be shorter than 50 symbols")
                            String fullName) {
        this.fullName = fullName;
    }

    public @NotEmpty(message = "please enter the password")
    @Size(min = 6, message = "sorry, your password should have at least 6 symbols")
    @Size(max = 50, message = "sorry, your password should be shorter than 50 symbols") String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty(message = "please enter the password")
                            @Size(min = 6, message = "sorry, your password should have at least 6 symbols")
                            @Size(max = 50, message = "sorry, your password should be shorter than 50 symbols")
                            String password) {
        this.password = password;
    }

    public @Email(message = "invalid email format")
    @NotEmpty(message = "email cant be empty") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "invalid email format")
                         @NotEmpty(message = "email cant be empty") String email) {
        this.email = email;
    }

    @Min(value = 0, message = "your age should be greater then 0")
    @Max(value = 120, message = "your age shouldnt be greater than 120")
    public int getAge() {
        return age;
    }

    public void setAge(@Min(value = 0, message = "your age should be greater then 0")
                       @Max(value = 120, message = "your age shouldnt be greater than 120") int age) {
        this.age = age;
    }

    public List<VerificationWorkDTO> getVerificationWorks() {
        return verificationWorks;
    }

    public void setVerificationWorks(List<VerificationWorkDTO> verificationWorks) {
        this.verificationWorks = verificationWorks;
    }
}
