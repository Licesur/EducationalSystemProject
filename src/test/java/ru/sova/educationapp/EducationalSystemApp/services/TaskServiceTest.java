package ru.sova.educationapp.EducationalSystemApp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.repositories.StudentRepository;
import ru.sova.educationapp.EducationalSystemApp.repositories.TaskRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TaskServiceTest {

    private static final long ID = 1;

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализация моков
    }

    @Test
    public void testFindTaskById_shouldCallRepository() {
        final Optional<Task> expected = Optional.ofNullable(mock(Task.class));
        when(taskRepository.findById(ID)).thenReturn(expected);

        final Optional<Task> actual = taskRepository.findById(ID);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(taskRepository, times(1)).findById(ID);
    }

    @Test
    public void testFindAll_shouldCallRepository() {
        final Task task = mock(Task.class);
        final List<Task> expected = Collections.singletonList(task);
        when(taskRepository.findAll()).thenReturn(expected);

        List<Task> actual = taskRepository.findAll();

        assertNotNull(actual);
        assertEquals(actual.size(), expected.size());
        assertEquals(actual, expected);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testSave_shouldCallRepository() {
        final Task task = mock(Task.class);
        when(taskRepository.save(task)).thenReturn(task);

        Task actual = taskRepository.save(task);

        assertNotNull(actual);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testDeleteById_shouldCallRepository() {

        taskRepository.deleteById(ID);

        verify(taskRepository, times(1)).deleteById(ID);
    }

    @Test
    public void testUpdate_shouldCallRepository() {
        final Task task = mock(Task.class);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskRepository.findById(ID)).thenReturn(Optional.of(task));

        Task updatedTask = taskRepository.save(task);
        Optional<Task> foundTask = taskRepository.findById(ID);

        assertEquals(updatedTask, task);
        verify(taskRepository, times(1)).findById(ID);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testFindByVerificationWork_shouldCallRepository() {
        final VerificationWork verificationWork = mock(VerificationWork.class);
        final List<Task> tasks = new ArrayList<>();
        when(taskRepository.findAllByVerificationWorksContains(verificationWork)).thenReturn(tasks);

        List<Task> foundTasks = taskRepository.findAllByVerificationWorksContains(verificationWork);

        assertEquals(tasks, foundTasks);
        verify(taskRepository, times(1))
                .findAllByVerificationWorksContains(verificationWork);
    }

}
