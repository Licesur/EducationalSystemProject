package ru.sova.educationapp.EducationalSystemApp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.services.TutorService;

@Controller
@RequestMapping("/tutors")
public class TutorController {

    private final TutorService tutorService;

    @Autowired
    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @GetMapping
    public String getLibrary(Model model, HttpServletRequest request) {
        model.addAttribute("library", tutorService.finAll());
        return "tutors/show";
    }
    @GetMapping("/{id}")
    public String getTutorById(@PathVariable("id") int id, Model model,
                              @ModelAttribute("tutor") Tutor tutor){
        model.addAttribute("tutor", tutorService.findById(id));
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
//    @PatchMapping("/{id}/choose")
//    public String choose(@PathVariable("id") int id, @ModelAttribute("person") Person person){
//        tutorService.setOwner(id, person);
//        return "redirect:/library/" + id;
//    }
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

