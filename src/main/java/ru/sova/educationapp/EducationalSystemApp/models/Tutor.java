package ru.sova.educationapp.EducationalSystemApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Tutor")
public class Tutor {
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

    @Column(name = "discipline")
    @NotEmpty(message = "please enter the discipline")
    @Size(min = 2, message = "sorry, your name should have at least 2 symbols")
    @Size(max = 50, message = "sorry, your name should be shorter than 50 symbols")
    private String discipline;

    @ManyToMany
    @JoinTable(name = "Tutor_Student",
            joinColumns = @JoinColumn(name = "tutor_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> students;

    @Override
    public String toString() {
        return "Tutor{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", discipline='" + discipline + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tutor tutor = (Tutor) o;
        return id == tutor.id && age == tutor.age && fullName.equals(tutor.fullName) && password.equals(tutor.password) && email.equals(tutor.email) && discipline.equals(tutor.discipline) && Objects.equals(students, tutor.students);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + fullName.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + age;
        result = 31 * result + discipline.hashCode();
        result = 31 * result + Objects.hashCode(students);
        return result;
    }

    public Tutor() {
    }

    public Tutor(int id, String fullName, String password,
                 String email, int age, String discipline,
                 List<Student> students) {
        this.id = id;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.age = age;
        this.discipline = discipline;
        this.students = students;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public  @NotEmpty(message = "please enter your full name")
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

    public  @NotEmpty(message = "please enter the password")
            @Size(min = 6, message = "sorry, your password should have at least 6 symbols")
            @Size(max = 50, message = "sorry, your password should be shorter than 50 symbols")
    String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty(message = "please enter the pass")
                            @Size(min = 6, message = "sorry, your password should have at least 6 symbols")
                            @Size(max = 50, message = "sorry, your password should be shorter than 50 symbols")
                            String password) {
        this.password = password;
    }

    public @Email(message = "invalid email format") @NotEmpty(message = "email cant be empty")
    String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "invalid email format") @NotEmpty(message = "email cant be empty")
                         String email) {
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

    public @NotEmpty(message = "please enter the discipline")
    @Size(min = 2, message = "sorry, your name should have at least 2 symbols")
    @Size(max = 50, message = "sorry, your name should be shorter than 50 symbols")String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(    @NotEmpty(message = "please enter the discipline")
                                  @Size(min = 2, message = "sorry, your name should have at least 2 symbols")
                                  @Size(max = 50, message = "sorry, your name should be shorter than 50 symbols")
                                  String discipline) {
        this.discipline = discipline;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
