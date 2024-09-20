package ru.sova.educationapp.EducationalSystemApp.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.sova.educationapp.EducationalSystemApp.models.Task;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class VerificationWorkDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @NotEmpty(message = "please enter the title of the work")
    @Size(min = 2, message = "sorry, your title should have at least 2 symbols")
    @Size(max = 100, message = "sorry, your title should be shorter than 100 symbols")
    @Schema(description = "Заголовок контрольной работы должен содержать от 2 до 100 символов")
    private String title;

    @Schema(description = "Список задач, входящих в работу")
    private List<TaskDTO> tasks;
}
