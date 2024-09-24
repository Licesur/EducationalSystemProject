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
import ru.sova.educationapp.EducationalSystemApp.DTO.TutorDTO;
import ru.sova.educationapp.EducationalSystemApp.DTO.VerificationWorkDTO;
import ru.sova.educationapp.EducationalSystemApp.mappers.StudentMapper;
import ru.sova.educationapp.EducationalSystemApp.mappers.TutorMapper;
import ru.sova.educationapp.EducationalSystemApp.mappers.VerificationWorkMapper;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.services.VerificationWorkService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {
    @Mock
    private StudentService studentService;
    @Mock
    private StudentMapper studentMapper;
    @Mock
    private VerificationWorkMapper verificationWorkMapper;
    @Mock
    private TutorMapper tutorMapper;
    @Mock
    private VerificationWorkService verificationWorkService;
    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    private final long ID = 1L;

    @Test
    public void testGetStudents() throws Exception {
        //Выполнение get запроса
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())  //Ожидаемый статус
                .andExpect(model().attributeExists("students")) //Ожидаемый атрибут модели
                .andExpect(view().name("students/show")); //Ожидаемое представление

        verify(studentService, times(1)).findAll();
    }

    @Test
    public void testGetStudent() throws Exception {
        Student student1 = mock(Student.class);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        Tutor tutor1 = mock(Tutor.class);
        TutorDTO tutorDTO1 = mock(TutorDTO.class);
        doReturn(student1).when(studentService).findById(ID);
        doReturn(studentDTO1).when(studentMapper).toStudentDTO(student1);
        doReturn(List.of(tutor1)).when(student1).getTutors();
        doReturn(tutorDTO1).when(tutorMapper).toTutorDTO(tutor1);

        //Выполнение get запроса
        mockMvc.perform(get("/students/{id}", ID))
                .andExpect(status().isOk()) //Ожидаемый статус
                .andExpect(model().attributeExists("student")) //Ожидаемый атрибут модели
                .andExpect(model().attributeExists("tutors")) //Ожидаемый атрибут модели
                .andExpect(view().name("students/index"));//Ожидаемое представление

        verify(studentService, times(2)).findById(ID);
        verify(studentMapper, times(1)).toStudentDTO(student1);
    }

    @Test
    public void testNewStudent() throws Exception {
        //Выполнение get запроса
        mockMvc.perform(get("/students/new"))
                .andExpect(status().isOk()) //Ожидаемый статус
                .andExpect(model().attributeExists("student")) //Ожидаемый атрибут модели
                .andExpect(view().name("students/new")); //Ожидаемое представление
    }

    @Test
    public void testCreateStudent_BidingResultHasErrors() throws Exception {
        //Создание объекта с невалидным полем
        StudentDTO studentDTO1 = new StudentDTO(ID, "", "test password 1",
                "test@mail.ru", 16, Collections.emptyList());

        //Выполнение post запроса
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)// Указываемые параметры формы
                        .param("fullName", studentDTO1.getFullName())
                        .param("password", studentDTO1.getPassword())
                        .param("email", studentDTO1.getEmail())
                        .param("age", String.valueOf(studentDTO1.getAge())))
                .andExpect(status().isOk()) // Ожидаемый статус
                .andExpect(view().name("students/new")); // Проверка на редирект
    }

    @Test
    public void testCreateStudent_BindingResultHasNoErrors() throws Exception {
        //Создание корректного объекта
        StudentDTO studentDTO1 = new StudentDTO(ID, "test full name 1", "test password 1",
                "test@mail.ru", 16, Collections.emptyList());
        //Выполняем post запрос
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)// Указываемые параметры формы
                        .param("fullName", studentDTO1.getFullName())
                        .param("password", studentDTO1.getPassword())
                        .param("email", studentDTO1.getEmail())
                        .param("age", String.valueOf(studentDTO1.getAge())))
                .andExpect(status().is3xxRedirection()) // Ожидаем редирект
                .andExpect(view().name("redirect:/students")); // Проверка на редирект
    }

    @Test
    public void testEditStudent() throws Exception {
        Student student1 = mock(Student.class);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        doReturn(student1).when(studentService).findById(ID);
        doReturn(studentDTO1).when(studentMapper).toStudentDTO(student1);

        //Выполнение get запроса
        mockMvc.perform(get("/students/{id}/edit", ID))
                .andExpect(status().isOk()) //Ожидаемый статус
                .andExpect(model().attributeExists("student")) //Ожидаемый атрибут модели
                .andExpect(view().name("students/edit")); //Ожидаемое представление

        verify(studentService, times(1)).findById(ID);
        verify(studentMapper, times(1)).toStudentDTO(student1);
    }

    @Test
    public void testDeleteStudent() throws Exception {
        doReturn(true).when(studentService).deleteById(ID);

        //Выполнение delete запроса
        mockMvc.perform(delete("/students/{id}", ID))
                .andExpect(status().is3xxRedirection()) // Ожидаем редирект
                .andExpect(view().name("redirect:/students")); //Проверка на редирект

        verify(studentService, times(1)).deleteById(ID);
    }

    @Test
    public void testUpdateStudent_BidingResultHasErrors() throws Exception {
        StudentDTO studentDTO1 = new StudentDTO(ID, "", "test password 1",
                "test@mail.ru", 16, Collections.emptyList());

        //Выполнение patch запроса
        mockMvc.perform(patch("/students/{id}", ID)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)// Указываемые параметры формы
                        .param("fullName", studentDTO1.getFullName())
                        .param("password", studentDTO1.getPassword())
                        .param("email", studentDTO1.getEmail())
                        .param("age", String.valueOf(studentDTO1.getAge())))
                .andExpect(status().isOk()) // Ожидаем редирект
                .andExpect(view().name("students/edit")); // Проверка на редирект
    }

    @Test
    public void testUpdateStudent_BindingResultHasNoErrors() throws Exception {
        StudentDTO studentDTO1 = new StudentDTO(ID, "test full name 1", "test password 1",
                "test@mail.ru", 16, Collections.emptyList());

        //Выполнение patch запроса
        mockMvc.perform(patch("/students/{id}", ID)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)// Указываемые параметры формы
                        .param("fullName", studentDTO1.getFullName())
                        .param("password", studentDTO1.getPassword())
                        .param("email", studentDTO1.getEmail())
                        .param("age", String.valueOf(studentDTO1.getAge())))
                .andExpect(status().is3xxRedirection()) // Ожидаем редирект
                .andExpect(view().name("redirect:/students")); // Проверка на редирект
    }
    @Test
    public void testExcludeWork() throws Exception {
        VerificationWork verificationWork1 = new VerificationWork(ID, "test title1",
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                Collections.emptyList(), Collections.emptyList());
        VerificationWorkDTO verificationWorkDTO1 =  new VerificationWorkDTO(ID, "test title1",
                Collections.emptyList());
        List<VerificationWork> verificationWorks = new ArrayList<>();
        verificationWorks.add(verificationWork1);
        Student student1 = new Student(ID, "test name 1", "test password 1",
                "test@mail.ru", 16, Collections.emptyList(), verificationWorks);
        doReturn(verificationWork1).when(verificationWorkMapper).toVerificationWork(verificationWorkDTO1);
        doReturn(verificationWork1).when(verificationWorkService).findById(ID);
        doReturn(student1).when(studentService).findById(ID);

        //Выполнение delete запроса
        mockMvc.perform(patch("/students/{id}/excludeWork", ID)
                        .flashAttr("workToExclude", verificationWorkDTO1))
                .andExpect(status().is3xxRedirection()) // Ожидаем редирект
                .andExpect(view().name("redirect:/students/" + ID)); //Проверка на редирект

        verify(studentService, times(1)).excludeWork(student1, verificationWork1);
    }

}
