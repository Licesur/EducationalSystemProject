package ru.sova.educationapp.EducationalSystemApp.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sova.educationapp.EducationalSystemApp.DTO.TaskDTO;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.repositories.StudentRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    private static final long ID = 1L;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private final VerificationWorkService verificationWorkService = mock(VerificationWorkService.class);

    private final TaskService taskService = mock(TaskService.class);

    @Test
    public void testAddVerificationWork_studentHasNoWorks(){
        VerificationWork verificationWork = mock(VerificationWork.class);
        Student student = new Student(1L, "test name 1", "test password 1",
                "test@mail.ru", 30, null, null);
        doReturn(Optional.of(student)).when(studentRepository).findById(ID);

        boolean result = studentService.addVerificationWork(verificationWork, student);

        assertEquals(verificationWork, student.getVerificationWorks().get(0));
        verify(studentRepository).save(student);
        verify(verificationWorkService).save(verificationWork);
        assertTrue(result);

    }
    @Test
    public void testAddVerificationWork_VerificationWorkAlreadyExists(){
        VerificationWork verificationWork1 = mock(VerificationWork.class);
        VerificationWork verificationWork2 = mock(VerificationWork.class);
        List<VerificationWork> verificationWorks = new ArrayList<>();
        verificationWorks.add(verificationWork1);
        verificationWorks.add(verificationWork2);
        Student student = new Student(1L, "test name 1", "test password 1",
                "test@mail.ru", 30, null, verificationWorks);
        doReturn(Optional.of(student)).when(studentRepository).findById(ID);

        assertNotNull(student.getVerificationWorks());
        assertTrue(student.getVerificationWorks().contains(verificationWork2));

        boolean result = studentService.addVerificationWork(verificationWork2, student);

        assertEquals(student.getVerificationWorks(), verificationWorks);
        verify(studentRepository).save(student);
        verify(verificationWorkService).save(verificationWork2);
        assertTrue(result);
    }
    @Test
    public void testAddVerificationWork_VerificationWorksAlreadyExistsWithoutNewVerificationWork(){
        VerificationWork verificationWork1 = mock(VerificationWork.class);
        VerificationWork verificationWork2 = mock(VerificationWork.class);
        List<VerificationWork> verificationWorks = new ArrayList<>();
        verificationWorks.add(verificationWork1);
        Student student = new Student(1L, "test name 1", "test password 1",
                "test@mail.ru", 30, null, verificationWorks);
        doReturn(Optional.of(student)).when(studentRepository).findById(ID);

        assertNotNull(student.getVerificationWorks());

        boolean result = studentService.addVerificationWork(verificationWork2, student);

        assertEquals(student.getVerificationWorks(), verificationWorks);
        verify(studentRepository).save(student);
        verify(verificationWorkService).save(verificationWork2);
        assertTrue(result);
    }
    @Test
    public void testAddVerificationWork_VerificationWorkDoesNotHaveStudent(){
        VerificationWork verificationWork = new VerificationWork(ID, "test title",
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                null, null);
        Student student = new Student(1L, "test name 1", "test password 1",
                "test@mail.ru", 30, null, null);
        doReturn(Optional.of(student)).when(studentRepository).findById(ID);

        boolean result = studentService.addVerificationWork(verificationWork, student);

        assertEquals(verificationWork.getStudents().get(0), student);
        verify(studentRepository).save(student);
        verify(verificationWorkService).save(verificationWork);
        assertTrue(result);
    }
    @Test
    public void testAddVerificationWork_StudentAlreadyExists(){
        Student student1 = new Student(ID, "test name 1", "test password 1",
                "test@mail.ru", 30, null, null);
        Student student2 = new Student(2L, "test name 1", "test password 1",
                "test@mail.ru", 30, null, null);
        VerificationWork verificationWork = new VerificationWork(ID, "test title",
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                null, List.of(student1, student2));

        doReturn(Optional.of(student1)).when(studentRepository).findById(ID);

        assertNotNull(verificationWork.getStudents());
        assertTrue(verificationWork.getStudents().contains(student1));

        boolean result = studentService.addVerificationWork(verificationWork, student1);

        assertEquals(student1.getVerificationWorks().get(0), verificationWork);
        verify(studentRepository).save(student1);
        verify(verificationWorkService).save(verificationWork);
        assertTrue(result);
    }
    @Test
    public void testAddVerificationWork_StudentsAlreadyExistsWithoutNewStudent(){
        Student student1 = new Student(ID, "test name 1", "test password 1",
                "test@mail.ru", 30, null, null);
        Student student2 = new Student(2L, "test name 1", "test password 1",
                "test@mail.ru", 30, null, null);
        List<Student> students = new ArrayList<>();
        students.add(student2);
        VerificationWork verificationWork = new VerificationWork(ID, "test title",
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                null, students);
        doReturn(Optional.of(student1)).when(studentRepository).findById(ID);


        assertNotNull(verificationWork.getStudents());

        boolean result = studentService.addVerificationWork(verificationWork, student1);

        assertTrue(verificationWork.getStudents().contains(student1));
        verify(studentRepository).save(student1);
        verify(verificationWorkService).save(verificationWork);
        assertTrue(result);
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
                new Student(1L,"testName1", "testPassword1", "test1@email.ru",
                        16, List.of(mock(Tutor.class)), List.of(mock(VerificationWork.class))),
                new Student(2L,"testName2", "testPassword2", "test2@email.ru",
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
    public void testDeleteById_Success() {
        doNothing().when(studentRepository).deleteById(ID);
        when(studentRepository.findById(ID)).thenReturn(Optional.empty());

        Boolean result = studentService.deleteById(ID);

        assertTrue(result);
        verify(studentRepository, times(1)).deleteById(ID);
        verify(studentRepository, times(1)).findById(ID);

    }
    @Test
    public void testDeleteById_NonExistentId() {
        // Настройка поведения мока
        doNothing().when(studentRepository).deleteById(ID);
        when(studentRepository.findById(ID)).thenReturn(Optional.of(new Student())); // запись найдена

        // Мы не должны вызывать deleteById, следовательно, результат должно быть false
        Boolean result = studentService.deleteById(ID);

        // Проверяем, что метод не должен был быть успешно вызван
        verify(studentRepository, times(1)).deleteById(ID);
        verify(studentRepository, times(1)).findById(ID);
        assertFalse(result);
    }

    @Test
    public void testUpdate_Success() {
        Student student = new Student(0L,"testName1", "testPassword1",
                "test1@email.ru", 16, List.of(mock(Tutor.class)),
                List.of(mock(VerificationWork.class)));
        when(studentRepository.findById(ID)).thenReturn(Optional.of(student));

        Boolean result = studentService.update(ID, student);

        assertTrue(result);
        verify(studentRepository, times(2)).findById(ID);
        verify(studentRepository, times(1)).save(student);
    }
    @Test
    public void testUpdate_NotFoundStudent() {
        Student student = new Student(0L,"testName1", "testPassword1",
                "test1@email.ru", 16, List.of(mock(Tutor.class)),
                List.of(mock(VerificationWork.class)));
        when(studentRepository.findById(ID)).thenReturn(Optional.empty());

        Boolean result = studentService.update(ID, student);

        assertFalse(result);
        verify(studentRepository, times(1)).findById(ID);
        verify(studentRepository, times(1)).save(student);
    }
    @Test
    public void testUpdate_NotThatStudent() {
        Student student1 = new Student(0L,"testName1", "testPassword1",
                "test1@email.ru", 16, List.of(mock(Tutor.class)),
                List.of(mock(VerificationWork.class)));
        Student student2 = new Student(1L,"testName2", "testPassword1",
                "test1@email.ru", 16, List.of(mock(Tutor.class)),
                List.of(mock(VerificationWork.class)));
        when(studentRepository.findById(ID)).thenReturn(Optional.of(student2));

        Boolean result = studentService.update(ID, student1);

        assertFalse(result);
        verify(studentRepository, times(2)).findById(ID);
        verify(studentRepository, times(1)).save(student1);
    }
    @Test
    public void testFindByVerificationWorkNotContains_NotFoundAnyStudents() {
        final VerificationWork verificationWork1 = mock(VerificationWork.class);
        final List<Student> students = List.of(mock(Student.class));
        when(studentRepository.findAllByVerificationWorksNotContains(verificationWork1)).thenReturn(students);

        List<Student> result = studentService.findByVerificationWorkNotContains(verificationWork1);

        assertEquals(students, result);
        verify(studentRepository, times(1))
                .findAllByVerificationWorksNotContains(verificationWork1);
    }
    @Test
    public void testFindByVerificationWorkNotContains_FoundStudents() {
        final VerificationWork verificationWork1 = mock(VerificationWork.class);
        final List<Student> students = null;
        when(studentRepository.findAllByVerificationWorksNotContains(verificationWork1)).thenReturn(students);

        List<Student> result = studentService.findByVerificationWorkNotContains(verificationWork1);

        assertEquals(Collections.emptyList(), result);
        verify(studentRepository, times(1))
                .findAllByVerificationWorksNotContains(verificationWork1);
    }

    @Test
    public void testFindByVerificationWork_FoundStudents() {
        final VerificationWork verificationWork1 = mock(VerificationWork.class);
        final List<Student> students = List.of(mock(Student.class));
        when(studentRepository.findAllByVerificationWorksContains(verificationWork1)).thenReturn(students);

        List<Student> result = studentService.findByVerificationWork(verificationWork1);

        assertEquals(students, result);
        verify(studentRepository, times(1))
                .findAllByVerificationWorksContains(verificationWork1);
    }
    @Test
    public void testFindByVerificationWork_NotFoundAnyStudents() {
        final VerificationWork verificationWork1 = mock(VerificationWork.class);
        final List<Student> students = null;
        when(studentRepository.findAllByVerificationWorksContains(verificationWork1)).thenReturn(students);

        List<Student> result = studentService.findByVerificationWork(verificationWork1);

        assertEquals(Collections.emptyList(), result);
        verify(studentRepository, times(1))
                .findAllByVerificationWorksContains(verificationWork1);
    }

    @Test
    public void testFindByTutorsNotContains() {
        final Tutor tutor = mock(Tutor.class);
        final List<Student> students = List.of(mock(Student.class));
        when(studentRepository.findByTutorsNotContains(tutor)).thenReturn(students);

        List<Student> foundStudents = studentService.findByTutorsNotContains(tutor);

        assertEquals(students, foundStudents);
        verify(studentRepository, times(1))
                .findByTutorsNotContains(tutor);
    }

    @Test
    public void testExcludeWork() {
        List<Student> students = new ArrayList<>();
        List<VerificationWork> verificationWorks = new ArrayList<>();
        Student student = new Student(ID,"testName1", "testPassword1",
                "test1@email.ru", 16, List.of(mock(Tutor.class)),
                verificationWorks);
        VerificationWork verificationWork = new VerificationWork(ID, "test title",
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                null, students);
        students.add(student);
        verificationWorks.add(verificationWork);
        student.setVerificationWorks(verificationWorks);
        verificationWork.setStudents(students);
        doReturn(student).when(studentRepository).save(student);
        doReturn(verificationWork).when(verificationWorkService).save(verificationWork);

        studentService.excludeWork(student, verificationWork);

        assertFalse(student.getVerificationWorks().contains(verificationWork));
        assertFalse(verificationWork.getStudents().contains(student));
        verify(studentRepository, times(1)).save(student);
        verify(verificationWorkService, times(1)).save(verificationWork);
    }

    @Test
    public void testCheckAnswers_WithRightAnswers() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task(1L, "task 1 def",
                "task 1 answer", List.of(mock(VerificationWork.class)));
        Task task2 = new Task(2L, "task 2 def",
                "task 2 answer", List.of(mock(VerificationWork.class)));
        tasks.add(task1);
        tasks.add(task2);
        VerificationWork verificationWork = new VerificationWork(ID, "test title",
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                tasks, null);
        doReturn(verificationWork).when(verificationWorkService).findById(ID);
        doReturn(task1).when(taskService).findById(1L);
        doReturn(task2).when(taskService).findById(2L);
        List<TaskDTO> answers = List.of(
                new TaskDTO(1L, "task 1 def", "task 1 answer"),
                new TaskDTO(2L, "task 2 def", "task 2 answer"));

        Map<Long, Boolean> result = studentService.checkAnswers(ID, answers);

        result.forEach((key, value) -> assertTrue(value));
    }

    @Test
    public void testCheckAnswers_WithWrongAnswers() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task(1L, "task 1 def",
                "task 1 answer", List.of(mock(VerificationWork.class)));
        Task task2 = new Task(2L, "task 2 def",
                "task 2 answer", List.of(mock(VerificationWork.class)));
        tasks.add(task1);
        tasks.add(task2);
        VerificationWork verificationWork = new VerificationWork(ID, "test title",
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                tasks, null);
        doReturn(verificationWork).when(verificationWorkService).findById(ID);
        doReturn(task1).when(taskService).findById(1L);
        doReturn(task2).when(taskService).findById(2L);
        List<TaskDTO> answers = List.of(
                new TaskDTO(1L, "task 1 def", "wrong task 1 answer"),
                new TaskDTO(2L, "task 2 def", "wrong task 2 answer"));

        Map<Long, Boolean> result = studentService.checkAnswers(ID, answers);

        result.forEach((key, value) -> assertFalse(value));
    }
    @Test
    public void testCheckAnswers_WithoutAnswers() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task(1L, "task 1 def",
                "task 1 answer", List.of(mock(VerificationWork.class)));
        Task task2 = new Task(2L, "task 2 def",
                "task 2 answer", List.of(mock(VerificationWork.class)));
        tasks.add(task1);
        tasks.add(task2);
        VerificationWork verificationWork = new VerificationWork(ID, "test title",
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                tasks, null);
        doReturn(verificationWork).when(verificationWorkService).findById(ID);
        doReturn(task1).when(taskService).findById(1L);
        doReturn(task2).when(taskService).findById(2L);
        List<TaskDTO> answers = new ArrayList<>();

        Map<Long, Boolean> result = studentService.checkAnswers(ID, answers);

        result.forEach((key, value) -> assertFalse(value));
    }

    @Test
    public void findTasksFromVerificationWork_foundSomeTasks() {
        VerificationWork verificationWork = new VerificationWork(ID, "test title",
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                List.of(mock(Task.class)), null);
        Student student = new Student(ID,"testName1", "testPassword1",
                "test1@email.ru", 16, List.of(mock(Tutor.class)),
                List.of(verificationWork));
        doReturn(Optional.of(student)).when(studentRepository).findById(ID);

        List<Task> foundTasks = studentService.findTasksFromVerificationWork(ID, ID);

        assertNotNull(foundTasks);
        assertEquals(foundTasks, verificationWork.getTasks());
    }
    @Test
    public void findTasksFromVerificationWork_NotFoundTasks() {
        VerificationWork verificationWork = new VerificationWork(2L, "test title",
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                LocalDateTime.of(20001, 1, 1, 0, 0, 0),
                List.of(mock(Task.class)), null);
        Student student = new Student(ID,"testName1", "testPassword1",
                "test1@email.ru", 16, List.of(mock(Tutor.class)),
                List.of(verificationWork));
        doReturn(Optional.of(student)).when(studentRepository).findById(ID);

        Exception exception = assertThrows(NoSuchElementException.class, () ->
                studentService.findTasksFromVerificationWork(ID, ID));

        assertEquals(exception.getMessage(), "No value present");

    }
}

