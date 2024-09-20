package ru.sova.educationapp.EducationalSystemApp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sova.educationapp.EducationalSystemApp.DTO.TaskDTO;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.repositories.StudentRepository;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    private static final int ID = 1;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private final VerificationWorkService verificationWorkService = mock(VerificationWorkService.class);

    private final TaskService taskService = mock(TaskService.class);

    @Test
    public void testAddVerificationWork_shouldCallRepositoryAndVerificationWorkService() {
        final Student student = mock(Student.class);
        final VerificationWork verificationWork = mock(VerificationWork.class);
        when(studentRepository.findById(ID)).thenReturn(Optional.of(student));
        when(studentRepository.save(student)).thenReturn(student);
        when(verificationWorkService.save(verificationWork)).thenReturn(verificationWork);

        Optional<Student> actualStudent = studentRepository.findById(ID);
        Student actualStudentAfterSaving = studentRepository.save(student);
        VerificationWork actualWork = verificationWorkService.save(verificationWork);

        assertNotNull(actualStudent);
        assertNotNull(actualWork);
        assertNotNull(actualStudentAfterSaving);

        verify(studentRepository, times(1)).findById(ID);
        verify(verificationWorkService, times(1)).save(verificationWork);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    public void testFindStudentById_shouldCallRepository() {
        final Student expected = mock(Student.class);
        when(studentRepository.findById(ID)).thenReturn(Optional.of(expected));

        final Student actual = studentService.findById(ID);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(studentRepository, times(1)).findById(ID);
    }

    @Test
    public void testFindAll_shouldCallRepository() {
        final List<Student> students = List.of(
                new Student(1,"testName1", "testPassword1", "test1@email.ru",
                        16, List.of(mock(Tutor.class)), List.of(mock(VerificationWork.class))),
                new Student(2,"testName2", "testPassword2", "test2@email.ru",
                        16, List.of(mock(Tutor.class)), List.of(mock(VerificationWork.class))));

        doReturn(students).when(studentRepository).findAll();

        var gottenStudents = studentService.findAll();

        assertNotNull(gottenStudents);
        assertEquals(students.size(), gottenStudents.size());
        assertEquals(gottenStudents, students);
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    public void testSave_shouldCallRepository() {
        final Student student = mock(Student.class);
        when(studentRepository.save(student)).thenReturn(student);

        Student actual = studentService.save(student);

        assertNotNull(actual);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    public void testDeleteById_shouldCallRepository() {

        studentRepository.deleteById(ID);

        verify(studentRepository, times(1)).deleteById(ID);
    }

    @Test
    public void testUpdate_shouldCallRepository() {
        final Student student = mock(Student.class);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentRepository.findById(ID)).thenReturn(Optional.of(student));

        Student updatedStudent = studentRepository.save(student);
        Student foundStudent = studentService.findById(ID);

        assertEquals(updatedStudent, foundStudent);
        verify(studentRepository, times(1)).findById(ID);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    public void testFindByVerificationWorkNotContains_shouldCallRepository() {
        final VerificationWork verificationWork = mock(VerificationWork.class);
        final List<Student> students = new ArrayList<>();
        when(studentRepository.findAllByVerificationWorksNotContains(verificationWork)).thenReturn(students);

        List<Student> foundStudents = studentRepository.findAllByVerificationWorksNotContains(verificationWork);

        assertEquals(students, foundStudents);
        verify(studentRepository, times(1))
                .findAllByVerificationWorksNotContains(verificationWork);
    }

    @Test
    public void testFindByVerificationWork_shouldCallRepository() {
        final VerificationWork verificationWork = mock(VerificationWork.class);
        final List<Student> students = new ArrayList<>();
        when(studentRepository.findAllByVerificationWorksContains(verificationWork)).thenReturn(students);

        List<Student> foundStudents = studentRepository.findAllByVerificationWorksContains(verificationWork);

        assertEquals(students, foundStudents);
        verify(studentRepository, times(1))
                .findAllByVerificationWorksContains(verificationWork);
    }

    @Test
    public void testFindByTutorsNotContains_shouldCallRepository() {
        final Tutor tutor = mock(Tutor.class);
        final List<Student> students = new ArrayList<>();
        when(studentRepository.findByTutorsNotContains(tutor)).thenReturn(students);

        List<Student> foundStudents = studentRepository.findByTutorsNotContains(tutor);

        assertEquals(students, foundStudents);
        verify(studentRepository, times(1))
                .findByTutorsNotContains(tutor);
    }

    @Test
    public void testExcludeWork_shouldCallRepository() {
        final VerificationWork verificationWork = mock(VerificationWork.class);
        final Student student = mock(Student.class);
        when(studentRepository.save(student)).thenReturn(student);
        when(verificationWorkService.save(verificationWork)).thenReturn(verificationWork);

        Student savedStudent = studentRepository.save(student);
        VerificationWork savedVerificationWork = verificationWorkService.save(verificationWork);

        assertEquals(savedStudent, student);
        verify(studentRepository, times(1))
                .save(student);
        assertEquals(savedVerificationWork, verificationWork);
        verify(verificationWorkService, times(1))
                .save(verificationWork);
    }

    @Test
    public void testCheckAnswersWithRightAnswers() {
        List<Task> tasks = List.of(
                new Task(1, "task 1 def",
                        "task 1 answer", List.of(mock(VerificationWork.class))),
                new Task(2, "task 2 def",
                        "task 2 answer", List.of(mock(VerificationWork.class))));
        List<TaskDTO> answers = List.of(
                new TaskDTO(1, "task 1 def", "task 1 answer"),
                new TaskDTO(2, "task 2 def", "task 2 answer"));
        Map<Integer, Boolean> answersStatus = new HashMap<>();
        for (TaskDTO task : answers) {
            if (task.getAnswer().equals(tasks.stream().filter(s -> s.getId() == task.getId()).findAny().get().getAnswer())) {
                answersStatus.put(task.getId(), true);
            } else {
                answersStatus.put(task.getId(), false);
            }
        }
        answersStatus.entrySet().forEach(answer -> assertTrue(answer.getValue()));
    }

    @Test
    public void testCheckAnswersWithInvalidAnswers() {
        List<Task> tasks = List.of(
                new Task(1, "task 1 def",
                        "task 1 answer", List.of(mock(VerificationWork.class))),
                new Task(2, "task 2 def",
                        "task 2 answer", List.of(mock(VerificationWork.class))));
        List<TaskDTO> answers = List.of(
                new TaskDTO(1, "task 1 def", "wrong task 1 answer"),
                new TaskDTO(2, "task 2 def", "wrong task 2 answer"));
        Map<Integer, Boolean> answersStatus = new HashMap<>();
        for (TaskDTO task : answers) {
            if (task.getAnswer().equals(tasks.stream().filter(s -> s.getId() == task.getId()).findAny().get().getAnswer())) {
                answersStatus.put(task.getId(), true);
            } else {
                answersStatus.put(task.getId(), false);
            }
        }
        answersStatus.entrySet().forEach(answer -> assertFalse(answer.getValue()));
    }

    @Test
    public void findTasksFromVerificationWork_shouldCallRepository() {
        final Task task = mock(Task.class);
        final VerificationWork verificationWork = new VerificationWork(
                ID, "test title 1", LocalDateTime.now(),
                LocalDateTime.now(), List.of(task), List.of(mock(Student.class)));
        final Student student = new Student(1,"test name 1", "test password 1",
                "test@email.ru", 16, List.of(mock(Tutor.class)), List.of(verificationWork));
        doReturn(Optional.of(student)).when(studentRepository).findById(ID);
        List<Task> foundTasks = studentRepository.findById(ID).get().getVerificationWorks()
                .stream().filter(s -> s.getId() == ID).findAny().get().getTasks()
                .stream().toList();
        assertNotNull(foundTasks);
        assertEquals(foundTasks.size(), verificationWork.getTasks().size());
        assertEquals(foundTasks, verificationWork.getTasks());
        verify(studentRepository, times(1)).findById(ID);

    }
}

