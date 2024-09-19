package ru.sova.educationapp.EducationalSystemApp.DTO;

import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Objects;

public class TutorDTO {

    private int id;

    @NotEmpty(message = "please enter your full name")
    @Size(min = 2, message = "sorry, your name should have at least 2 symbols")
    @Size(max = 50, message = "sorry, your name should be shorter than 50 symbols")
    private String fullName;

    @NotEmpty(message = "please enter the password")
    @Size(min = 6, message = "sorry, your password should have at least 6 symbols")
    @Size(max = 50, message = "sorry, your password should be shorter than 50 symbols")
    private String password;

    @Email(message = "invalid email format")
    @NotEmpty(message = "email cant be empty")
    private String email;

    @Min(value = 0, message = "your age should be greater then 0")
    @Max(value = 120, message = "your age shouldnt be greater than 120")
    private int age;

    @NotEmpty(message = "please enter the discipline")
    @Size(min = 2, message = "sorry, your name should have at least 2 symbols")
    @Size(max = 50, message = "sorry, your name should be shorter than 50 symbols")
    private String discipline;

    private List<StudentDTO> students;

    @Override
    public String toString() {
        return "Tutor{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", discipline='" + discipline + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TutorDTO tutorDTO = (TutorDTO) o;
        return id == tutorDTO.id && age == tutorDTO.age && fullName.equals(tutorDTO.fullName) && password.equals(tutorDTO.password) && email.equals(tutorDTO.email) && discipline.equals(tutorDTO.discipline) && students.equals(tutorDTO.students);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + fullName.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + age;
        result = 31 * result + discipline.hashCode();
        result = 31 * result + students.hashCode();
        return result;
    }

    public TutorDTO( int id, String fullName,
                    String password,
                    String email, int age,
                    String discipline,
                    List<StudentDTO> students) {
        this.id = id;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.age = age;
        this.discipline = discipline;
        this.students = students;
    }

    public TutorDTO() {
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
    @Size(max = 50, message = "sorry, you password should be shorter than 50 symbols") String getPassword() {
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

    public      @NotEmpty(message = "please enter the discipline")
                @Size(min = 2, message = "sorry, your name should have at least 2 symbols")
                @Size(max = 50, message = "sorry, your name should be shorter than 50 symbols") String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(    @NotEmpty(message = "please enter the discipline")
                                  @Size(min = 2, message = "sorry, your name should have at least 2 symbols")
                                  @Size(max = 50, message = "sorry, your name should be shorter than 50 symbols") String discipline) {
        this.discipline = discipline;
    }

    public List<StudentDTO> getStudents() {
        return students;
    }

    public void setStudents(List<StudentDTO> students) {
        this.students = students;
    }
}
