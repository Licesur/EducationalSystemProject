package ru.sova.educationapp.EducationalSystemApp.restControllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.DTO.StudentDTO;
import ru.sova.educationapp.EducationalSystemApp.mappers.StudentMapper;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.udtil.StudentNotCreatedException;
import ru.sova.educationapp.EducationalSystemApp.udtil.StudentNotUpdatedException;

import java.util.List;


@RestController
@RequestMapping("/rest/students")
public class RestStudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;

    @Autowired
    public RestStudentController(StudentService studentService, StudentMapper studentMapper) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getStudents() {
        final List<StudentDTO> students = studentService.finAll()
                .stream().map(studentMapper::toStudentDTO).toList();
        return !students.isEmpty()
                ? new ResponseEntity<>(students, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable("id") int id) {
        final StudentDTO student = studentMapper.toStudentDTO(studentService.findById(id));
        return student != null
                ? new ResponseEntity<>(student, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid StudentDTO studentDTO,
                                             BindingResult bindingResult){
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()){
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new StudentNotCreatedException(errorMesssage.toString());
        }
        studentService.save(studentMapper.toStudent(studentDTO));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid StudentDTO studentDTO,
                         BindingResult bindingResult, @PathVariable("id") int id){
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()){
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new StudentNotUpdatedException(errorMesssage.toString());
        }
        final boolean updated = studentService.update(id, studentMapper.toStudent(studentDTO));
        return updated ? new ResponseEntity<>(HttpStatus.OK): new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") int id){
        Boolean deleted = studentService.deleteById(id);
        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
