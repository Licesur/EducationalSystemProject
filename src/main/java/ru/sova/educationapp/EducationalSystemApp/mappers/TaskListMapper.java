package ru.sova.educationapp.EducationalSystemApp.mappers;

import org.mapstruct.Mapper;
import ru.sova.educationapp.EducationalSystemApp.DTO.TaskDTO;
import ru.sova.educationapp.EducationalSystemApp.models.Task;

import java.util.List;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface TaskListMapper {
    List<TaskDTO> taskListToTaskDTOList(List<Task> taskDTOList);
    List<Task> taskDTOListToTaskList(List<TaskDTO> taskList);
}
