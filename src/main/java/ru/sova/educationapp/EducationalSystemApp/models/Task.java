package ru.sova.educationapp.EducationalSystemApp.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "task")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return id == task.id && definition.equals(task.definition) && answer.equals(task.answer) && Objects.equals(verificationWorks, task.verificationWorks);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + definition.hashCode();
        result = 31 * result + answer.hashCode();
        result = 31 * result + Objects.hashCode(verificationWorks);
        return result;
    }

    public Task() {
    }

    public Task(int id, String definition, String answer, List<VerificationWork> verificationWorks) {
        this.id = id;
        this.definition = definition;
        this.answer = answer;
        this.verificationWorks = verificationWorks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<VerificationWork> getVerificationWorks() {
        return verificationWorks;
    }

    public void setVerificationWorks(List<VerificationWork> verificationWorks) {
        this.verificationWorks = verificationWorks;
    }
}
