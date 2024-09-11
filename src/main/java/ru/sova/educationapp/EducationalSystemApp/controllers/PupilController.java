package ru.sova.educationapp.EducationalSystemApp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.models.Pupil;
import ru.sova.educationapp.EducationalSystemApp.services.PupilService;


@Controller
@RequestMapping("/pupils")
public class PupilController {

    private final PupilService pupilService;

    @Autowired
    public PupilController(PupilService pupilService) {
        this.pupilService = pupilService;
    }

    @GetMapping
    public String getPeople(Model model) {
        model.addAttribute("people", pupilService.finAll());
        return "pupils/show";
    }
    @GetMapping("/{id}")
    public String getPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("pupil", pupilService.findById(id));
//        model.addAttribute("tutors", pupilService.getBooksByPersonId(id));
        return "pupils/index";
    }
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Pupil pupil){
        return "pupil/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("pupil") @Valid Pupil person, BindingResult bindingResult){
//        personValidator.validate(person, bindingResult);//todo

        if (bindingResult.hasErrors()){
            return "pupils/new";
        }
        pupilService.save(person);
        return "redirect:/pupils";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("person", pupilService.findById(id));
        return "pupils/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("pupil") @Valid Pupil pupil,
                         BindingResult bindingResult, @PathVariable("id") int id){
//        personValidator.validate(person, bindingResult);//todo

        if (bindingResult.hasErrors()){
            return "pupils/edit";
        }
        pupilService.update(id, pupil);
        return "redirect:/pupils";
    }
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id){
        pupilService.deleteById(id);
        return "redirect:/pupils";
    }
}
