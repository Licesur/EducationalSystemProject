package ru.sova.educationapp.EducationalSystemApp.restControllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.DTO.StudentDTO;
import ru.sova.educationapp.EducationalSystemApp.DTO.TaskDTO;
import ru.sova.educationapp.EducationalSystemApp.DTO.TutorDTO;
import ru.sova.educationapp.EducationalSystemApp.DTO.VerificationWorkDTO;
import ru.sova.educationapp.EducationalSystemApp.mappers.StudentMapper;
import ru.sova.educationapp.EducationalSystemApp.mappers.VerificationWorkMapper;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.services.TaskService;
import ru.sova.educationapp.EducationalSystemApp.services.VerificationWorkService;
import ru.sova.educationapp.EducationalSystemApp.udtil.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/works")
public class RestVerificationWorkController {

    private final VerificationWorkService verificationWorkService;
    private final TaskService taskService;
    private final StudentService studentService;
    private final VerificationWorkMapper verificationWorkMapper;
    private final StudentMapper studentMapper;

    @Autowired
    public RestVerificationWorkController(VerificationWorkService verificationWorkService,
                                          TaskService taskService, StudentService studentService,
                                          VerificationWorkMapper verificationWorkMapper,
                                          StudentMapper studentMapper) {
        this.verificationWorkService = verificationWorkService;
        this.taskService = taskService;
        this.studentService = studentService;
        this.verificationWorkMapper = verificationWorkMapper;
        this.studentMapper = studentMapper;
    }

    @GetMapping
    public ResponseEntity<List<VerificationWorkDTO>> getVerificationWorks() {
        final List<VerificationWorkDTO> verificationWorks = verificationWorkService.finAll()
                .stream().map(verificationWorkMapper::toVerificationWorkDTO).toList();
        return !verificationWorks.isEmpty()
                ? new ResponseEntity<>(verificationWorks, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);    }

    @GetMapping("/{id}")
    public ResponseEntity<VerificationWorkDTO> getVerificationWork(@PathVariable("id") int id) {
        final VerificationWorkDTO verificationWorkDTO = verificationWorkMapper
                .toVerificationWorkDTO(verificationWorkService.findById(id));
        return verificationWorkDTO != null
                ? new ResponseEntity<>(verificationWorkDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid VerificationWorkDTO verificationWorkDTO,
                         BindingResult bindingResult){
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()){
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new VerificationWorkNotCreateException(errorMesssage.toString());
        }
        verificationWorkService.save(verificationWorkMapper.toVerificationWork(verificationWorkDTO));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid VerificationWorkDTO workToUpdateDTO,
                         BindingResult bindingResult, @PathVariable("id") int id) {
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()){
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new VerificationWorkNotUpdatedException(errorMesssage.toString());
        }
        final boolean updated = verificationWorkService.update(id, verificationWorkMapper
                .toVerificationWork(workToUpdateDTO));
        return updated ? new ResponseEntity<>(HttpStatus.OK): new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVerificationWork(@PathVariable("id") int id){
        Boolean deleted = verificationWorkService.deleteById(id);
        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PatchMapping("/{id}/choose")
    public ResponseEntity<HttpStatus> choose(@PathVariable("id") int id,
                         @RequestBody @Valid StudentDTO studentDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new VerificationWorkNotAssingedException(errorMesssage.toString());
        }
        Boolean added = studentService.addVerificationWork(verificationWorkService.findById(id),
                studentMapper.toStudent(studentDTO));
        return added ? new ResponseEntity<>(HttpStatus.OK): new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}