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
import ru.sova.educationapp.EducationalSystemApp.DTO.TaskDTO;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotCreatedException;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotUpdatedException;
import ru.sova.educationapp.EducationalSystemApp.mappers.StudentMapperImpl;
import ru.sova.educationapp.EducationalSystemApp.mappers.TaskListMapperImpl;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    @InjectMocks
    private StudentController studentController;
    @Mock
    private StudentService studentService;
    @Mock
    private StudentMapperImpl studentMapper;
    @Mock
    private TaskListMapperImpl taskListMapper;

    private final long ID = 1;

    @Test
    public void testGetStudents_FoundStudents(){
        Student student1 = mock(Student.class);
        Student student2 = mock(Student.class);
        List<Student> students = List.of(student1, student2);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        StudentDTO studentDTO2 = mock(StudentDTO.class);
        List<StudentDTO> studentDTOS = List.of(studentDTO1, studentDTO2);
        doReturn(students).when(studentService).findAll();
        doReturn(studentDTO1).when(studentMapper).toStudentDTO(student1);
        doReturn(studentDTO2).when(studentMapper).toStudentDTO(student2);

        ResponseEntity<List<StudentDTO>> responseEntity = studentController.getStudents();

        assertEquals(ResponseEntity.ok(studentDTOS), responseEntity);
    }
    @Test
    public void testGetStudents_NotFoundAnyStudents(){
        doReturn(Collections.emptyList()).when(studentService).findAll();

        ResponseEntity<List<StudentDTO>> responseEntity = studentController.getStudents();

        assertEquals(ResponseEntity.notFound().build(), responseEntity);
    }

    @Test
    public void testGetStudent_FoundStudent(){
        Student student1 = mock(Student.class);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        doReturn(student1).when(studentService).findById(ID);
        doReturn(studentDTO1).when(studentMapper).toStudentDTO(student1);

        ResponseEntity<StudentDTO> responseEntity = studentController.getStudent(ID);

        assertEquals(ResponseEntity.ok(studentDTO1), responseEntity);
        verify(studentMapper, times(1)).toStudentDTO(student1);
        verify(studentService, times(1)).findById(ID);
    }
    @Test
    public void testGetStudent_NotFoundStudent(){
        doReturn(null).when(studentService).findById(ID);
        doReturn(null).when(studentMapper).toStudentDTO(null);

        ResponseEntity<StudentDTO> responseEntity = studentController.getStudent(ID);

        assertEquals(ResponseEntity.notFound().build(), responseEntity);
        verify(studentMapper, times(1)).toStudentDTO(null);
        verify(studentService, times(1)).findById(ID);
    }
    @Test
    public void testCreate_Success(){
        Student student1 = mock(Student.class);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(student1).when(studentMapper).toStudent(studentDTO1);

        ResponseEntity<HttpStatus> responseEntity = studentController.create(studentDTO1, bindingResult);

        assertEquals(ResponseEntity.ok(HttpStatus.CREATED),responseEntity );
        verify(studentMapper, times(1)).toStudent(studentDTO1);
        verify(studentService, times(1)).save(student1);
    }
    @Test
    public void testCreate_BindingResultHasErrors(){
        Student student1 = mock(Student.class);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(true).when(bindingResult).hasErrors();
        FieldError fieldError = mock(FieldError.class);
        doReturn(List.of(fieldError)).when(bindingResult).getFieldErrors();
        doReturn("field1").when(fieldError).getField();
        doReturn("defaultField1Message").when(fieldError).getDefaultMessage();

        Exception exception = assertThrows(NotCreatedException.class, () ->{
            studentController.create(studentDTO1, bindingResult);
        });

        assertEquals("field1 - defaultField1Message;: the student wasn't created",exception.getMessage());
        verify(studentMapper, times(0)).toStudent(studentDTO1);
        verify(studentService, times(0)).update(ID, student1);
    }
    @Test
    public void testUpdate_Success(){
        Student student1 = mock(Student.class);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(student1).when(studentMapper).toStudent(studentDTO1);
        doReturn(true).when(studentService).update(ID, student1);

        ResponseEntity<HttpStatus> responseEntity = studentController.update(studentDTO1, bindingResult, ID);

        assertEquals(responseEntity, new ResponseEntity<>(HttpStatus.OK));
        verify(studentMapper, times(1)).toStudent(studentDTO1);
        verify(studentService, times(1)).update(ID,student1);
    }
    @Test
    public void testUpdate_NotSuccess(){
        Student student1 = mock(Student.class);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(student1).when(studentMapper).toStudent(studentDTO1);
        doReturn(false).when(studentService).update(ID, student1);

        ResponseEntity<HttpStatus> responseEntity = studentController.update(studentDTO1, bindingResult, ID);

        assertEquals(responseEntity, new ResponseEntity<>(HttpStatus.NOT_MODIFIED));
        verify(studentMapper, times(1)).toStudent(studentDTO1);
        verify(studentService, times(1)).update(ID,student1);
    }
    @Test
    public void testUpdate_BindingResultHasErrors(){
        Student student1 = mock(Student.class);
        StudentDTO studentDTO1 = mock(StudentDTO.class);
        BindingResult bindingResult = mock(BindingResult.class);
        doReturn(true).when(bindingResult).hasErrors();
        FieldError fieldError = mock(FieldError.class);
        doReturn(List.of(fieldError)).when(bindingResult).getFieldErrors();
        doReturn("field1").when(fieldError).getField();
        doReturn("defaultField1Message").when(fieldError).getDefaultMessage();

        Exception exception = assertThrows(NotUpdatedException.class, () ->{
            studentController.update(studentDTO1, bindingResult, ID);
        });

        assertEquals("field1 - defaultField1Message;: the student wasn't updated",exception.getMessage() );
        verify(studentMapper, times(0)).toStudent(studentDTO1);
        verify(studentService, times(0)).save(student1);
    }
    @Test
    public void testDelete_Success(){
        doReturn(true).when(studentService).deleteById(ID);

        ResponseEntity<?> responseEntity = studentController.deleteStudent(ID);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        verify(studentService, times(1)).deleteById(ID);
    }
    @Test
    public void testDelete_StudentNotDeleted(){
        doReturn(false).when(studentService).deleteById(ID);

        ResponseEntity<?> responseEntity = studentController.deleteStudent(ID);

        assertEquals(HttpStatus.NOT_MODIFIED,responseEntity.getStatusCode());
        verify(studentService, times(1)).deleteById(ID);
    }
    @Test
    public void testGetTasksFromVerificationWork_TasksFound(){
        List<TaskDTO> taskDTOS = List.of(mock(TaskDTO.class), mock(TaskDTO.class));
        List<Task> tasks = List.of(mock(Task.class), mock(Task.class));
        doReturn(tasks).when(studentService).findTasksFromVerificationWork(ID,ID);
        doReturn(taskDTOS).when(taskListMapper).taskListToTaskDTOList(tasks);

        ResponseEntity<?> responseEntity = studentController.getTasksFromVerificationWork(ID,ID);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        verify(studentService, times(1)).findTasksFromVerificationWork(ID,ID);
        verify(taskListMapper, times(1)).taskListToTaskDTOList(tasks);
    }
    @Test
    public void testGetTasksFromVerificationWork_TasksNotFound(){
        doReturn(Collections.emptyList()).when(studentService).findTasksFromVerificationWork(ID,ID);
        doReturn(Collections.emptyList()).when(taskListMapper).taskListToTaskDTOList(Collections.emptyList());

        ResponseEntity<?> responseEntity = studentController.getTasksFromVerificationWork(ID,ID);

        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
        verify(studentService, times(1)).findTasksFromVerificationWork(ID,ID);
        verify(taskListMapper, times(1)).taskListToTaskDTOList(Collections.emptyList());
    }
    @Test
    public void testGetSolutionStatus_SolutionsAccepted(){
        List<TaskDTO> answers = List.of(mock(TaskDTO.class), mock(TaskDTO.class));
        Map<Long, Boolean> solutions = new HashMap<>();
        solutions.put(1L, true);
        solutions.put(2L, true);
        doReturn(solutions).when(studentService).checkAnswers(ID,answers);

        ResponseEntity<?> responseEntity = studentController.getSolutionStatus(ID,ID,answers);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(solutions,responseEntity.getBody());
        verify(studentService, times(1)).checkAnswers(ID,answers);
     }
    @Test
    public void testGetSolutionStatus_SolutionsNotAccepted(){
        List<TaskDTO> answers = List.of(mock(TaskDTO.class), mock(TaskDTO.class));
        doReturn(Collections.emptyMap()).when(studentService).checkAnswers(ID,answers);

        ResponseEntity<?> responseEntity = studentController.getSolutionStatus(ID,ID,answers);

        assertEquals(HttpStatus.NOT_ACCEPTABLE,responseEntity.getStatusCode());
        verify(studentService, times(1)).checkAnswers(ID,answers);
    }



}
