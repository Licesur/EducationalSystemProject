package ru.sova.educationapp.EducationalSystemApp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.repositories.TaskRepository;
import ru.sova.educationapp.EducationalSystemApp.repositories.TutorRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TutorServiceTest {

    private static final int ID = 1;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализация моков
    }

    @Test
    public void testFindTutorById_shouldCallRepository() {
        final Optional<Tutor> expected = Optional.ofNullable(mock(Tutor.class));
        when(tutorRepository.findById(ID)).thenReturn(expected);

        final Optional<Tutor> actual = tutorRepository.findById(ID);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(tutorRepository, times(1)).findById(ID);
    }

    @Test
    public void testFindAll_shouldCallRepository() {
        final Tutor tutor = mock(Tutor.class);
        final List<Tutor> expected = Collections.singletonList(tutor);
        when(tutorRepository.findAll()).thenReturn(expected);

        List<Tutor> actual = tutorRepository.findAll();

        assertNotNull(actual);
        assertEquals(actual.size(), expected.size());
        assertEquals(actual, expected);
        verify(tutorRepository, times(1)).findAll();
    }

    @Test
    public void testSave_shouldCallRepository() {
        final Tutor tutor = mock(Tutor.class);
        when(tutorRepository.save(tutor)).thenReturn(tutor);

        Tutor actual = tutorRepository.save(tutor);

        assertNotNull(actual);
        verify(tutorRepository, times(1)).save(tutor);
    }

    @Test
    public void testDeleteById_shouldCallRepository() {

        tutorRepository.deleteById(ID);

        verify(tutorRepository, times(1)).deleteById(ID);
    }

    @Test
    public void testUpdate_shouldCallRepository() {
        final Tutor tutor = mock(Tutor.class);
        when(tutorRepository.save(tutor)).thenReturn(tutor);
        when(tutorRepository.findById(ID)).thenReturn(Optional.of(tutor));

        Tutor updatedTutor = tutorRepository.save(tutor);
        Optional<Tutor> foundTutor = tutorRepository.findById(ID);

        assertEquals(updatedTutor, tutor);
        verify(tutorRepository, times(1)).findById(ID);
        verify(tutorRepository, times(1)).save(tutor);
    }

    @Test
    public void testExcludeStudent_shouldCallRepository() {
        final Tutor tutor = mock(Tutor.class);
        final Student student = mock(Student.class);
        when(tutorRepository.save(tutor)).thenReturn(tutor);
        when(studentService.save(student)).thenReturn(student);

        Student savedStudent = studentService.save(student);
        Tutor savedTutor = tutorRepository.save(tutor);

        assertEquals(savedStudent, student);
        verify(tutorRepository, times(1)).save(tutor);
        assertEquals(savedTutor, tutor);
        verify(studentService, times(1)).save(student);
    }

    @Test
    public void testAddStudent_shouldCallRepositoryAndVerificationWorkService() {
        final Student student = mock(Student.class);
        final Tutor tutor = mock(Tutor.class);
        when(tutorRepository.findById(ID)).thenReturn(Optional.of(tutor));
        when(tutorRepository.save(tutor)).thenReturn(tutor);
        when(studentService.save(student)).thenReturn(student);

        Optional<Tutor> actualTutor = tutorRepository.findById(ID);
        Tutor actualTutorAfterSaving = tutorRepository.save(tutor);
        Student actualStudent = studentService.save(student);

        assertNotNull(actualStudent);
        assertNotNull(actualTutor);
        assertNotNull(actualTutorAfterSaving);

        verify(tutorRepository, times(1)).findById(ID);
        verify(studentService, times(1)).save(student);
        verify(tutorRepository, times(1)).save(tutor);
    }

}
