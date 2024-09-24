package ru.sova.educationapp.EducationalSystemApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

/**
 * Модель сущности студента
 */
@Entity
@Table(name = "Student")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

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

    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "students")
    private List<Tutor> tutors;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "students")
    private List<VerificationWork> verificationWorks;

}
