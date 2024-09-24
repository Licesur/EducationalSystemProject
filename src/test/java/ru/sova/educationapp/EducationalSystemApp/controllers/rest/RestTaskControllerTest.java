package ru.sova.educationapp.EducationalSystemApp.controllers.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.sova.educationapp.EducationalSystemApp.DTO.TaskDTO;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotCreatedException;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotUpdatedException;
import ru.sova.educationapp.EducationalSystemApp.mappers.TaskMapper;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.services.TaskService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class RestTaskControllerTest {
    @InjectMocks
    private RestTaskController restTaskController;
    @Mock
    private TaskService taskService;
    @Mock
    private TaskMapper taskMapper;

    private final long ID = 1;


    @Test
    public void testGetTasks_FoundTasks(){
        Task task1 = mock(Task.class);
        Task task2 = mock(Task.class);
        List<Task> tasks = List.of(task1, task2);
        TaskDTO taskDTO1 = mock(TaskDTO.class);
        TaskDTO taskDTO2 = mock(TaskDTO.class);
        List<TaskDTO> taskDTOS = List.of(taskDTO1, taskDTO2);
        doReturn(tasks).when(taskService).findAll();
        doReturn(taskDTO1).when(taskMapper).toTaskDTO(task1);
        doReturn(taskDTO2).when(taskMapper).toTaskDTO(task2);

        ResponseEntity<List<TaskDTO>> responseEntity = restTaskController.getTasks();

        assertEquals(ResponseEntity.ok(taskDTOS), responseEntity);
    }
    @Test
    public void testGetTasks_NotFoundAnyTasks(){
        doReturn(Collections.emptyList()).when(taskService).findAll();

        ResponseEntity<List<TaskDTO>> responseEntity = restTaskController.getTasks();

        assertEquals(ResponseEntity.notFound().build(), responseEntity);
    }
    @Test
    public void testGetTask_FoundTask(){
        Task task1 = mock(Task.class);
        TaskDTO taskDTO1 = mock(TaskDTO.class);
        doReturn(task1).when(taskService).findById(ID);
        doReturn(taskDTO1).when(taskMapper).toTaskDTO(task1);

        ResponseEntity<TaskDTO> responseEntity = restTaskController.getTask(ID);

        assertEquals(ResponseEntity.ok(taskDTO1), responseEntity);
        verify(taskMapper, times(1)).toTaskDTO(task1);
        verify(taskService, times(1)).findById(ID);
    }
    @Test
    public void testGetTask_NotFoundTask(){
        doReturn(null).when(taskService).findById(ID);
        doReturn(null).when(taskMapper).toTaskDTO(null);

        ResponseEntity<TaskDTO> responseEntity = restTaskController.getTask(ID);

        assertEquals(ResponseEntity.notFound().build(), responseEntity);
        verify(taskMapper, times(1)).toTaskDTO(null);
        verify(taskService, times(1)).findById(ID);
    }
    @Test
    public void testCreateTask_Success(){
        Task task1 = mock(Task.class);
        TaskDTO taskDTO1 = mock(TaskDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(task1).when(taskMapper).toTask(taskDTO1);

        ResponseEntity<HttpStatus> responseEntity = restTaskController.createTask(taskDTO1, bindingResult);

        assertEquals(ResponseEntity.ok(HttpStatus.CREATED),responseEntity );
        verify(taskMapper, times(1)).toTask(taskDTO1);
        verify(taskService, times(1)).save(task1);
    }
    @Test
    public void testCreateTask_BindingResultHasErrors(){
        Task task1 = mock(Task.class);
        TaskDTO taskDTO1 = mock(TaskDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(true).when(bindingResult).hasErrors();
        FieldError fieldError = mock(FieldError.class);
        doReturn(List.of(fieldError)).when(bindingResult).getFieldErrors();
        doReturn("field1").when(fieldError).getField();
        doReturn("defaultField1Message").when(fieldError).getDefaultMessage();

        Exception exception = assertThrows(NotCreatedException.class, () ->
                restTaskController.createTask(taskDTO1, bindingResult));

        assertEquals("field1 - defaultField1Message;: the task wasn't created",exception.getMessage());
        verify(taskMapper, times(0)).toTask(taskDTO1);
        verify(taskService, times(0)).update(ID, task1);
    }
    @Test
    public void testUpdateTask_Success(){
        Task task1 = mock(Task.class);
        TaskDTO taskDTO1 = mock(TaskDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(task1).when(taskMapper).toTask(taskDTO1);
        doReturn(true).when(taskService).update(ID, task1);

        ResponseEntity<HttpStatus> responseEntity = restTaskController.updateTask(taskDTO1, bindingResult, ID);

        assertEquals(responseEntity, new ResponseEntity<>(HttpStatus.OK));
        verify(taskMapper, times(1)).toTask(taskDTO1);
        verify(taskService, times(1)).update(ID,task1);
    }
    @Test
    public void testUpdateTask_NotSuccess(){
        Task task1 = mock(Task.class);
        TaskDTO taskDTO1 = mock(TaskDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(task1).when(taskMapper).toTask(taskDTO1);
        doReturn(false).when(taskService).update(ID, task1);

        ResponseEntity<HttpStatus> responseEntity = restTaskController.updateTask(taskDTO1, bindingResult, ID);

        assertEquals(responseEntity, new ResponseEntity<>(HttpStatus.NOT_MODIFIED));
        verify(taskMapper, times(1)).toTask(taskDTO1);
        verify(taskService, times(1)).update(ID,task1);
    }
    @Test
    public void testUpdateTask_BindingResultHasErrors(){
        Task task1 = mock(Task.class);
        TaskDTO taskDTO1 = mock(TaskDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(true).when(bindingResult).hasErrors();
        FieldError fieldError = mock(FieldError.class);
        doReturn(List.of(fieldError)).when(bindingResult).getFieldErrors();
        doReturn("field1").when(fieldError).getField();
        doReturn("defaultField1Message").when(fieldError).getDefaultMessage();

        Exception exception = assertThrows(NotUpdatedException.class, () ->
                restTaskController.updateTask(taskDTO1, bindingResult, ID));

        assertEquals("field1 - defaultField1Message;: the task wasn't updated",exception.getMessage() );
        verify(taskMapper, times(0)).toTask(taskDTO1);
        verify(taskService, times(0)).save(task1);
    }
    @Test
    public void testDeleteTask_Success(){
        doReturn(true).when(taskService).deleteById(ID);

        ResponseEntity<?> responseEntity = restTaskController.deleteTask(ID);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        verify(taskService, times(1)).deleteById(ID);
    }
    @Test
    public void testDeleteTask_TaskNotDeleted(){
        doReturn(false).when(taskService).deleteById(ID);

        ResponseEntity<?> responseEntity = restTaskController.deleteTask(ID);

        assertEquals(HttpStatus.NOT_MODIFIED,responseEntity.getStatusCode());
        verify(taskService, times(1)).deleteById(ID);
    }

}
