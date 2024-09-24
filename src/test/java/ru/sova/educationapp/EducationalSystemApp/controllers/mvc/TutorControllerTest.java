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
import ru.sova.educationapp.EducationalSystemApp.mappers.StudentMapper;
import ru.sova.educationapp.EducationalSystemApp.mappers.TutorMapper;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.services.TutorService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TutorControllerTest {
    @Mock
    private TutorService tutorService;
    @Mock
    private StudentService studentService;
    @Mock
    private TutorMapper tutorMapper;
    @Mock
    private StudentMapper studentMapper;
    @InjectMocks
    private TutorController tutorController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tutorController).build();
    }

    private final long ID = 1L;

    @Test
    public void testGetTutors() throws Exception {
        //Выполнение get запроса
        mockMvc.perform(get("/tutors"))
                .andExpect(status().isOk())  //Ожидаемый статус
                .andExpect(model().attributeExists("tutors")) //Ожидаемый атрибут модели
                .andExpect(view().name("tutors/show")); //Ожидаемое представление

        verify(tutorService, times(1)).findAll();
    }

    @Test
    public void testGetTutor() throws Exception {
        Tutor tutor1 = mock(Tutor.class);
        TutorDTO tutorDTO1 = mock(TutorDTO.class);
        List<Student> studentsOfTheTutor = List.of(mock(Student.class));
        List<Student> validStudents = List.of(mock(Student.class));
        doReturn(tutor1).when(tutorService).findById(ID);
        doReturn(tutorDTO1).when(tutorMapper).toTutorDTO(tutor1);
        doReturn(studentsOfTheTutor).when(tutor1).getStudents();
        doReturn(validStudents).when(studentService).findByTutorsNotContains(tutor1);

        //Выполнение get запроса
        mockMvc.perform(get("/tutors/{id}", ID))
                .andExpect(status().isOk()) //Ожидаемый статус
                .andExpect(model().attributeExists("studentsOfTheTutor")) //Ожидаемый атрибут модели
                .andExpect(model().attributeExists("tutor"))
                .andExpect(model().attributeExists("validStudents"))
                .andExpect(view().name("tutors/index"));//Ожидаемое представление

        verify(tutorService, times(2)).findById(ID);
        verify(tutorMapper, times(1)).toTutorDTO(tutor1);
        verify(tutor1, times(1)).getStudents();
    }

    @Test
    public void testNewTutor() throws Exception {
        //Выполнение get запроса
        mockMvc.perform(get("/tutors/new"))
                .andExpect(status().isOk()) //Ожидаемый статус
                .andExpect(model().attributeExists("tutor")) //Ожидаемый атрибут модели
                .andExpect(view().name("tutors/new")); //Ожидаемое представление
    }
    @Test
    public void testCreateTutor_BidingResultHasErrors() throws Exception {
        //Создание объекта с невалидным полем
        TutorDTO tutorDTO1 = new TutorDTO(ID, "", "test password 1",
                "test@mail.ru", 16, "math", Collections.emptyList());

        //Выполнение post запроса
        mockMvc.perform(post("/tutors")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)// Указываемые параметры формы
                        .param("fullName", tutorDTO1.getFullName())
                        .param("password", tutorDTO1.getPassword())
                        .param("email", tutorDTO1.getEmail())
                        .param("age", String.valueOf(tutorDTO1.getAge()))
                        .param("discipline", tutorDTO1.getDiscipline()))
                .andExpect(status().isOk()) // Ожидаемый статус
                .andExpect(view().name("tutors/new")); // Проверка на редирект
    }
    @Test
    public void testCreateTutor_BindingResultHasNoErrors() throws Exception {
        //Создание объекта с невалидным полем
        TutorDTO tutorDTO1 = new TutorDTO(ID, "test full name 1", "test password 1",
                "test@mail.ru", 16, "math", Collections.emptyList());

        //Выполняем post запрос
        mockMvc.perform(post("/tutors")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)// Указываемые параметры формы
                        .param("fullName", tutorDTO1.getFullName())
                        .param("password", tutorDTO1.getPassword())
                        .param("email", tutorDTO1.getEmail())
                        .param("age", String.valueOf(tutorDTO1.getAge()))
                        .param("discipline", tutorDTO1.getDiscipline()))
                .andExpect(status().is3xxRedirection()) // Ожидаем редирект
                .andExpect(view().name("redirect:/tutors")); // Проверка на редирект
    }
    @Test
    public void testEditTutor() throws Exception {
        Tutor tutor1 = mock(Tutor.class);
        TutorDTO tutorDTO1 = mock(TutorDTO.class);
        doReturn(tutor1).when(tutorService).findById(ID);
        doReturn(tutorDTO1).when(tutorMapper).toTutorDTO(tutor1);

        //Выполнение get запроса
        mockMvc.perform(get("/tutors/{id}/edit", ID))
                .andExpect(status().isOk()) //Ожидаемый статус
                .andExpect(model().attributeExists("tutor")) //Ожидаемый атрибут модели
                .andExpect(view().name("tutors/edit")); //Ожидаемое представление

        verify(tutorService, times(1)).findById(ID);
        verify(tutorMapper, times(1)).toTutorDTO(tutor1);
    }

    @Test
    public void testDeleteTutor() throws Exception {
        doReturn(true).when(tutorService).deleteById(ID);

        //Выполнение delete запроса
        mockMvc.perform(delete("/tutors/{id}", ID))
                .andExpect(status().is3xxRedirection()) // Ожидаем редирект
                .andExpect(view().name("redirect:/tutors")); //Проверка на редирект

        verify(tutorService, times(1)).deleteById(ID);
    }
    @Test
    public void testUpdateTutor_BidingResultHasErrors() throws Exception {
        TutorDTO tutorDTO1 = new TutorDTO(ID, "", "test password 1",
                "test@mail.ru", 16, "math", Collections.emptyList());

        //Выполнение patch запроса
        mockMvc.perform(patch("/tutors/{id}", ID)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)// Указываемые параметры формы
                        .param("fullName", tutorDTO1.getFullName())
                        .param("password", tutorDTO1.getPassword())
                        .param("email", tutorDTO1.getEmail())
                        .param("age", String.valueOf(tutorDTO1.getAge()))
                        .param("discipline", tutorDTO1.getDiscipline()))
                .andExpect(status().isOk()) // Ожидаем редирект
                .andExpect(view().name("tutors/edit")); // Проверка на редирект
    }

    @Test
    public void testUpdateTutor_BindingResultHasNoErrors() throws Exception {
        TutorDTO tutorDTO1 = new TutorDTO(ID, "test full name 1", "test password 1",
                "test@mail.ru", 16, "math", Collections.emptyList());

        //Выполнение patch запроса
        mockMvc.perform(patch("/tutors/{id}", ID)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)// Указываемые параметры формы
                        .param("fullName", tutorDTO1.getFullName())
                        .param("password", tutorDTO1.getPassword())
                        .param("email", tutorDTO1.getEmail())
                        .param("age", String.valueOf(tutorDTO1.getAge()))
                        .param("discipline", tutorDTO1.getDiscipline()))
                .andExpect(status().is3xxRedirection()) // Ожидаем редирект
                .andExpect(view().name("redirect:/tutors")); // Проверка на редирект
    }
    @Test
    public void testExcludeStudent() throws Exception {
        Student student1 = new Student(ID, "test name 1", "test password 1",
                "test@mail.ru", 16, Collections.emptyList(), Collections.emptyList());
        StudentDTO studentDTO1 = new StudentDTO(ID, "test name 1", "test password 1",
                "test@mail.ru", 16, Collections.emptyList());
        List<Student> students = new ArrayList<>();
        students.add(student1);
        Tutor tutor1 = new Tutor(ID, "test name 1", "test password 1",
                "test@mail.ru", 16, "math", students);
        doReturn(student1).when(studentMapper).toStudent(studentDTO1);
        doReturn(student1).when(studentService).findById(ID);
        doReturn(tutor1).when(tutorService).findById(ID);

        //Выполнение delete запроса
        mockMvc.perform(patch("/tutors/{id}/exclude", ID)
                        .flashAttr("student", studentDTO1))
                .andExpect(status().is3xxRedirection()) // Ожидаем редирект
                .andExpect(view().name("redirect:/tutors/" + ID)); //Проверка на редирект

        verify(tutorService, times(1)).excludeStudent(student1, tutor1);
    }
    @Test
    public void testChooseStudent() throws Exception {
        Student student1 = new Student(ID, "test name 1", "test password 1",
                "test@mail.ru", 16, Collections.emptyList(), Collections.emptyList());
        StudentDTO studentDTO1 = new StudentDTO(ID, "test name 1", "test password 1",
                "test@mail.ru", 16, Collections.emptyList());
        List<Student> students = new ArrayList<>();
        Tutor tutor1 = new Tutor(ID, "test name 1", "test password 1",
                "test@mail.ru", 16, "math", students);
        doReturn(student1).when(studentMapper).toStudent(studentDTO1);
        doReturn(student1).when(studentService).findById(ID);
        doReturn(tutor1).when(tutorService).findById(ID);

        //Выполнение delete запроса
        mockMvc.perform(patch("/tutors/{id}/choose", ID)
                        .flashAttr("student", studentDTO1))
                .andExpect(status().is3xxRedirection()) // Ожидаем редирект
                .andExpect(view().name("redirect:/tutors/" + ID)); //Проверка на редирект

        verify(tutorService, times(1)).addStudent(student1, tutor1);
    }

}
