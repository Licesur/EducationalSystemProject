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
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.udtil.StudentNotCreatedException;

import java.util.List;


@RestController
@RequestMapping("/rest/students")
public class RestStudentController {

    private final StudentService studentService;

    @Autowired
    public RestStudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getStudents() {return studentService.finAll();}

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable("id") int id, Model model) {return studentService.findById(id);}

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid StudentDTO studentDTO, BindingResult bindingResult){
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
        studentService.save(convertDtoToStudent(studentDTO));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    private Student convertDtoToStudent(StudentDTO studentDTO) {
        Student student = new Student();
        student.setFullName(studentDTO.getFullName());
        student.setEmail(studentDTO.getEmail());
        student.setPassword(studentDTO.getPassword());
        student.setAge(studentDTO.getAge());
//        student.setVerificationWorks(studentDTO.getVerificationWorks());
//        student.setTutors(studentDTO.getTutors());
        return student;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("student", studentService.findById(id));
        return "students/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("student") @Valid Student student,
                         BindingResult bindingResult, @PathVariable("id") int id){
//        personValidator.validate(person, bindingResult);//todo

        if (bindingResult.hasErrors()){
            return "students/edit";
        }
        studentService.update(id, student);
        return "redirect:/students";
    }
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable("id") int id){
        studentService.deleteById(id);
        return "redirect:/students";
    }
}
