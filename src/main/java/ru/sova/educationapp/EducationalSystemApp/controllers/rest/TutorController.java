package ru.sova.educationapp.EducationalSystemApp.controllers.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.DTO.StudentDTO;
import ru.sova.educationapp.EducationalSystemApp.DTO.TutorDTO;
import ru.sova.educationapp.EducationalSystemApp.mappers.TutorMapper;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.services.TutorService;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotAssignedException;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotCreatedException;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotExcludedException;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotUpdatedException;

import java.util.List;

@RestController
@RequestMapping("/rest/tutors")
public class TutorController {

    private final TutorService tutorService;
    private final StudentService studentService;
    private final TutorMapper tutorMapper;

    @Autowired
    public TutorController(TutorService tutorService,
                           StudentService studentService,
                           TutorMapper tutorMapper) {
        this.tutorService = tutorService;
        this.studentService = studentService;
        this.tutorMapper = tutorMapper;
    }

    @GetMapping
    public ResponseEntity<List<TutorDTO>> getTutors() {
        final List<TutorDTO> tutors = tutorService.findAll()
                .stream().map(tutorMapper::toTutorDTO).toList();
        return !tutors.isEmpty()
                ? new ResponseEntity<>(tutors, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TutorDTO> getTutorById(@PathVariable("id") long id) {
        final TutorDTO tutorDTO = tutorMapper.toTutorDTO(tutorService.findById(id));
        return tutorDTO != null
                ? new ResponseEntity<>(tutorDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid TutorDTO tutorDTO,
                                             BindingResult bindingResult) {
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotCreatedException(errorMesssage + ": the tutor wasn't created");
        }
        tutorService.save(tutorMapper.toTutor(tutorDTO));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid TutorDTO tutorDTO,
                                             BindingResult bindingResult, @PathVariable("id") long id) {
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotUpdatedException(errorMesssage + ": the tutor wasn't updated");
        }
        final boolean updated = tutorService.update(id, tutorMapper.toTutor(tutorDTO));
        return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTutor(@PathVariable("id") long id) {
        Boolean deleted = tutorService.deleteById(id);
        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PatchMapping("/{id}/choose")
    public ResponseEntity<HttpStatus> assignStudent(@PathVariable("id") long id,
                                                    @RequestBody StudentDTO studentDTO,
                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotAssignedException(errorMesssage+ ": the student wasn't assigned");
        }
        Boolean added = tutorService.addStudent(studentService.findById(studentDTO.getId()),
                tutorService.findById(id));
        return added ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PatchMapping("/{id}/exclude")
    public ResponseEntity<HttpStatus> excludeStudent(@PathVariable("id") long id,
                                                     @RequestBody StudentDTO studentDTO,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotExcludedException(errorMesssage + ": the student wasn't excluded");
        }
        Boolean excluded = tutorService.excludeStudent(studentService.findById(studentDTO.getId()),
                tutorService.findById(id));
        return excluded ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}

