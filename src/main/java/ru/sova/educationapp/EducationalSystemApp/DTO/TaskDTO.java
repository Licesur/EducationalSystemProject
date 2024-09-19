package ru.sova.educationapp.EducationalSystemApp.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class TaskDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @NotEmpty(message = "please enter the definition for the task")
    @Schema(description = "Текст задачи должен содержать от 3 до 500 символов")
    @Size(min = 3, message = "sorry, your task should be at least 3 symbols length")
    @Size(max = 500, message = "sorry, your definition is too large, please try to insert it in 500 symbols")
    private String definition;

    @NotEmpty(message = "please enter thee answer for the task")
    @Schema(description = "Ответ задачи должен может содержать максимум 100 символов")
    @Size(max = 100, message = "sorry, your answer should be shorter than 100 symbols")
    private String answer;

    @Override
    public String toString() {
        return "Task{" +id +
                ", definition='" + definition + '\'' +
                "answer='" + answer + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskDTO taskDTO = (TaskDTO) o;
        return id == taskDTO.id && definition.equals(taskDTO.definition) && answer.equals(taskDTO.answer);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + definition.hashCode();
        result = 31 * result + answer.hashCode();
        return result;
    }

    public TaskDTO() {
    }

    public TaskDTO(int id, String definition, String answer) {
        this.id = id;
        this.definition = definition;
        this.answer = answer;
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
}
