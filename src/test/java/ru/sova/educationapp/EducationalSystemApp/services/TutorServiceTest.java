package ru.sova.educationapp.EducationalSystemApp.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.repositories.TutorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
@ExtendWith(MockitoExtension.class)
public class TutorServiceTest {

    private static final long ID = 1L;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private StudentService studentService;
    @InjectMocks
    private TutorService tutorService;


    @Test
    public void testFindTutorById_shouldCallRepository() {
        final Tutor expected = mock(Tutor.class);
        when(tutorRepository.findById(ID)).thenReturn(Optional.of(expected));

        final Tutor actual = tutorService.findById(ID);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(tutorRepository, times(1)).findById(ID);
    }

    @Test
    public void testFindAll_shouldCallRepository() {
        final List<Tutor> tutors = List.of(
                new Tutor(1L,"testName1", "testPassword1", "test1@email.ru",
                        16, "math", List.of(mock(Student.class))),
                new Tutor(2L,"testName2", "testPassword2", "test2@email.ru",
                        16, "Math", List.of(mock(Student.class))));

        doReturn(tutors).when(tutorRepository).findAll();

        var gottenTutors = tutorService.findAll();

        assertNotNull(gottenTutors);
        assertEquals(tutors.size(), gottenTutors.size());
        assertEquals(gottenTutors, tutors);
        verify(tutorRepository, times(1)).findAll();
    }

    @Test
    public void testSave_shouldCallRepository() {
        final Tutor tutor = mock(Tutor.class);
        when(tutorRepository.save(tutor)).thenReturn(tutor);

        Tutor actual = tutorService.save(tutor);

        assertNotNull(actual);
        verify(tutorRepository, times(1)).save(tutor);
    }

    @Test
    public void testDeleteById_Success() {
        doNothing().when(tutorRepository).deleteById(ID);
        when(tutorRepository.findById(ID)).thenReturn(Optional.empty());

        Boolean result = tutorService.deleteById(ID);

        assertTrue(result);
        verify(tutorRepository, times(1)).deleteById(ID);
        verify(tutorRepository, times(1)).findById(ID);

    }
    @Test
    public void testDeleteById_NonExistentId() {
        // Настройка поведения мока
        doNothing().when(tutorRepository).deleteById(ID);
        when(tutorRepository.findById(ID)).thenReturn(Optional.of(new Tutor())); // запись найдена

        // Мы не должны вызывать deleteById, следовательно, результат должно быть false
        Boolean result = tutorService.deleteById(ID);

        // Проверяем, что метод не должен был быть успешно вызван
        verify(tutorRepository, times(1)).deleteById(ID);
        verify(tutorRepository, times(1)).findById(ID);
        assertFalse(result);
    }
    @Test
    public void testUpdate_Success() {
        Tutor tutor = new Tutor(0L,"testName1", "testPassword1",
                "test1@email.ru", 16, "math",
                List.of(mock(Student.class)));
        when(tutorRepository.findById(ID)).thenReturn(Optional.of(tutor));

        Boolean result = tutorService.update(ID, tutor);

        assertTrue(result);
        verify(tutorRepository, times(2)).findById(ID);
        verify(tutorRepository, times(1)).save(tutor);
    }
    @Test
    public void testUpdate_NotFoundTutor() {
        Tutor tutor = new Tutor(0L,"testName1", "testPassword1",
                "test1@email.ru", 16, "math",
                List.of(mock(Student.class)));
        when(tutorRepository.findById(ID)).thenReturn(Optional.empty());

        Boolean result = tutorService.update(ID, tutor);

        assertFalse(result);
        verify(tutorRepository, times(1)).findById(ID);
        verify(tutorRepository, times(1)).save(tutor);
    }
    @Test
    public void testUpdate_NotThatTutor() {
        Tutor tutor1 = new Tutor(0L,"testName1", "testPassword1",
                "test1@email.ru", 16, "math",
                List.of(mock(Student.class)));
        Tutor tutor2 = new Tutor(2L,"testName2", "testPassword2",
                "test2@email.ru", 16, "math",
                List.of(mock(Student.class)));
        when(tutorRepository.findById(ID)).thenReturn(Optional.of(tutor2));

        Boolean result = tutorService.update(ID, tutor1);

        assertFalse(result);
        verify(tutorRepository, times(2)).findById(ID);
        verify(tutorRepository, times(1)).save(tutor1);
    }
    @Test
    public void testExcludeStudent_Success() {
        List<Student> students = new ArrayList<>();
        List<Tutor> tutors = new ArrayList<>();
        Student student = new Student(ID,"testName1", "testPassword1",
                "test1@email.ru", 16, tutors,
                List.of(mock(VerificationWork.class)));
        Tutor tutor = new Tutor(ID,"testName1", "testPassword1",
                "test1@email.ru", 16, "math",
                students);
        students.add(student);
        tutors.add(tutor);
        doReturn(tutor).when(tutorRepository).save(tutor);
        doReturn(student).when(studentService).save(student);

        Boolean result = tutorService.excludeStudent(student, tutor);

        assertTrue(result);
        assertFalse(student.getTutors().contains(tutor));
        assertFalse(tutor.getStudents().contains(student));
        verify(tutorRepository, times(1)).save(tutor);
        verify(studentService, times(1)).save(student);
    }
    @Test
    public void testExcludeStudent_TriedToExcludeButThereIsNoSuchStudent() {
        List<Student> students = new ArrayList<>();
        List<Tutor> tutors = new ArrayList<>();
        Student student1 = new Student(ID,"testName1", "testPassword1",
                "test1@email.ru", 16, tutors,
                List.of(mock(VerificationWork.class)));
        Student student2 = new Student(2L,"testName2", "testPassword2",
                "test2@email.ru", 16, tutors,
                List.of(mock(VerificationWork.class)));
        Tutor tutor = new Tutor(ID,"testName1", "testPassword1",
                "test1@email.ru", 16, "math",
                students);
        students.add(student2);
        tutors.add(tutor);
        doReturn(tutor).when(tutorRepository).save(tutor);
        doReturn(student1).when(studentService).save(student1);

        Boolean result = tutorService.excludeStudent(student1, tutor);

        assertTrue(result);
        assertFalse(student1.getTutors().contains(tutor));
        assertFalse(tutor.getStudents().contains(student1));
        verify(tutorRepository, times(1)).save(tutor);
        verify(studentService, times(1)).save(student1);
    }
    @Test
    public void testAddStudent_TutorHasNoStudents(){
        Student student = mock(Student.class);
        Tutor tutor = new Tutor(1L, "test name 1", "test password 1",
                "test@mail.ru", 30, "math", null);
        doReturn(Optional.of(tutor)).when(tutorRepository).findById(ID);

        boolean result = tutorService.addStudent(student, tutor);

        assertEquals(student, tutor.getStudents().get(0));
        verify(tutorRepository).save(tutor);
        verify(studentService).save(student);
        assertTrue(result);
    }
    @Test
    public void testAddStudent_StudentAlreadyExists(){
        Student student1 = mock(Student.class);
        Student student2 = mock(Student.class);
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        Tutor tutor = new Tutor(1L, "test name 1", "test password 1",
                "test@mail.ru", 30, "math", students);
        doReturn(Optional.of(tutor)).when(tutorRepository).findById(ID);

        assertNotNull(tutor.getStudents());
        assertTrue(tutor.getStudents().contains(student2));

        boolean result = tutorService.addStudent(student2, tutor);

        assertEquals(tutor.getStudents(), students);
        verify(tutorRepository).save(tutor);
        verify(studentService).save(student2);
        assertTrue(result);
    }
    @Test
    public void testAddStudent_StudentsAlreadyExistsWithoutNewStudent(){
        Student student1 = mock(Student.class);
        Student student2 = mock(Student.class);
        List<Student> students = new ArrayList<>();
        students.add(student1);
        Tutor tutor = new Tutor(1L, "test name 1", "test password 1",
                "test@mail.ru", 30, "math", students);
        doReturn(Optional.of(tutor)).when(tutorRepository).findById(ID);

        assertNotNull(tutor.getStudents());

        boolean result = tutorService.addStudent(student2, tutor);

        assertEquals(tutor.getStudents(), students);
        verify(tutorRepository).save(tutor);
        verify(studentService).save(student2);
        assertTrue(result);
    }
    @Test
    public void testAddStudent_StudentDoesNotHaveTutor(){
        Tutor tutor = new Tutor(1L, "test name 1", "test password 1",
                "test@mail.ru", 30, "math", null);
        Student student = new Student(1L, "test name 1", "test password 1",
                "test@mail.ru", 30, null, null);
        doReturn(Optional.of(tutor)).when(tutorRepository).findById(ID);

        boolean result = tutorService.addStudent(student, tutor);

        assertEquals(tutor.getStudents().get(0), student);
        verify(tutorRepository).save(tutor);
        verify(studentService).save(student);
        assertTrue(result);
    }
    @Test
    public void testAddStudent_TutorAlreadyExists(){
        Tutor tutor1 = new Tutor(ID, "test name 1", "test password 1",
                "test@mail.ru", 30, "math", null);
        Tutor tutor2 = new Tutor(2L, "test name 2", "test password 2",
                "test@mail.ru", 30, "math", null);
        Student student1 = new Student(ID, "test name 1", "test password 1",
                "test@mail.ru", 30, List.of(tutor1,tutor2), null);
        doReturn(Optional.of(tutor1)).when(tutorRepository).findById(ID);

        assertNotNull(student1.getTutors());
        assertTrue(student1.getTutors().contains(tutor1));
        boolean result = tutorService.addStudent(student1, tutor1);

        assertEquals(tutor1.getStudents().get(0), student1);
        verify(tutorRepository).save(tutor1);
        verify(studentService).save(student1);
        assertTrue(result);
    }
    @Test
    public void testAddStudent_TutorsAlreadyExistsWithoutNewTutor(){
        Tutor tutor1 = new Tutor(ID, "test name 1", "test password 1",
                "test@mail.ru", 30, "math", null);
        Tutor tutor2 = new Tutor(2L, "test name 2", "test password 2",
                "test@mail.ru", 30, "math", null);
        List<Tutor> tutors = new ArrayList<>();
        tutors.add(tutor2);
        Student student1 = new Student(ID, "test name 1", "test password 1",
                "test@mail.ru", 30, tutors, null);
        doReturn(Optional.of(tutor1)).when(tutorRepository).findById(ID);


        assertNotNull(student1.getTutors());

        boolean result = tutorService.addStudent(student1, tutor1);

        assertTrue(student1.getTutors().contains(tutor1));
        verify(tutorRepository).save(tutor1);
        verify(studentService).save(student1);
        assertTrue(result);
    }

}
