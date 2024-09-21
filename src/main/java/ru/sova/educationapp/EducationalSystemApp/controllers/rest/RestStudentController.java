package ru.sova.educationapp.EducationalSystemApp.controllers.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.DTO.StudentDTO;
import ru.sova.educationapp.EducationalSystemApp.DTO.TaskDTO;
import ru.sova.educationapp.EducationalSystemApp.mappers.StudentMapper;
import ru.sova.educationapp.EducationalSystemApp.mappers.TaskListMapper;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotCreatedException;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotUpdatedException;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/rest/students")
public class RestStudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;
    private final TaskListMapper taskListMapper;

    @Autowired
    public RestStudentController(StudentService studentService,
                                 StudentMapper studentMapper,
                                 TaskListMapper taskListMapper) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
        this.taskListMapper = taskListMapper;
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getStudents() {
        final List<StudentDTO> students = studentService.findAll()
                .stream().map(studentMapper::toStudentDTO).toList();
        return !students.isEmpty()
                ? new ResponseEntity<>(students, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable("id") long id) {
        final StudentDTO student = studentMapper.toStudentDTO(studentService.findById(id));
        return student != null
                ? new ResponseEntity<>(student, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid
                                             @Parameter(description = "student object you want to create",
                                                     required = true) StudentDTO studentDTO,
                                             BindingResult bindingResult) {
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotCreatedException(errorMesssage + ": the student wasn't created");
        }
        studentService.save(studentMapper.toStudent(studentDTO));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid StudentDTO studentDTO,
                                             BindingResult bindingResult, @PathVariable("id") long id) {
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotUpdatedException(errorMesssage + ": the student wasn't updated");
        }
        final boolean updated = studentService.update(id, studentMapper.toStudent(studentDTO));
        return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") long id) {
        Boolean deleted = studentService.deleteById(id);
        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/{id}/works/{workId}")
    public ResponseEntity<?> getTasksFromVerificationWork(@PathVariable("id")
                                                          @Schema(description =
                                                                  "Введите айди ученика")
                                                          long id,
                                                          @PathVariable("workId")
                                                          @Schema(description
                                                                  = "Введите айди проверяемой работы")
                                                          long workId) {
        final List<TaskDTO> tasks = taskListMapper
                .taskListToTaskDTOList(studentService.findTasksFromVerificationWork(id, workId));
        return !tasks.isEmpty()
                ? new ResponseEntity<>(tasks, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}/works/{workId}/solution")
    public ResponseEntity<?> getSolutionStatus(@PathVariable("id")
                                               @Schema(description =
                                                       "Введите айди ученика")
                                               long id,
                                               @PathVariable("workId")
                                               @Schema(description = "Введите айди проверяемой работы")
                                               long workId,
                                               @RequestBody @Valid
                                               @Schema(description =
                                                       "Введите ответы в формате списка задач",
                                                       example = "[\n" +
                                                               "    {\n" +
                                                               "        \"id\": 1,\n" +
                                                               "        \"definition\":\"\",\n" +
                                                               "        \"answer\": \"5\"\n" +
                                                               "    },\n" +
                                                               "    {\n" +
                                                               "        \"id\": 2,\n" +
                                                               "        \"definition\":\"\",\n" +
                                                               "        \"answer\": \"1\"\n" +
                                                               "    }\n" +
                                                               "]")
                                               List<TaskDTO> answers) {

        Map<Long, Boolean> solution = studentService.checkAnswers(workId, answers);
        return !solution.isEmpty()
                ? new ResponseEntity<>(solution, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
}

