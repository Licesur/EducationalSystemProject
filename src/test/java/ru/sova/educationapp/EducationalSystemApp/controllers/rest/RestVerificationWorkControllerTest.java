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
import ru.sova.educationapp.EducationalSystemApp.DTO.VerificationWorkDTO;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotAssignedException;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotCreatedException;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotUpdatedException;
import ru.sova.educationapp.EducationalSystemApp.mappers.StudentMapper;
import ru.sova.educationapp.EducationalSystemApp.mappers.VerificationWorkMapper;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.services.VerificationWorkService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class RestVerificationWorkControllerTest {
    @Mock
    private VerificationWorkService verificationWorkService;
    @Mock
    private StudentService studentService;
    @Mock
    private VerificationWorkMapper verificationWorkMapper;
    @Mock
    private StudentMapper studentMapper;
    @InjectMocks
    private RestVerificationWorkController restVerificationWorkController;

    private final long ID = 1;

    @Test
    public void testGetVerificationWorks_FoundVerificationWorks(){
        VerificationWork verificationWork1 = mock(VerificationWork.class);
        VerificationWork verificationWork2 = mock(VerificationWork.class);
        List<VerificationWork> verificationWorks = List.of(verificationWork1, verificationWork2);
        VerificationWorkDTO verificationWorkDTO1 = mock(VerificationWorkDTO.class);
        VerificationWorkDTO verificationWorkDTO2 = mock(VerificationWorkDTO.class);
        List<VerificationWorkDTO> verificationWorkDTOS = List.of(verificationWorkDTO1, verificationWorkDTO2);
        doReturn(verificationWorks).when(verificationWorkService).findAll();
        doReturn(verificationWorkDTO1).when(verificationWorkMapper).toVerificationWorkDTO(verificationWork1);
        doReturn(verificationWorkDTO2).when(verificationWorkMapper).toVerificationWorkDTO(verificationWork2);

        ResponseEntity<List<VerificationWorkDTO>> responseEntity = restVerificationWorkController.getVerificationWorks();

        assertEquals(ResponseEntity.ok(verificationWorkDTOS), responseEntity);
    }
    @Test
    public void testGetVerificationWorks_NotFoundAnyVerificationWorks(){
        doReturn(Collections.emptyList()).when(verificationWorkService).findAll();

        ResponseEntity<List<VerificationWorkDTO>> responseEntity = restVerificationWorkController.getVerificationWorks();

        assertEquals(ResponseEntity.notFound().build(), responseEntity);
    }

    @Test
    public void testGetVerificationWork_FoundVerificationWork(){
        VerificationWork verificationWork1 = mock(VerificationWork.class);
        VerificationWorkDTO verificationWorkDTO1 = mock(VerificationWorkDTO.class);
        doReturn(verificationWork1).when(verificationWorkService).findById(ID);
        doReturn(verificationWorkDTO1).when(verificationWorkMapper).toVerificationWorkDTO(verificationWork1);

        ResponseEntity<VerificationWorkDTO> responseEntity = restVerificationWorkController.getVerificationWork(ID);

        assertEquals(ResponseEntity.ok(verificationWorkDTO1), responseEntity);
        verify(verificationWorkMapper, times(1)).toVerificationWorkDTO(verificationWork1);
        verify(verificationWorkService, times(1)).findById(ID);
    }
    @Test
    public void testGetVerificationWork_NotFoundVerificationWork(){
        doReturn(null).when(verificationWorkService).findById(ID);
        doReturn(null).when(verificationWorkMapper).toVerificationWorkDTO(null);

        ResponseEntity<VerificationWorkDTO> responseEntity = restVerificationWorkController.getVerificationWork(ID);

        assertEquals(ResponseEntity.notFound().build(), responseEntity);
        verify(verificationWorkMapper, times(1)).toVerificationWorkDTO(null);
        verify(verificationWorkService, times(1)).findById(ID);
    }
    @Test
    public void testCreateVerificationWork_Success(){
        VerificationWork verificationWork1 = mock(VerificationWork.class);
        VerificationWorkDTO verificationWorkDTO1 = mock(VerificationWorkDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(verificationWork1).when(verificationWorkMapper).toVerificationWork(verificationWorkDTO1);

        ResponseEntity<HttpStatus> responseEntity =
                restVerificationWorkController.createVerificationWork(verificationWorkDTO1, bindingResult);

        assertEquals(ResponseEntity.ok(HttpStatus.CREATED),responseEntity );
        verify(verificationWorkMapper, times(1)).toVerificationWork(verificationWorkDTO1);
        verify(verificationWorkService, times(1)).save(verificationWork1);
    }
    @Test
    public void testCreateVerificationWork_BindingResultHasErrors(){
        VerificationWork verificationWork1 = mock(VerificationWork.class);
        VerificationWorkDTO verificationWorkDTO1 = mock(VerificationWorkDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(true).when(bindingResult).hasErrors();
        FieldError fieldError = mock(FieldError.class);
        doReturn(List.of(fieldError)).when(bindingResult).getFieldErrors();
        doReturn("field1").when(fieldError).getField();
        doReturn("defaultField1Message").when(fieldError).getDefaultMessage();

        Exception exception = assertThrows(NotCreatedException.class, () ->{
            restVerificationWorkController.createVerificationWork(verificationWorkDTO1, bindingResult);
        });

        assertEquals("field1 - defaultField1Message;: work wasn't created",exception.getMessage());
        verify(verificationWorkMapper, times(0)).toVerificationWork(verificationWorkDTO1);
        verify(verificationWorkService, times(0)).update(ID, verificationWork1);
    }
    @Test
    public void testUpdateVerificationWork_Success(){
        VerificationWork verificationWork1 = mock(VerificationWork.class);
        VerificationWorkDTO verificationWorkDTO1 = mock(VerificationWorkDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(verificationWork1).when(verificationWorkMapper).toVerificationWork(verificationWorkDTO1);
        doReturn(true).when(verificationWorkService).update(ID, verificationWork1);

        ResponseEntity<HttpStatus> responseEntity =
                restVerificationWorkController.updateVerificationWork(verificationWorkDTO1, bindingResult, ID);

        assertEquals(responseEntity, new ResponseEntity<>(HttpStatus.OK));
        verify(verificationWorkMapper, times(1)).toVerificationWork(verificationWorkDTO1);
        verify(verificationWorkService, times(1)).update(ID,verificationWork1);
    }
    @Test
    public void testUpdateVerificationWork_NotSuccess(){
        VerificationWork verificationWork1 = mock(VerificationWork.class);
        VerificationWorkDTO verificationWorkDTO1 = mock(VerificationWorkDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(verificationWork1).when(verificationWorkMapper).toVerificationWork(verificationWorkDTO1);
        doReturn(false).when(verificationWorkService).update(ID, verificationWork1);

        ResponseEntity<HttpStatus> responseEntity =
                restVerificationWorkController.updateVerificationWork(verificationWorkDTO1, bindingResult, ID);

        assertEquals(responseEntity, new ResponseEntity<>(HttpStatus.NOT_MODIFIED));
        verify(verificationWorkMapper, times(1)).toVerificationWork(verificationWorkDTO1);
        verify(verificationWorkService, times(1)).update(ID,verificationWork1);
    }
    @Test
    public void testUpdateVerificationWork_BindingResultHasErrors(){
        VerificationWork verificationWork1 = mock(VerificationWork.class);
        VerificationWorkDTO verificationWorkDTO1 = mock(VerificationWorkDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(true).when(bindingResult).hasErrors();
        FieldError fieldError = mock(FieldError.class);
        doReturn(List.of(fieldError)).when(bindingResult).getFieldErrors();
        doReturn("field1").when(fieldError).getField();
        doReturn("defaultField1Message").when(fieldError).getDefaultMessage();

        Exception exception = assertThrows(NotUpdatedException.class, () ->{
            restVerificationWorkController.updateVerificationWork(verificationWorkDTO1, bindingResult, ID);
        });

        assertEquals("field1 - defaultField1Message;: the chosen work wasn't updated",exception.getMessage() );
        verify(verificationWorkMapper, times(0)).toVerificationWork(verificationWorkDTO1);
        verify(verificationWorkService, times(0)).save(verificationWork1);
    }
    @Test
    public void testDeleteVerificationWork_Success(){
        doReturn(true).when(verificationWorkService).deleteById(ID);

        ResponseEntity<?> responseEntity = restVerificationWorkController.deleteVerificationWork(ID);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        verify(verificationWorkService, times(1)).deleteById(ID);
    }
    @Test
    public void testDeleteVerificationWork_VerificationWorkNotDeleted(){
        doReturn(false).when(verificationWorkService).deleteById(ID);

        ResponseEntity<?> responseEntity = restVerificationWorkController.deleteVerificationWork(ID);

        assertEquals(HttpStatus.NOT_MODIFIED,responseEntity.getStatusCode());
        verify(verificationWorkService, times(1)).deleteById(ID);
    }
    @Test
    public void testAssignToStudent_Success(){
        VerificationWork verificationWork1 = mock(VerificationWork.class);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        Student student1 = mock(Student.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(verificationWork1).when(verificationWorkService).findById(ID);
        doReturn(student1).when(studentMapper).toStudent(studentDTO1);
        doReturn(true).when(studentService).addVerificationWork(verificationWork1,student1);

        ResponseEntity<HttpStatus> responseEntity = restVerificationWorkController.assignToStudent(ID, studentDTO1, bindingResult);

        verify(studentService, times(1)).addVerificationWork(verificationWork1, student1);
        verify(verificationWorkService, times(1)).findById(ID);
        verify(studentMapper, times(1)).toStudent(studentDTO1);
        assertEquals(new ResponseEntity<>(HttpStatus.OK), responseEntity);
    }
    @Test
    public void testAssignToStudent_NotSuccess(){
        VerificationWork verificationWork1 = mock(VerificationWork.class);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        Student student1 = mock(Student.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(verificationWork1).when(verificationWorkService).findById(ID);
        doReturn(student1).when(studentMapper).toStudent(studentDTO1);
        doReturn(false).when(studentService).addVerificationWork(verificationWork1,student1);

        ResponseEntity<HttpStatus> responseEntity = restVerificationWorkController.assignToStudent(ID, studentDTO1, bindingResult);

        verify(studentService, times(1)).addVerificationWork(verificationWork1, student1);
        verify(verificationWorkService, times(1)).findById(ID);
        verify(studentMapper, times(1)).toStudent(studentDTO1);
        assertEquals(new ResponseEntity<>(HttpStatus.NOT_MODIFIED), responseEntity);
    }
    @Test
    public void testAssignToStudent_BindingResultHasErrors(){
        VerificationWork verificationWork1 = mock(VerificationWork.class);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        Student student1 = mock(Student.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(true).when(bindingResult).hasErrors();
        FieldError fieldError = mock(FieldError.class);
        doReturn(List.of(fieldError)).when(bindingResult).getFieldErrors();
        doReturn("field1").when(fieldError).getField();
        doReturn("defaultField1Message").when(fieldError).getDefaultMessage();

        Exception exception = assertThrows(NotAssignedException.class, () ->{
            restVerificationWorkController.assignToStudent(ID, studentDTO1, bindingResult);
        });

        assertEquals("field1 - defaultField1Message;: work wasn't assigned to the chosen student",exception.getMessage());
        verify(studentService, times(0)).addVerificationWork(verificationWork1, student1);
        verify(verificationWorkService, times(0)).findById(ID);
        verify(studentMapper, times(0)).toStudent(studentDTO1);
    }
}
