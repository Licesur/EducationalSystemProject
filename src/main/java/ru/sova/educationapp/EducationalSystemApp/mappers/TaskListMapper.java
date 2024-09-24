package ru.sova.educationapp.EducationalSystemApp.mappers;

import org.mapstruct.Mapper;
import ru.sova.educationapp.EducationalSystemApp.DTO.TaskDTO;
import ru.sova.educationapp.EducationalSystemApp.models.Task;

import java.util.List;

/**
 * Маппер, предназначенный для перевода объекта списка сущностей студента к списку DTO и обратно
 */
@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface TaskListMapper {
    List<TaskDTO> taskListToTaskDTOList(List<Task> taskDTOList);

    List<Task> taskDTOListToTaskList(List<TaskDTO> taskList);
}
