package ru.sova.educationapp.EducationalSystemApp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;


@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String getStudents(Model model) {
        model.addAttribute("students", studentService.finAll());
        return "students/show";
    }
    @GetMapping("/{id}")
    public String getStudent(@PathVariable("id") int id, Model model) {
        model.addAttribute("student", studentService.findById(id));
        model.addAttribute("tutors", studentService.findById(id).getTutors());
        return "students/index";
    }
    @GetMapping("/new")
    public String newStudent(@ModelAttribute("student") Student student){
        return "students/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("student") @Valid Student student, BindingResult bindingResult){
//        personValidator.validate(person, bindingResult);//todo

        if (bindingResult.hasErrors()){
            return "students/new";
        }
        studentService.save(student);
        return "redirect:/students";
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
