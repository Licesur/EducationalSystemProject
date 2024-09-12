package ru.sova.educationapp.EducationalSystemApp.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "verificationwork")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class VerificationWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
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
    @JoinTable(name = "verificationwork_student",
            joinColumns = @JoinColumn(name = "verification_work_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> students;

    @Override
    public String toString() {
        return "VerificationWork{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", assignationDatetime=" + assignationDatetime +
                ", deadline=" + deadline +
                ", tasks=" + tasks +
                '}';
    }
}
