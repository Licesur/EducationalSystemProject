package ru.sova.educationapp.EducationalSystemApp.controllers.mvc;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.DTO.StudentDTO;
import ru.sova.educationapp.EducationalSystemApp.DTO.TutorDTO;
import ru.sova.educationapp.EducationalSystemApp.mappers.StudentMapper;
import ru.sova.educationapp.EducationalSystemApp.mappers.TutorMapper;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.services.TutorService;

@Controller
@RequestMapping("/tutors")
public class TutorController {

    private final TutorService tutorService;
    private final StudentService studentService;
    private final TutorMapper tutorMapper;
    private final StudentMapper studentMapper;

    @Autowired
    public TutorController(TutorService tutorService,
                           StudentService studentService,
                           TutorMapper tutorMapper,
                           StudentMapper studentMapper) {
        this.tutorService = tutorService;
        this.studentService = studentService;
        this.tutorMapper = tutorMapper;
        this.studentMapper = studentMapper;
    }

    @GetMapping
    public String getTutors(Model model) {
        model.addAttribute("tutors", tutorService.findAll().stream().map(tutorMapper::toTutorDTO));
        return "tutors/show";
    }

    @GetMapping("/{id}")
    public String getTutorById(@PathVariable("id") long id, Model model,
                               @ModelAttribute("student") StudentDTO studentDTO) {
        Tutor tutor = tutorService.findById(id);
        model.addAttribute("tutor", tutorMapper.toTutorDTO(tutor));
        model.addAttribute("studentsOfTheTutor", tutorService.findById(id).getStudents());
        model.addAttribute("validStudents", studentService.findByTutorsNotContains(tutor)
                );
//        .stream().map(studentMapper::toStudentDTO).collect(Collectors.toList())


        return "tutors/index";
    }

    @GetMapping("/new")
    public String newTutor(@ModelAttribute("tutor") TutorDTO tutorDTO) {
        return "tutors/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("tutor") @Valid TutorDTO tutorDTO, BindingResult bindingResult) {
//        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "tutors/new";
        }
        tutorService.save(tutorMapper.toTutor(tutorDTO));
        return "redirect:/tutors";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        model.addAttribute("tutor", tutorMapper.toTutorDTO(tutorService.findById(id)));
        return "tutors/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("tutor") @Valid TutorDTO tutorDTO,
                         BindingResult bindingResult, @PathVariable("id") long id) {
//        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "tutors/edit";
        }
        tutorService.update(id, tutorMapper.toTutor(tutorDTO));
        return "redirect:/tutors";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") long id) {
        tutorService.deleteById(id);
        return "redirect:/tutors";
    }

    @PatchMapping("/{id}/choose")
    public String choose(@PathVariable("id") long id, @ModelAttribute("tutor") Tutor tutor,
                         @ModelAttribute("student") StudentDTO studentDTO) {
        tutorService.addStudent(studentService.findById(studentMapper.toStudent(studentDTO).getId()),
                tutorService.findById(id));
        return "redirect:/tutors/" + id;
    }

    @PatchMapping("/{id}/exclude")
    public String excludeStudent(@PathVariable("id") long id, @ModelAttribute("student") StudentDTO studentDTO) {
        tutorService.excludeStudent(studentService.findById(studentMapper.toStudent(studentDTO).getId()),
                tutorService.findById(id));
        return "redirect:/tutors/" + id;
    }
}

