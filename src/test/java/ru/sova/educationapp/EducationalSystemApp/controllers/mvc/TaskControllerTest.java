package ru.sova.educationapp.EducationalSystemApp.controllers.mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.sova.educationapp.EducationalSystemApp.DTO.TaskDTO;
import ru.sova.educationapp.EducationalSystemApp.mappers.TaskMapper;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.services.TaskService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {
    @Mock
    private TaskService taskService;
    @Mock
    private TaskMapper taskMapper;
    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
     mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    private final long ID = 1L;
    @Test
    public void testGetTasks() throws Exception {
        Task task1 = mock(Task.class);
        Task task2 = mock(Task.class);
        doReturn(List.of(task1,task2)).when(taskService).findAll();

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("tasks"))
                .andExpect(view().name("tasks/show"));

        verify(taskService, times(1)).findAll();
    }
}
