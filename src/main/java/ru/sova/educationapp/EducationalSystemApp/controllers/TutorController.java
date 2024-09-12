package ru.sova.educationapp.EducationalSystemApp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.services.TutorService;

@Controller
@RequestMapping("/tutors")
public class TutorController {

    private final TutorService tutorService;
    private final StudentService studentService;

    @Autowired
    public TutorController(TutorService tutorService, StudentService studentService) {
        this.tutorService = tutorService;
        this.studentService = studentService;
    }

    @GetMapping
    public String getTutors(Model model, HttpServletRequest request) {
        model.addAttribute("tutors", tutorService.finAll());
        return "tutors/show";
    }
    @GetMapping("/{id}")
    public String getTutorById(@PathVariable("id") int id, Model model,
                              @ModelAttribute("student") Student student){
        model.addAttribute("tutor", tutorService.findById(id));
        model.addAttribute("students", tutorService.findById(id).getStudents());
        model.addAttribute("validStudents", studentService.finAll());


        return "tutors/index";
    }
    @GetMapping("/new")
    public String newBook(@ModelAttribute("tutor") Tutor tutor){
        return "tutors/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("tutor") @Valid Tutor tutor, BindingResult bindingResult){
//        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()){
            return "tutors/new";
        }
        tutorService.save(tutor);
        return "redirect:/tutors";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("tutor", tutorService.findById(id));
        return "tutors/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("tutor") @Valid Tutor tutor, BindingResult bindingResult, @PathVariable("id") int id){
//        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()){
            return "tutors/edit";
        }
        tutorService.update(id, tutor);
        return "redirect:/tutors";
    }
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id){
        tutorService.deleteById(id);
        return "redirect:/tutors";
    }
    @PatchMapping("/{id}/choose")
    public String choose(@PathVariable("id") int id, @ModelAttribute("tutor") Tutor tutor,
                         @ModelAttribute("student") Student student){
        tutorService.addPupil(studentService.findById(student.getId()), tutorService.findById(id));
        return "redirect:/tutors/" + id;
    }
    @PatchMapping("/{id}/exclude")
    public String excludeStudent(@PathVariable("id") int id, @ModelAttribute("student") Student student){
        tutorService.excludeStudent(studentService.findById(student.getId()), tutorService.findById(id));
        return "redirect:/tutors/" + id;
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

