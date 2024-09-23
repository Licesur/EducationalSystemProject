package ru.sova.educationapp.EducationalSystemApp.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.repositories.VerificationWorkRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
@ExtendWith(MockitoExtension.class)
public class VerificationWorkServiceTest {

    private static final long ID = 1;

    @Mock
    private VerificationWorkRepository verificationWorkRepository;

    @InjectMocks
    private VerificationWorkService verificationWorkService;


    @Test
    public void testFindAll_shouldCallRepository() {

        final List<VerificationWork> verificationWorks = List.of(
                new VerificationWork(ID, "test title1",
                        LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                        LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                        null, null),
                new VerificationWork(2L, "test title2",
                        LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                        LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                        null, null));

        doReturn(verificationWorks).when(verificationWorkRepository).findAll();

        var gottenVerificationWorks = verificationWorkService.findAll();

        assertNotNull(gottenVerificationWorks);
        assertEquals(gottenVerificationWorks.size(), verificationWorks.size());
        assertEquals(gottenVerificationWorks, verificationWorks);
        verify(verificationWorkRepository, times(1)).findAll();
    }

    @Test
    public void testFindVerificationWorkById_shouldCallRepository() {
        final VerificationWork expected = mock(VerificationWork.class);
        when(verificationWorkRepository.findById(ID)).thenReturn(Optional.of(expected));

        final VerificationWork actual = verificationWorkService.findById(ID);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(verificationWorkRepository, times(1)).findById(ID);
    }

    @Test
    public void testSave_shouldCallRepository() {
        final VerificationWork verificationWork = mock(VerificationWork.class);
        when(verificationWorkRepository.save(verificationWork)).thenReturn(verificationWork);

        VerificationWork actual = verificationWorkService.save(verificationWork);

        assertNotNull(actual);
        verify(verificationWorkRepository, times(1)).save(verificationWork);
    }

    @Test
    public void testDeleteById_Success() {
        doNothing().when(verificationWorkRepository).deleteById(ID);
        when(verificationWorkRepository.findById(ID)).thenReturn(Optional.empty());

        Boolean result = verificationWorkService.deleteById(ID);

        assertTrue(result);
        verify(verificationWorkRepository, times(1)).deleteById(ID);
        verify(verificationWorkRepository, times(1)).findById(ID);

    }
    @Test
    public void testDeleteById_NonExistentId() {
        // Настройка поведения мока
        doNothing().when(verificationWorkRepository).deleteById(ID);
        when(verificationWorkRepository.findById(ID)).thenReturn(Optional.of(new VerificationWork())); // запись найдена

        // Мы не должны вызывать deleteById, следовательно, результат должно быть false
        Boolean result = verificationWorkService.deleteById(ID);

        // Проверяем, что метод не должен был быть успешно вызван
        verify(verificationWorkRepository, times(1)).deleteById(ID);
        verify(verificationWorkRepository, times(1)).findById(ID);
        assertFalse(result);
    }

    @Test
    public void testUpdate_Success() {
        VerificationWork verificationWork = new VerificationWork(0L, "test title",
                LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                Collections.emptyList(), Collections.emptyList());
        when(verificationWorkRepository.findById(ID)).thenReturn(Optional.of(verificationWork));

        Boolean result = verificationWorkService.update(ID, verificationWork);

        assertTrue(result);
        verify(verificationWorkRepository, times(2)).findById(ID);
        verify(verificationWorkRepository, times(1)).save(verificationWork);
    }
    @Test
    public void testUpdate_NotFoundStudent() {
        VerificationWork verificationWork = new VerificationWork(0L, "test title",
                LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                Collections.emptyList(), Collections.emptyList());
        when(verificationWorkRepository.findById(ID)).thenReturn(Optional.empty());

        Boolean result = verificationWorkService.update(ID, verificationWork);

        assertFalse(result);
        verify(verificationWorkRepository, times(1)).findById(ID);
        verify(verificationWorkRepository, times(1)).save(verificationWork);
    }
    @Test
    public void testUpdate_NotThatStudent() {
        VerificationWork verificationWork1 = new VerificationWork(0L, "test title1",
                LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                Collections.emptyList(), Collections.emptyList());
        VerificationWork verificationWork2 = new VerificationWork(1L, "test title2",
                LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                Collections.emptyList(), Collections.emptyList());
        doReturn(Optional.of(verificationWork2)).when(verificationWorkRepository).findById(ID);

        Boolean result = verificationWorkService.update(ID, verificationWork1);

        assertFalse(result);
        verify(verificationWorkRepository, times(2)).findById(ID);
        verify(verificationWorkRepository, times(1)).save(verificationWork1);
    }

    @Test
    public void fillTasks_shouldCallRepository() {
        final VerificationWork verificationWork = new VerificationWork(0L, "test title1",
                LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                LocalDateTime.of(20001, 01, 01, 0, 0, 0),
                Collections.emptyList(), Collections.emptyList());
        List<Task> tasks = List.of(mock(Task.class), mock(Task.class));

        verificationWorkService.fillTasks(verificationWork, tasks);

        assertNotNull(verificationWork.getTasks());
        assertEquals(verificationWork.getTasks(), tasks);
        verify(verificationWorkRepository, times(1)).save(verificationWork);
    }


}
