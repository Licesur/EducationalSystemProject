package ru.sova.educationapp.EducationalSystemApp.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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
}
