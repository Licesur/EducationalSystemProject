package ru.sova.educationapp.EducationalSystemApp.DTO;


import ru.sova.educationapp.EducationalSystemApp.models.Task;

import java.util.List;

public class VerificationWorkDTO {

    private String title;

    private List<Task> tasks;

    private int id;

    @Override
    public String toString() {
        return "VerificationWorkDTO{" +
                "title='" + title + '\'' +
                ", tasks=" + tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VerificationWorkDTO that = (VerificationWorkDTO) o;
        return id == that.id && title.equals(that.title) && tasks.equals(that.tasks);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + tasks.hashCode();
        result = 31 * result + id;
        return result;
    }

    public VerificationWorkDTO() {
    }

    public VerificationWorkDTO(String title, int id, List<Task> tasks) {
        this.title = title;
        this.tasks = tasks;
        this.id = id;
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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
