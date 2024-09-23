package ru.sova.educationapp.EducationalSystemApp.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.repositories.TaskRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    private static final long ID = 1;

    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskService taskService;


    @Test
    public void testFindTaskById() {
        final Task expected = mock(Task.class);
        when(taskRepository.findById(ID)).thenReturn(Optional.of(expected));

        final Task actual = taskService.findById(ID);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(taskRepository, times(1)).findById(ID);
    }


    @Test
    public void testFindAll_shouldCallRepository() {
        final List<Task> tasks = List.of(mock(Task.class), mock(Task.class));

        doReturn(tasks).when(taskRepository).findAll();

        var gottenTasks = taskService.findAll();

        assertNotNull(gottenTasks);
        assertEquals(gottenTasks.size(), tasks.size());
        assertEquals(gottenTasks, tasks);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testSave_shouldCallRepository() {
        final Task task = mock(Task.class);
        when(taskRepository.save(task)).thenReturn(task);

        Task actual = taskService.save(task);

        assertNotNull(actual);
        verify(taskRepository, times(1)).save(task);
    }



    @Test
    public void testDeleteById_Success() {
        doNothing().when(taskRepository).deleteById(ID);
        when(taskRepository.findById(ID)).thenReturn(Optional.empty());

        Boolean result = taskService.deleteById(ID);

        assertTrue(result);
        verify(taskRepository, times(1)).deleteById(ID);
        verify(taskRepository, times(1)).findById(ID);

    }
    @Test
    public void testDeleteById_NonExistentId() {
        // Настройка поведения мока
        doNothing().when(taskRepository).deleteById(ID);
        when(taskRepository.findById(ID)).thenReturn(Optional.of(new Task())); // запись найдена

        // Мы не должны вызывать deleteById, следовательно, результат должно быть false
        Boolean result = taskService.deleteById(ID);

        // Проверяем, что метод не должен был быть успешно вызван
        verify(taskRepository, times(1)).deleteById(ID);
        verify(taskRepository, times(1)).findById(ID);
        assertFalse(result);
    }

    @Test
    public void testUpdate_Success() {
        Task task = new Task(0L, "task definition that will update some task",
                "task answer that will update some task", null);
        when(taskRepository.findById(ID)).thenReturn(Optional.of(task));

        Boolean result = taskService.update(ID, task);

        assertTrue(result);
        verify(taskRepository, times(2)).findById(ID);
        verify(taskRepository, times(1)).save(task);
    }
    @Test
    public void testUpdate_NotFoundStudent() {
        Task task = new Task(0L, "task definition that will update some task",
                "task answer that will update some task", null);
        when(taskRepository.findById(ID)).thenReturn(Optional.empty());

        Boolean result = taskService.update(ID, task);

        assertFalse(result);
        verify(taskRepository, times(1)).findById(ID);
        verify(taskRepository, times(1)).save(task);
    }
    @Test
    public void testUpdate_NotThatStudent() {
        Task task1 = new Task(0L, "task definition that will update some task 1",
                "task answer that will update some task 1", null);

        Task task2 = new Task(2L, "task definition that will be returned",
                "task answer that will be returned", null);

        when(taskRepository.findById(ID)).thenReturn(Optional.of(task2));

        Boolean result = taskService.update(ID, task1);

        assertFalse(result);
        verify(taskRepository, times(2)).findById(ID);
        verify(taskRepository, times(1)).save(task1);
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
    @Test
    public void testFindByVerificationWork_FoundTasks() {
        final VerificationWork verificationWork1 = mock(VerificationWork.class);
        final List<Task> tasks = List.of(mock(Task.class));
        when(taskRepository.findAllByVerificationWorksContains(verificationWork1)).thenReturn(tasks);

        List<Task> result = taskService.findByVerificationWork(verificationWork1);

        assertEquals(tasks, result);
        verify(taskRepository, times(1))
                .findAllByVerificationWorksContains(verificationWork1);
    }
    @Test
    public void testFindByVerificationWork_NotFoundAnyTasks() {
        final VerificationWork verificationWork1 = mock(VerificationWork.class);
        final List<Task> tasks = Collections.emptyList();
        when(taskRepository.findAllByVerificationWorksContains(verificationWork1)).thenReturn(tasks);

        List<Task> result = taskService.findByVerificationWork(verificationWork1);

        assertEquals(Collections.emptyList(), result);
        verify(taskRepository, times(1))
                .findAllByVerificationWorksContains(verificationWork1);
    }

}
