package ru.sova.educationapp.EducationalSystemApp.controllers.mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.sova.educationapp.EducationalSystemApp.DTO.TaskDTO;
import ru.sova.educationapp.EducationalSystemApp.mappers.TaskMapper;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.services.TaskService;

import static org.mockito.Mockito.*;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        //Выполнение get запроса
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())  //Ожидаемый статус
                .andExpect(model().attributeExists("tasks")) //Ожидаемый атрибут модели
                .andExpect(view().name("tasks/show")); //Ожидаемое представление

        verify(taskService, times(1)).findAll();
    }

    @Test
    public void testGetTask() throws Exception {
        Task task1 = mock(Task.class);
        TaskDTO taskDTO1 = mock(TaskDTO.class);
        doReturn(task1).when(taskService).findById(ID);
        doReturn(taskDTO1).when(taskMapper).toTaskDTO(task1);

        //Выполнение get запроса
        mockMvc.perform(get("/tasks/{id}", ID))
                .andExpect(status().isOk()) //Ожидаемый статус
                .andExpect(model().attributeExists("task")) //Ожидаемый атрибут модели
                .andExpect(view().name("tasks/index"));//Ожидаемое представление

        verify(taskService, times(1)).findById(ID);
        verify(taskMapper, times(1)).toTaskDTO(task1);
    }

    @Test
    public void testNewTask() throws Exception {
        //Выполнение get запроса
        mockMvc.perform(get("/tasks/new"))
                .andExpect(status().isOk()) //Ожидаемый статус
                .andExpect(model().attributeExists("task")) //Ожидаемый атрибут модели
                .andExpect(view().name("tasks/new")); //Ожидаемое представление
    }

    @Test
    public void testCreateTask_BidingResultHasErrors() throws Exception {
        //Создание объекта с невалидным полем
        TaskDTO taskDTO1 = new TaskDTO(ID, "", "test answer 1");

        //Выполнение post запроса
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)// Указываемые параметры формы
                        .param("definition", taskDTO1.getDefinition())
                        .param("answer", taskDTO1.getAnswer()))
                .andExpect(status().isOk()) // Ожидаемый статус
                .andExpect(view().name("tasks/new")); // Проверка на редирект
    }

    @Test
    public void testCreateTask_BindingResultHasNoErrors() throws Exception {
        //Создание корректного объекта
        TaskDTO taskDTO1 = new TaskDTO(ID, "test definition 1", "test answer 1");

        //Выполняем post запрос
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("definition", taskDTO1.getDefinition())// Указываем параметры формы
                        .param("answer", taskDTO1.getAnswer()))
                .andExpect(status().is3xxRedirection()) // Ожидаем редирект
                .andExpect(view().name("redirect:/tasks")); // Проверка на редирект
    }

    @Test
    public void testEditTask() throws Exception {
        Task task1 = mock(Task.class);
        TaskDTO taskDTO1 = mock(TaskDTO.class);
        doReturn(task1).when(taskService).findById(ID);
        doReturn(taskDTO1).when(taskMapper).toTaskDTO(task1);

        //Выполнение get запроса
        mockMvc.perform(get("/tasks/{id}/edit", ID))
                .andExpect(status().isOk()) //Ожидаемый статус
                .andExpect(model().attributeExists("task")) //Ожидаемый атрибут модели
                .andExpect(view().name("tasks/edit")); //Ожидаемое представление

        verify(taskService, times(1)).findById(ID);
        verify(taskMapper, times(1)).toTaskDTO(task1);
    }

    @Test
    public void testDeleteTask() throws Exception {
        Task task1 = mock(Task.class);
        doReturn(true).when(taskService).deleteById(ID);

        //Выполнение delete запроса
        mockMvc.perform(delete("/tasks/{id}", ID))
                .andExpect(status().is3xxRedirection()) // Ожидаем редирект
                .andExpect(view().name("redirect:/tasks")); //Проверка на редирект

        verify(taskService, times(1)).deleteById(ID);
    }

    @Test
    public void testUpdateTask_BidingResultHasErrors() throws Exception {
        TaskDTO taskDTO1 = new TaskDTO(ID, "", "test answer 1");

        //Выполнение patch запроса
        mockMvc.perform(patch("/tasks/{id}", ID)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)// Указываем параметры формы
                        .param("definition", taskDTO1.getDefinition())
                        .param("answer", taskDTO1.getAnswer()))
                .andExpect(status().isOk()) // Ожидаем редирект
                .andExpect(view().name("tasks/edit")); // Проверка на редирект
    }

    @Test
    public void testUpdateTask_BindingResultHasNoErrors() throws Exception {
        TaskDTO taskDTO1 = new TaskDTO(ID, "test definition 1", "test answer 1");

        //Выполнение patch запроса
        mockMvc.perform(patch("/tasks/{id}", ID)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)// Указываем параметры формы
                        .param("definition", taskDTO1.getDefinition())
                        .param("answer", taskDTO1.getAnswer()))
                .andExpect(status().is3xxRedirection()) // Ожидаем редирект
                .andExpect(view().name("redirect:/tasks")); // Проверка на редирект
    }
}

