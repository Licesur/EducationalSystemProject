package ru.sova.educationapp.EducationalSystemApp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWorkTest;
import ru.sova.educationapp.EducationalSystemApp.repositories.StudentRepository;
import ru.sova.educationapp.EducationalSystemApp.repositories.VerificationWorkRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudentServiceTest {

    private static final int ID = 1;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private final VerificationWorkService verificationWorkService = mock(VerificationWorkService.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализация моков
    }
    @Test
    public void testAddVerificationWork_shouldCallRepositoryAndVerificationWorkService(){
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
        final Student student = mock(Student.class);
        final List<Student> expected = Collections.singletonList(student);
        when(studentRepository.findAll()).thenReturn(expected);

        List<Student> actual = studentService.finAll();

        assertNotNull(actual);
        assertEquals(actual.size(), expected.size());
        assertEquals(actual, expected);
        verify(studentRepository, times(1)).findAll();
    }
    @Test
    public void testSave_shouldCallRepository(){
        final Student student = mock(Student.class);
        when(studentRepository.save(student)).thenReturn(student);

        Student actual = studentService.save(student);

        assertNotNull(actual);
        verify(studentRepository, times(1)).save(student);
    }
    @Test
    public void testDeleteById_shouldCallRepository(){

        studentRepository.deleteById(ID);

        verify(studentRepository, times(1)).deleteById(ID);
    }
    @Test
    public void testUpdate_shouldCallRepository(){
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
    public void testFindByVerificationWorkNotContains_shouldCallRepository(){
        final VerificationWork verificationWork = mock(VerificationWork.class);
        final List<Student> students = new ArrayList<>();
        when(studentRepository.findAllByVerificationWorksNotContains(verificationWork)).thenReturn(students);

        List<Student> foundStudents = studentRepository.findAllByVerificationWorksNotContains(verificationWork);

        assertEquals(students, foundStudents);
        verify(studentRepository, times(1))
                .findAllByVerificationWorksNotContains(verificationWork);
    }
    @Test
    public void testFindByVerificationWork_shouldCallRepository(){
        final VerificationWork verificationWork = mock(VerificationWork.class);
        final List<Student> students = new ArrayList<>();
        when(studentRepository.findAllByVerificationWorksContains(verificationWork)).thenReturn(students);

        List<Student> foundStudents = studentRepository.findAllByVerificationWorksContains(verificationWork);

        assertEquals(students, foundStudents);
        verify(studentRepository, times(1))
                .findAllByVerificationWorksContains(verificationWork);
    }
    @Test
    public void testFindByTutorsNotContains_shouldCallRepository(){
        final Tutor tutor = mock(Tutor.class);
        final List<Student> students = new ArrayList<>();
        when(studentRepository.findByTutorsNotContains(tutor)).thenReturn(students);

        List<Student> foundStudents = studentRepository.findByTutorsNotContains(tutor);

        assertEquals(students, foundStudents);
        verify(studentRepository, times(1))
                .findByTutorsNotContains(tutor);
    }
    @Test
    public void testExcludeWork_shouldCallRepository(){
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
}
