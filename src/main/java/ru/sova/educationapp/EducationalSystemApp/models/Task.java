package ru.sova.educationapp.EducationalSystemApp.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "task")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "definition")
    private String definition;

    @Column(name = "answer")
    private String answer;

    @ManyToMany(mappedBy = "tasks")
    private List<VerificationWork> verificationWorks;

    @Override
    public String toString() {
        return "Task{" +
                "answer='" + answer + '\'' +
                ", definition='" + definition + '\'' +
                ", id=" + id +
                '}';
    }
}
