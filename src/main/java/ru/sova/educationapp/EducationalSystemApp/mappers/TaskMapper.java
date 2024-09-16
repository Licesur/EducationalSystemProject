package ru.sova.educationapp.EducationalSystemApp.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.sova.educationapp.EducationalSystemApp.DTO.TaskDTO;
import ru.sova.educationapp.EducationalSystemApp.models.Task;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    List<TaskDTO> taskDTOListToTaskDTOList(List<Task> tasks);

    List<Task> taskDTOListToTaskList(List<TaskDTO> taskDTOs);

    @Mapping(source = "id", target = "id")
    TaskDTO toTaskDTO(Task task);
    @Mapping(source = "id", target = "id")
    Task toTask(TaskDTO taskDTO);
}
