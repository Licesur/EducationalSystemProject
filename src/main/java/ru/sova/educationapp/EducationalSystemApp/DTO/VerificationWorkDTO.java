package ru.sova.educationapp.EducationalSystemApp.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import ru.sova.educationapp.EducationalSystemApp.models.Task;

import java.util.List;

public class VerificationWorkDTO {

    @NotEmpty(message = "please enter the title of the work")
    @Size(min = 2, message = "sorry, your title should have at least 2 symbols")
    @Size(max = 100, message = "sorry, your title should be shorter than 100 symbols")
    @Schema(description = "Заголовок контрольной работы должен содержать от 2 до 100 символов")
    private String title;

    @Schema(description = "Список задач, входящих в работу")
    private List<TaskDTO> tasks;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
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

    public VerificationWorkDTO(int id, String title, List<TaskDTO> tasks) {
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

    public      @NotEmpty(message = "please enter the title of the work")
                @Size(min = 2, message = "sorry, your title should have at least 2 symbols")
                @Size(max = 100, message = "sorry, your title should be shorter than 100 symbols") String getTitle() {
        return title;
    }

    public void setTitle(    @NotEmpty(message = "please enter the title of the work")
                             @Size(min = 2, message = "sorry, your title should have at least 2 symbols")
                             @Size(max = 100, message = "sorry, your title should be shorter than 100 symbols")
                             String title) {
        this.title = title;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }
}
