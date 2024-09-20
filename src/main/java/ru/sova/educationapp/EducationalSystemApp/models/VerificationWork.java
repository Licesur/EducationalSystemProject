package ru.sova.educationapp.EducationalSystemApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "verificationwork")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class VerificationWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    @NotEmpty(message = "please enter the title of the work")
    @Size(min = 2, message = "sorry, your title should have at least 2 symbols")
    @Size(max = 100, message = "sorry, your title should be shorter than 100 symbols")
    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy hh/mm/ss")
    @Column(name = "assignation_datetime")
    private LocalDateTime assignationDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy hh/mm/ss")
    @Column(name = "deadline")
    private LocalDateTime deadline;

    @ManyToMany
    @JoinTable(name = "verificationwork_task",
            joinColumns = @JoinColumn(name = "verification_work_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    private List<Task> tasks;

    @ManyToMany
    @ToString.Exclude
    @JoinTable(name = "verificationwork_student",
            joinColumns = @JoinColumn(name = "verification_work_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> students;
}
