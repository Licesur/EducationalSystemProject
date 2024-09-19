package ru.sova.educationapp.EducationalSystemApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "full_name")
    @NotEmpty(message = "please enter your full name")
    @Size(min = 2, message = "sorry, your name should have at least 2 symbols")
    @Size(max = 50, message = "sorry, your name should be shorter than 50 symbols")
    private String fullName;

    @Column(name = "password")
    @NotEmpty(message = "please enter the password")
    @Size(min = 6, message = "sorry, your password should have at least 6 symbols")
    @Size(max = 50, message = "sorry, your password should be shorter than 50 symbols")
    private String password;

    @Column(name = "email")
    @Email(message = "invalid email format")
    @NotEmpty(message = "email cant be empty")
    private String email;

    @Column(name = "age")
    @Min(value = 0, message = "your age should be greater then 0")
    @Max(value = 120, message = "your age shouldnt be greater than 120")
    private int age;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "students")
    private List<Tutor> tutors;

    @ManyToMany(mappedBy = "students")
    private List<VerificationWork> verificationWorks;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", tutors=" + tutors +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;
        return id == student.id && age == student.age && fullName.equals(student.fullName) && password.equals(student.password) && email.equals(student.email) && Objects.equals(tutors, student.tutors) && Objects.equals(verificationWorks, student.verificationWorks);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + fullName.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + age;
        result = 31 * result + Objects.hashCode(tutors);
        result = 31 * result + Objects.hashCode(verificationWorks);
        return result;
    }

    public Student() {
    }

    public Student( String fullName,
                   String password, String email, int age,
                   List<Tutor> tutors,
                   List<VerificationWork> verificationWorks) {
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.age = age;
        this.tutors = tutors;
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

    public List<Tutor> getTutors() {
        return tutors;
    }

    public void setTutors(List<Tutor> tutors) {
        this.tutors = tutors;
    }

    public List<VerificationWork> getVerificationWorks() {
        return verificationWorks;
    }

    public void setVerificationWorks(List<VerificationWork> verificationWorks) {
        this.verificationWorks = verificationWorks;
    }
}
