package ru.sova.educationapp.EducationalSystemApp.restControllers;

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
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.services.TutorService;
import ru.sova.educationapp.EducationalSystemApp.udtil.StudentNotAssingedException;
import ru.sova.educationapp.EducationalSystemApp.udtil.StudentNotExcludedException;
import ru.sova.educationapp.EducationalSystemApp.udtil.TutorNotCreatedException;
import ru.sova.educationapp.EducationalSystemApp.udtil.TutorNotUpdatedException;

import java.util.List;

@RestController
@RequestMapping("/rest/tutors")
public class RestTutorController {

    private final TutorService tutorService;
    private final StudentService studentService;
    private final TutorMapper tutorMapper;

    @Autowired
    public RestTutorController(TutorService tutorService, StudentService studentService, TutorMapper tutorMapper) {
        this.tutorService = tutorService;
        this.studentService = studentService;
        this.tutorMapper = tutorMapper;
    }

    @GetMapping
    public ResponseEntity<List<TutorDTO>> getTutors() {
        final List<TutorDTO> tutors = tutorService.finAll()
                .stream().map(tutorMapper::toTutorDTO).toList();
        return !tutors.isEmpty()
                ? new ResponseEntity<>(tutors, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TutorDTO> getTutorById(@PathVariable("id") int id){
        final TutorDTO tutorDTO = tutorMapper.toTutorDTO(tutorService.findById(id));
        return tutorDTO != null
                ? new ResponseEntity<>(tutorDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid TutorDTO tutorDTO,
                                             BindingResult bindingResult){
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()){
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new TutorNotCreatedException(errorMesssage.toString());
        }
        tutorService.save(tutorMapper.toTutor(tutorDTO));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid TutorDTO tutorDTO,
                                             BindingResult bindingResult, @PathVariable("id") int id){
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()){
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new TutorNotUpdatedException(errorMesssage.toString());
        }
        final boolean updated = tutorService.update(id, tutorMapper.toTutor(tutorDTO));
        return updated ? new ResponseEntity<>(HttpStatus.OK): new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTutor(@PathVariable("id") int id){
        Boolean deleted = tutorService.deleteById(id);
        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PatchMapping("/{id}/choose")
    public ResponseEntity<HttpStatus> choose(@PathVariable("id") int id,
                         @RequestBody StudentDTO studentDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new StudentNotAssingedException(errorMesssage.toString());
        }
        Boolean added = tutorService.addPupil(studentService.findById(studentDTO.getId()), tutorService.findById(id));
        return added ? new ResponseEntity<>(HttpStatus.OK): new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    @PatchMapping("/{id}/exclude")
    public ResponseEntity<HttpStatus> excludeStudent(@PathVariable("id") int id,
                                                     @RequestBody StudentDTO studentDTO,
                                                     BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new StudentNotExcludedException(errorMesssage.toString());
        }
        Boolean excluded = tutorService.excludeStudent(studentService.findById(studentDTO.getId()), tutorService.findById(id));
        return excluded ? new ResponseEntity<>(HttpStatus.OK): new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

    }
//    @PatchMapping("/{id}/release")
//    public String release(@PathVariable("id") int id){
//        bookService.setOwner(id, null);
//        return "redirect:/library/" + id;
//    }
//    @GetMapping("/search")
//    public String search(Model model, @ModelAttribute("book") Book book){
//        return "library/search";
//    }
//    @PostMapping("/search")
//    public String makeSearch(Model model, @RequestParam("title") String title){
//        model.addAttribute("books", bookService.findByTitleStartingWith(title));
//        return "library/search";
//    }


}

