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
import ru.sova.educationapp.EducationalSystemApp.DTO.StudentDTO;
import ru.sova.educationapp.EducationalSystemApp.DTO.TaskDTO;
import ru.sova.educationapp.EducationalSystemApp.DTO.VerificationWorkDTO;
import ru.sova.educationapp.EducationalSystemApp.mappers.StudentMapper;
import ru.sova.educationapp.EducationalSystemApp.mappers.TaskListMapper;
import ru.sova.educationapp.EducationalSystemApp.mappers.TaskMapper;
import ru.sova.educationapp.EducationalSystemApp.mappers.VerificationWorkMapper;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.services.TaskService;
import ru.sova.educationapp.EducationalSystemApp.services.VerificationWorkService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class VerificationWorkTest {
    @Mock
    private VerificationWorkService verificationWorkService;
    @Mock
    private TaskService taskService;
    @Mock
    private StudentService studentService;
    @Mock
    private VerificationWorkMapper verificationWorkMapper;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private StudentMapper studentMapper;
    @Mock
    private TaskListMapper taskListMapper;
    @InjectMocks
    private VerificationWorkController verificationWorkController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(verificationWorkController).build();
    }

    private final long ID = 1L;

    @Test
    public void testGetVerificationWorks() throws Exception {
        //Выполнение get запроса
        mockMvc.perform(get("/works"))
                .andExpect(status().isOk())  //Ожидаемый статус
                .andExpect(model().attributeExists("works")) //Ожидаемый атрибут модели
                .andExpect(view().name("works/show")); //Ожидаемое представление

        verify(verificationWorkService, times(1)).findAll();
    }
    @Test
    public void testGetVerificationWork() throws Exception {
        Task task1 = mock(Task.class);
        VerificationWork verificationWork1 = mock(VerificationWork.class);
        VerificationWorkDTO verificationWorkDTO1 = mock(VerificationWorkDTO.class);
        TaskDTO taskDTO1 = mock(TaskDTO.class);
        List<Task> tasks = List.of(task1);
        Student student1 = mock(Student.class);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        Student student2 = mock(Student.class);
        StudentDTO studentDTO2 = mock(StudentDTO.class);
        List<Student> students1 = List.of(student1);
        List<Student> students2 = List.of(student2);
        //обеспечение tasks атрибута модели
        doReturn(tasks).when(taskService).findByVerificationWork(verificationWork1);
        doReturn(taskDTO1).when(taskMapper).toTaskDTO(task1);
        //обеспечение work атрибута модели
        doReturn(verificationWorkDTO1).when(verificationWorkMapper).toVerificationWorkDTO(verificationWork1);
        //обеспечение students атрибута моделли
        doReturn(students1).when(studentService).findByVerificationWork(verificationWork1);
        doReturn(verificationWork1).when(verificationWorkService).findById(ID);
        doReturn(studentDTO1).when(studentMapper).toStudentDTO(student1);
        //обеспечение validStudents атрибута модели
        doReturn(students2).when(studentService).findByVerificationWorkNotContains(verificationWork1);
        doReturn(studentDTO2).when(studentMapper).toStudentDTO(student2);

        //Выполнение get запроса
        mockMvc.perform(get("/works/{id}", ID))
                .andExpect(status().isOk()) //Ожидаемый статус
                .andExpect(model().attributeExists("tasks")) //Ожидаемый атрибут модели
                .andExpect(model().attributeExists("work"))
                .andExpect(model().attributeExists("validStudents"))
                .andExpect(model().attributeExists("students"))
                .andExpect(view().name("works/index"));//Ожидаемое представление

        verify(taskService, times(1)).findByVerificationWork(verificationWork1);
        verify(taskMapper, times(1)).toTaskDTO(task1);
        verify(verificationWorkMapper, times(1)).toVerificationWorkDTO(verificationWork1);
        verify(studentService, times(1)).findByVerificationWork(verificationWork1);
        verify(studentService, times(1)).findByVerificationWorkNotContains(verificationWork1);
        verify(verificationWorkService, times(4)).findById(ID);
        verify(studentMapper, times(2)).toStudentDTO(any(Student.class));
    }

    @Test
    public void testNewVerificationWork() throws Exception {
        List<Task> tasks = List.of(mock(Task.class));
        List<TaskDTO> taskDTOs = List.of(mock(TaskDTO.class));
        doReturn(tasks).when(taskService).findAll();
        doReturn(taskDTOs).when(taskListMapper).taskListToTaskDTOList(tasks);

        //Выполнение get запроса
        mockMvc.perform(get("/works/new"))
                .andExpect(status().isOk()) //Ожидаемый статус
                .andExpect(model().attributeExists("tasksDTO"))//Ожидаемые атрибуты модели
                .andExpect(model().attributeExists("work"))
                .andExpect(view().name("works/new")); //Ожидаемое представление

        verify(taskService, times(1)).findAll();
        verify(taskListMapper, times(1)).taskListToTaskDTOList(tasks);
    }
    @Test
    public void testCreate_BidingResultHasErrors() throws Exception {
        VerificationWorkDTO verificationWorkDTO1 = mock(VerificationWorkDTO.class);

        //Выполнение post запроса
        mockMvc.perform(post("/works")
                        .param("selectedTasks", "1", "2")
                        .flashAttr("work", verificationWorkDTO1)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)// Указываемые параметры формы
                        .param("title", ""))
                .andExpect(status().isOk()) // Ожидаемый статус
                .andExpect(view().name("works/new")); // Проверка на редирект
    }
    @Test
    public void testCreate_BindingResultHasNoErrors() throws Exception {
        //Создание объекта с невалидным полем
        VerificationWorkDTO verificationWorkDTO1 = new VerificationWorkDTO();
        verificationWorkDTO1.setId(ID); // Установите необходимые поля
        //Выполняем post запрос
        mockMvc.perform(post("/works")
                        .param("selectedTasks", "1", "2")
                        .flashAttr("work", verificationWorkDTO1)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)// Указываемые параметры формы
                        .param("title", "test title 1"))
                .andExpect(status().is3xxRedirection()) // Ожидаемый статус
                .andExpect(view().name("redirect:/works")); // Проверка на редирект
    }
    @Test
    public void testEditVerificationWork() throws Exception {
        Task task1 = mock(Task.class);
        TaskDTO taskDTO1 = mock(TaskDTO.class);
        VerificationWork verificationWork1 = mock(VerificationWork.class);
        VerificationWorkDTO verificationWorkDTO1 = mock(VerificationWorkDTO.class);
        doReturn(List.of(task1)).when(taskService).findAll();
        doReturn(taskDTO1).when(taskMapper).toTaskDTO(task1);
        doReturn(verificationWork1).when(verificationWorkService).findById(ID);
        doReturn(verificationWorkDTO1).when(verificationWorkMapper).toVerificationWorkDTO(verificationWork1);

        //Выполнение get запроса
        mockMvc.perform(get("/works/{id}/edit", ID))
                .andExpect(status().isOk()) //Ожидаемый статус
                .andExpect(model().attributeExists("work")) //Ожидаемый атрибут модели
                .andExpect(model().attributeExists("tasks"))
                .andExpect(view().name("works/edit")); //Ожидаемое представление

        verify(taskService, times(1)).findAll();
        verify(taskMapper, times(1)).toTaskDTO(task1);
        verify(verificationWorkMapper, times(1)).toVerificationWorkDTO(verificationWork1);
        verify(verificationWorkService, times(1)).findById(ID);
    }

    @Test
    public void testDeleteVerificationWork() throws Exception {
        doReturn(true).when(verificationWorkService).deleteById(ID);

        //Выполнение delete запроса
        mockMvc.perform(delete("/works/{id}", ID))
                .andExpect(status().is3xxRedirection()) // Ожидаем редирект
                .andExpect(view().name("redirect:/works")); //Проверка на редирект

        verify(verificationWorkService, times(1)).deleteById(ID);
    }
    @Test
    public void testUpdateVerificationWork_BidingResultHasErrors() throws Exception {
        VerificationWorkDTO verificationWorkDTO1 = mock(VerificationWorkDTO.class);

        //Выполнение post запроса
        mockMvc.perform(patch("/works/{id}", ID)
                        .param("selectedTasks", "1", "2")
                        .flashAttr("work", verificationWorkDTO1)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)// Указываемые параметры формы
                        .param("title", ""))
                .andExpect(status().isOk()) // Ожидаемый статус
                .andExpect(view().name("works/edit")); // Проверка на редирект
    }

    @Test
    public void testUpdateVerificationWork_BindingResultHasNoErrors() throws Exception {
        //Создание объекта с невалидным полем
        VerificationWorkDTO verificationWorkDTO1 = new VerificationWorkDTO();
        verificationWorkDTO1.setId(ID); // Установите необходимые поля
        //Выполняем post запрос
        mockMvc.perform(patch("/works/{id}", ID)
                        .param("selectedTasks", "1", "2")
                        .flashAttr("work", verificationWorkDTO1)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)// Указываемые параметры формы
                        .param("title", "test title 1"))
                .andExpect(status().is3xxRedirection()) // Ожидаемый статус
                .andExpect(view().name("redirect:/works")); // Проверка на редирект
    }
    @Test
    public void testChooseStudent() throws Exception {
        VerificationWork verificationWork1 = new VerificationWork();
        Student student1 = new Student(ID, "test name 1", "test password 1",
                "test@mail.ru", 16, Collections.emptyList(), Collections.emptyList());
        StudentDTO studentDTO1 = new StudentDTO(ID, "test name 1", "test password 1",
                "test@mail.ru", 16, Collections.emptyList());
        doReturn(student1).when(studentMapper).toStudent(studentDTO1);
        doReturn(verificationWork1).when(verificationWorkService).findById(ID);

        //Выполнение delete запроса
        mockMvc.perform(patch("/works/{id}/choose", ID)
                        .flashAttr("student", studentDTO1))
                .andExpect(status().is3xxRedirection()) // Ожидаем редирект
                .andExpect(view().name("redirect:/works/" + ID)); //Проверка на редирект

        verify(studentService, times(1)).addVerificationWork(verificationWork1, student1);
    }
}
