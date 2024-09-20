package ru.sova.educationapp.EducationalSystemApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "task")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "definition")
    @NotEmpty(message = "please enter the definition for the task")
    @Size(min = 3, message = "sorry, your task should be at least 3 symbols length")
    @Size(max = 500, message = "sorry, your definition is too large, please try to insert it in 500 symbols")
    private String definition;
    //todo check restrictions on db tables
    @Column(name = "answer")
    @NotEmpty(message = "please enter thee answer for the task")
    @Size(max = 100, message = "sorry, your answer should be shorter than 100 symbols")
    private String answer;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "tasks")
    private List<VerificationWork> verificationWorks;

//    @Override
//    public String toString() {
//        return "Task{" +
//                "answer='" + answer + '\'' +
//                ", definition='" + definition + '\'' +
//                ", id=" + id +
//                '}';
//    }
}
