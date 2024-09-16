package ru.sova.educationapp.EducationalSystemApp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.DTO.StudentDTO;
import ru.sova.educationapp.EducationalSystemApp.mappers.StudentMapper;
import ru.sova.educationapp.EducationalSystemApp.mappers.TutorMapper;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;

import java.util.stream.Collectors;


@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;
    private final TutorMapper tutorMapper;

    @Autowired
    public StudentController(StudentService studentService, StudentMapper studentMapper, TutorMapper tutorMapper) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
        this.tutorMapper = tutorMapper;
    }

    @GetMapping
    public String getStudents(Model model) {
        model.addAttribute("students", studentService.finAll().stream().map(studentMapper::toStudentDTO));
        return "students/show";
    }
    @GetMapping("/{id}")
    public String getStudent(@PathVariable("id") int id, Model model) {
        model.addAttribute("student", studentMapper.toStudentDTO(studentService.findById(id)));
        model.addAttribute("tutors",
                studentService.findById(id).getTutors()
                        .stream().map(tutorMapper::toTutorDTO).collect(Collectors.toList()));
        return "students/index";
    }
    @GetMapping("/new")
    public String newStudent(@ModelAttribute("student") StudentDTO studentDTOt){
        return "students/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("student") @Valid StudentDTO studentDTO, BindingResult bindingResult){
//        personValidator.validate(person, bindingResult);//todo

        if (bindingResult.hasErrors()){
            return "students/new";
        }
        studentService.save(studentMapper.toStudent(studentDTO));
        return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("student", studentMapper.toStudentDTO(studentService.findById(id)));
        return "students/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("student") @Valid StudentDTO studentDTO,
                         BindingResult bindingResult, @PathVariable("id") int id){
//        personValidator.validate(person, bindingResult);//todo

        if (bindingResult.hasErrors()){
            return "students/edit";
        }
        studentService.update(id, studentMapper.toStudent(studentDTO));
        return "redirect:/students";
    }
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable("id") int id){
        studentService.deleteById(id);
        return "redirect:/students";
    }
}
