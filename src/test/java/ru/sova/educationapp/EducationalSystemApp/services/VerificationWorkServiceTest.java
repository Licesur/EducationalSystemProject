package ru.sova.educationapp.EducationalSystemApp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.repositories.TutorRepository;
import ru.sova.educationapp.EducationalSystemApp.repositories.VerificationWorkRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class VerificationWorkServiceTest {

    private static final int ID = 1;

    @Mock
    private VerificationWorkRepository verificationWorkRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализация моков
    }

    @Test
    public void testFindTutorById_shouldCallRepository() {
        final Optional<VerificationWork> expected = Optional.ofNullable(mock(VerificationWork.class));
        when(verificationWorkRepository.findById(ID)).thenReturn(expected);

        final Optional<VerificationWork> actualVerificationWork = verificationWorkRepository.findById(ID);

        assertNotNull(actualVerificationWork);
        assertEquals(expected, actualVerificationWork);
        verify(verificationWorkRepository, times(1)).findById(ID);
    }
    @Test
    public void testFindAll_shouldCallRepository() {
        final VerificationWork verificationWork = mock(VerificationWork.class);
        final List<VerificationWork> expected = Collections.singletonList(verificationWork);
        when(verificationWorkRepository.findAll()).thenReturn(expected);

        List<VerificationWork> actualVerificationWorks = verificationWorkRepository.findAll();

        assertNotNull(actualVerificationWorks);
        assertEquals(actualVerificationWorks.size(), expected.size());
        assertEquals(actualVerificationWorks, expected);
        verify(verificationWorkRepository, times(1)).findAll();
    }
    @Test
    public void testSave_shouldCallRepository(){
        final VerificationWork verificationWork = mock(VerificationWork.class);
        when(verificationWorkRepository.save(verificationWork)).thenReturn(verificationWork);

        VerificationWork actualVerificationWork = verificationWorkRepository.save(verificationWork);

        assertNotNull(actualVerificationWork);
        assertEquals(actualVerificationWork, verificationWork);
        verify(verificationWorkRepository, times(1)).save(verificationWork);
    }
    @Test
    public void testDeleteById_shouldCallRepository(){

        verificationWorkRepository.deleteById(ID);

        verify(verificationWorkRepository, times(1)).deleteById(ID);
    }
    @Test
    public void testUpdate_shouldCallRepository(){
        final VerificationWork verificationWork = mock(VerificationWork.class);
        when(verificationWorkRepository.save(verificationWork)).thenReturn(verificationWork);

        VerificationWork updatedVerificationWork = verificationWorkRepository.save(verificationWork);

        assertEquals(updatedVerificationWork, verificationWork);
        verify(verificationWorkRepository, times(1)).save(verificationWork);
    }
    @Test
    public void addTasks_shouldCallRepository(){
        final VerificationWork verificationWork = mock(VerificationWork.class);
        when(verificationWorkRepository.save(verificationWork)).thenReturn(verificationWork);

        VerificationWork actualVerificationWork = verificationWorkRepository.save(verificationWork);

        assertNotNull(actualVerificationWork);
        assertEquals(actualVerificationWork, verificationWork);
        verify(verificationWorkRepository, times(1)).save(verificationWork);
    }

}
