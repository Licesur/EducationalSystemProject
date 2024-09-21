package ru.sova.educationapp.EducationalSystemApp.controllers.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.DTO.StudentDTO;
import ru.sova.educationapp.EducationalSystemApp.DTO.VerificationWorkDTO;
import ru.sova.educationapp.EducationalSystemApp.mappers.StudentMapper;
import ru.sova.educationapp.EducationalSystemApp.mappers.VerificationWorkMapper;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.services.VerificationWorkService;
import ru.sova.educationapp.EducationalSystemApp.exceptions.*;

import java.util.List;

@RestController
@RequestMapping("/rest/works")
public class RestVerificationWorkController {

    private final VerificationWorkService verificationWorkService;
    private final StudentService studentService;
    private final VerificationWorkMapper verificationWorkMapper;
    private final StudentMapper studentMapper;

    @Autowired
    public RestVerificationWorkController(VerificationWorkService verificationWorkService,
                                          StudentService studentService,
                                          VerificationWorkMapper verificationWorkMapper,
                                          StudentMapper studentMapper) {
        this.verificationWorkService = verificationWorkService;
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
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VerificationWorkDTO> getVerificationWork(@PathVariable("id") long id) {
        final VerificationWorkDTO verificationWorkDTO = verificationWorkMapper
                .toVerificationWorkDTO(verificationWorkService.findById(id));
        return verificationWorkDTO != null
                ? new ResponseEntity<>(verificationWorkDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid VerificationWorkDTO verificationWorkDTO,
                                             BindingResult bindingResult) {
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotCreatedException(errorMesssage + ": work wasn't created");
        }
        verificationWorkService.save(verificationWorkMapper.toVerificationWork(verificationWorkDTO));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid VerificationWorkDTO workToUpdateDTO,
                                             BindingResult bindingResult, @PathVariable("id") long id) {
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotUpdatedException(errorMesssage + ": the chosen work wasn't updated");
        }
        final boolean updated = verificationWorkService.update(id, verificationWorkMapper
                .toVerificationWork(workToUpdateDTO));
        return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVerificationWork(@PathVariable("id") long id) {
        boolean deleted = verificationWorkService.deleteById(id);
        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PatchMapping("/{id}/choose")
    public ResponseEntity<HttpStatus> assignToStudent(@PathVariable("id") long id,
                                                      @RequestBody @Valid StudentDTO studentDTO,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotAssignedException(errorMesssage +
                    ": work wasn't assigned to the chosen student");
        }
        Boolean added = studentService.addVerificationWork(verificationWorkService.findById(id),
                studentMapper.toStudent(studentDTO));
        return added ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}