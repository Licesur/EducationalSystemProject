package ru.sova.educationapp.EducationalSystemApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Tutor")
@Getter()
@Setter
@NoArgsConstructor
@ToString
public class Tutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "full_name")
    @NotEmpty(message = "please enter your full name")
    @Size(min = 2, message = "sorry, your name should have at least 2 symbols")
    @Size(max = 50, message = "sorry, you name should be shorter than 50 symbols")
    private String fullName;

    @Column(name = "password")
    @NotEmpty(message = "please enter the pass")
    @Size(min = 2, message = "sorry, your password should have at least 6 symbols")
    @Size(max = 50, message = "sorry, you password should be shorter than 50 symbols")
    private String password;

    @Column(name = "email")
    @Email(message = "invalid email format")
    @NotEmpty(message = "email cant be empty")
    private String email;

    @Column(name = "age")
    @Min(value = 0, message = "your age should be greater then 0")
    @Max(value = 120, message = "your age shouldnt be grater than 120")
    private int age;

    @Column(name = "discipline")
    @NotEmpty
    private String discipline;

    @ManyToMany
    @JoinTable(name = "Tutor_Pupil",
            joinColumns = @JoinColumn(name = "tutor_id"),
            inverseJoinColumns = @JoinColumn(name = "pupil_id"))
    private List<Pupil> pupils;


}
