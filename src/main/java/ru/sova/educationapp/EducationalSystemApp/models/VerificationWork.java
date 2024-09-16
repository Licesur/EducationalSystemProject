package ru.sova.educationapp.EducationalSystemApp.models;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "verificationwork")
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VerificationWork that = (VerificationWork) o;
        return id == that.id && title.equals(that.title) && Objects.equals(assignationDatetime, that.assignationDatetime) && Objects.equals(deadline, that.deadline) && tasks.equals(that.tasks) && Objects.equals(students, that.students);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + title.hashCode();
        result = 31 * result + Objects.hashCode(assignationDatetime);
        result = 31 * result + Objects.hashCode(deadline);
        result = 31 * result + tasks.hashCode();
        result = 31 * result + Objects.hashCode(students);
        return result;
    }

    public VerificationWork() {
    }

    public VerificationWork(int id, String title,
                            LocalDateTime assignationDatetime,
                            LocalDateTime deadline,
                            List<Task> tasks, List<Student> students) {
        this.id = id;
        this.title = title;
        this.assignationDatetime = assignationDatetime;
        this.deadline = deadline;
        this.tasks = tasks;
        this.students = students;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getAssignationDatetime() {
        return assignationDatetime;
    }

    public void setAssignationDatetime(LocalDateTime assignationDatetime) {
        this.assignationDatetime = assignationDatetime;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

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
