package ru.sova.educationapp.EducationalSystemApp.controllers.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.sova.educationapp.EducationalSystemApp.DTO.StudentDTO;
import ru.sova.educationapp.EducationalSystemApp.DTO.TutorDTO;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotAssignedException;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotCreatedException;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotExcludedException;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotUpdatedException;
import ru.sova.educationapp.EducationalSystemApp.mappers.TutorMapper;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.services.TutorService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TutorControllerTest {

    @Mock
    private TutorService tutorService;
    @Mock
    private StudentService studentService;
    @Mock
    private TutorMapper tutorMapper;
    @InjectMocks
    private TutorController tutorController;

    private final long ID = 1;


    @Test
    public void testGetTutors_FoundTutors(){
        Tutor tutor1 = mock(Tutor.class);
        Tutor tutor2 = mock(Tutor.class);
        List<Tutor> tutors = List.of(tutor1, tutor2);
        TutorDTO tutorDTO1 = mock(TutorDTO.class);
        TutorDTO tutorDTO2 = mock(TutorDTO.class);
        List<TutorDTO> tutorDTOS = List.of(tutorDTO1, tutorDTO2);
        doReturn(tutors).when(tutorService).findAll();
        doReturn(tutorDTO1).when(tutorMapper).toTutorDTO(tutor1);
        doReturn(tutorDTO2).when(tutorMapper).toTutorDTO(tutor2);

        ResponseEntity<List<TutorDTO>> responseEntity = tutorController.getTutors();

        assertEquals(ResponseEntity.ok(tutorDTOS), responseEntity);
    }
    @Test
    public void testGetTutors_NotFoundAnyTutors(){
        doReturn(Collections.emptyList()).when(tutorService).findAll();

        ResponseEntity<List<TutorDTO>> responseEntity = tutorController.getTutors();

        assertEquals(ResponseEntity.notFound().build(), responseEntity);
    }

    @Test
    public void testGetTutor_FoundTutor(){
        Tutor tutor1 = mock(Tutor.class);
        TutorDTO tutorDTO1 = mock(TutorDTO.class);
        doReturn(tutor1).when(tutorService).findById(ID);
        doReturn(tutorDTO1).when(tutorMapper).toTutorDTO(tutor1);

        ResponseEntity<TutorDTO> responseEntity = tutorController.getTutorById(ID);

        assertEquals(ResponseEntity.ok(tutorDTO1), responseEntity);
        verify(tutorMapper, times(1)).toTutorDTO(tutor1);
        verify(tutorService, times(1)).findById(ID);
    }
    @Test
    public void testGetTutor_NotFoundTutor(){
        doReturn(null).when(tutorService).findById(ID);
        doReturn(null).when(tutorMapper).toTutorDTO(null);

        ResponseEntity<TutorDTO> responseEntity = tutorController.getTutorById(ID);

        assertEquals(ResponseEntity.notFound().build(), responseEntity);
        verify(tutorMapper, times(1)).toTutorDTO(null);
        verify(tutorService, times(1)).findById(ID);
    }
    @Test
    public void testCreate_Success(){
        Tutor tutor1 = mock(Tutor.class);
        TutorDTO tutorDTO1 = mock(TutorDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(tutor1).when(tutorMapper).toTutor(tutorDTO1);

        ResponseEntity<HttpStatus> responseEntity = tutorController.create(tutorDTO1, bindingResult);

        assertEquals(ResponseEntity.ok(HttpStatus.CREATED),responseEntity );
        verify(tutorMapper, times(1)).toTutor(tutorDTO1);
        verify(tutorService, times(1)).save(tutor1);
    }
    @Test
    public void testCreate_BindingResultHasErrors(){
        Tutor tutor1 = mock(Tutor.class);
        TutorDTO tutorDTO1 = mock(TutorDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(true).when(bindingResult).hasErrors();
        FieldError fieldError = mock(FieldError.class);
        doReturn(List.of(fieldError)).when(bindingResult).getFieldErrors();
        doReturn("field1").when(fieldError).getField();
        doReturn("defaultField1Message").when(fieldError).getDefaultMessage();

        Exception exception = assertThrows(NotCreatedException.class, () ->{
            tutorController.create(tutorDTO1, bindingResult);
        });

        assertEquals("field1 - defaultField1Message;: the tutor wasn't created",exception.getMessage());
        verify(tutorMapper, times(0)).toTutor(tutorDTO1);
        verify(tutorService, times(0)).update(ID, tutor1);
    }
    @Test
    public void testUpdate_Success(){
        Tutor tutor1 = mock(Tutor.class);
        TutorDTO tutorDTO1 = mock(TutorDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(tutor1).when(tutorMapper).toTutor(tutorDTO1);
        doReturn(true).when(tutorService).update(ID, tutor1);

        ResponseEntity<HttpStatus> responseEntity = tutorController.update(tutorDTO1, bindingResult, ID);

        assertEquals(responseEntity, new ResponseEntity<>(HttpStatus.OK));
        verify(tutorMapper, times(1)).toTutor(tutorDTO1);
        verify(tutorService, times(1)).update(ID,tutor1);
    }
    @Test
    public void testUpdate_NotSuccess(){
        Tutor tutor1 = mock(Tutor.class);
        TutorDTO tutorDTO1 = mock(TutorDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(tutor1).when(tutorMapper).toTutor(tutorDTO1);
        doReturn(false).when(tutorService).update(ID, tutor1);

        ResponseEntity<HttpStatus> responseEntity = tutorController.update(tutorDTO1, bindingResult, ID);

        assertEquals(responseEntity, new ResponseEntity<>(HttpStatus.NOT_MODIFIED));
        verify(tutorMapper, times(1)).toTutor(tutorDTO1);
        verify(tutorService, times(1)).update(ID,tutor1);
    }
    @Test
    public void testUpdate_BindingResultHasErrors(){
        Tutor tutor1 = mock(Tutor.class);
        TutorDTO tutorDTO1 = mock(TutorDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(true).when(bindingResult).hasErrors();
        FieldError fieldError = mock(FieldError.class);
        doReturn(List.of(fieldError)).when(bindingResult).getFieldErrors();
        doReturn("field1").when(fieldError).getField();
        doReturn("defaultField1Message").when(fieldError).getDefaultMessage();

        Exception exception = assertThrows(NotUpdatedException.class, () ->{
            tutorController.update(tutorDTO1, bindingResult, ID);
        });

        assertEquals("field1 - defaultField1Message;: the tutor wasn't updated",exception.getMessage() );
        verify(tutorMapper, times(0)).toTutor(tutorDTO1);
        verify(tutorService, times(0)).save(tutor1);
    }
    @Test
    public void testDelete_Success(){
        doReturn(true).when(tutorService).deleteById(ID);

        ResponseEntity<?> responseEntity = tutorController.deleteTutor(ID);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        verify(tutorService, times(1)).deleteById(ID);
    }
    @Test
    public void testDelete_TutorNotDeleted(){
        doReturn(false).when(tutorService).deleteById(ID);

        ResponseEntity<?> responseEntity = tutorController.deleteTutor(ID);

        assertEquals(HttpStatus.NOT_MODIFIED,responseEntity.getStatusCode());
        verify(tutorService, times(1)).deleteById(ID);
    }
    @Test
    public void testAssignStudent_Success(){
        Tutor tutor1 = mock(Tutor.class);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        Student student1 = mock(Student.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(ID).when(studentDTO1).getId();
        doReturn(student1).when(studentService).findById(ID);
        doReturn(tutor1).when(tutorService).findById(ID);
        doReturn(true).when(tutorService).addStudent(student1,tutor1);

        ResponseEntity<HttpStatus> responseEntity = tutorController.assignStudent(ID, studentDTO1, bindingResult);

        verify(tutorService, times(1)).addStudent(student1, tutor1);
        verify(tutorService, times(1)).findById(ID);
        verify(studentService, times(1)).findById(ID);
        assertEquals(new ResponseEntity<>(HttpStatus.OK), responseEntity);
    }
    @Test
    public void testAssignStudent_NotSuccess(){
        Tutor tutor1 = mock(Tutor.class);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        Student student1 = mock(Student.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(ID).when(studentDTO1).getId();
        doReturn(student1).when(studentService).findById(ID);
        doReturn(tutor1).when(tutorService).findById(ID);
        doReturn(false).when(tutorService).addStudent(student1,tutor1);

        ResponseEntity<HttpStatus> responseEntity = tutorController.assignStudent(ID, studentDTO1, bindingResult);

        verify(tutorService, times(1)).addStudent(student1, tutor1);
        verify(tutorService, times(1)).findById(ID);
        verify(studentService, times(1)).findById(ID);
        assertEquals(new ResponseEntity<>(HttpStatus.NOT_MODIFIED), responseEntity);
    }
    @Test
    public void testAssignStudent_BindingResultHasErrors(){
        Tutor tutor1 = mock(Tutor.class);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        Student student1 = mock(Student.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(true).when(bindingResult).hasErrors();
        FieldError fieldError = mock(FieldError.class);
        doReturn(List.of(fieldError)).when(bindingResult).getFieldErrors();
        doReturn("field1").when(fieldError).getField();
        doReturn("defaultField1Message").when(fieldError).getDefaultMessage();

        Exception exception = assertThrows(NotAssignedException.class, () ->{
            tutorController.assignStudent(ID, studentDTO1, bindingResult);
        });

        assertEquals("field1 - defaultField1Message;: the student wasn't assigned",exception.getMessage());
        verify(tutorService, times(0)).addStudent(student1, tutor1);
        verify(tutorService, times(0)).findById(ID);
        verify(studentService, times(0)).findById(ID);
    }
    @Test
    public void testExcludeStudent_Success(){
        Tutor tutor1 = mock(Tutor.class);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        Student student1 = mock(Student.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(ID).when(studentDTO1).getId();
        doReturn(student1).when(studentService).findById(ID);
        doReturn(tutor1).when(tutorService).findById(ID);
        doReturn(true).when(tutorService).excludeStudent(student1,tutor1);

        ResponseEntity<HttpStatus> responseEntity = tutorController.excludeStudent(ID, studentDTO1, bindingResult);

        verify(tutorService, times(1)).excludeStudent(student1, tutor1);
        verify(tutorService, times(1)).findById(ID);
        verify(studentService, times(1)).findById(ID);
        assertEquals(new ResponseEntity<>(HttpStatus.OK), responseEntity);
    }
    @Test
    public void testExcludeStudent_NotSuccess(){
        Tutor tutor1 = mock(Tutor.class);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        Student student1 = mock(Student.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(ID).when(studentDTO1).getId();
        doReturn(student1).when(studentService).findById(ID);
        doReturn(tutor1).when(tutorService).findById(ID);
        doReturn(false).when(tutorService).excludeStudent(student1,tutor1);

        ResponseEntity<HttpStatus> responseEntity = tutorController.excludeStudent(ID, studentDTO1, bindingResult);

        verify(tutorService, times(1)).excludeStudent(student1, tutor1);
        verify(tutorService, times(1)).findById(ID);
        verify(studentService, times(1)).findById(ID);
        assertEquals(new ResponseEntity<>(HttpStatus.NOT_MODIFIED), responseEntity);
    }
    @Test
    public void testExcludeStudent_BindingResultHasErrors(){
        Tutor tutor1 = mock(Tutor.class);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        Student student1 = mock(Student.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(true).when(bindingResult).hasErrors();
        FieldError fieldError = mock(FieldError.class);
        doReturn(List.of(fieldError)).when(bindingResult).getFieldErrors();
        doReturn("field1").when(fieldError).getField();
        doReturn("defaultField1Message").when(fieldError).getDefaultMessage();

        Exception exception = assertThrows(NotExcludedException.class, () ->{
            tutorController.excludeStudent(ID, studentDTO1, bindingResult);
        });

        assertEquals("field1 - defaultField1Message;: the student wasn't excluded",exception.getMessage());
        verify(tutorService, times(0)).excludeStudent(student1, tutor1);
        verify(tutorService, times(0)).findById(ID);
        verify(studentService, times(0)).findById(ID);
    }
}
