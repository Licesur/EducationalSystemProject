package ru.sova.educationapp.EducationalSystemApp.restControllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.DTO.TaskDTO;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.services.TaskService;
import ru.sova.educationapp.EducationalSystemApp.services.VerificationWorkService;

import java.util.List;

@RestController
@RequestMapping("/rest/works")
public class RestVerificationWorkController {

    private final VerificationWorkService verificationWorkService;
    private final TaskService taskService;
    private final StudentService studentService;

    @Autowired
    public RestVerificationWorkController(VerificationWorkService verificationWorkService, TaskService taskService, StudentService studentService) {
        this.verificationWorkService = verificationWorkService;
        this.taskService = taskService;
        this.studentService = studentService;
    }

    @GetMapping
    public String getVerificationWorks(Model model) {
        model.addAttribute("works", verificationWorkService.finAll());
        return "works/show";
    }
    @GetMapping("/{id}")
    public String getVerificationWork(@PathVariable("id") int id, Model model,
                                      @ModelAttribute("student") Student student) {
        model.addAttribute("tasks",
                taskService.findByVerificationWork(verificationWorkService.findById(id)));
        model.addAttribute("work", verificationWorkService.findById(id));
        model.addAttribute("students",
                studentService.findByVerificationWork(verificationWorkService.findById(id)));
        model.addAttribute("validStudents",
                studentService.findByVerificationWorkNotContains(verificationWorkService.findById(id)));
        return "works/index";
    }
    @GetMapping("/new")
    public String newVerificationWork(@ModelAttribute("work") VerificationWork verificationWork, Model model){
        model.addAttribute("tasks", taskService.finAll());
        return "works/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("work") @Valid VerificationWork verificationWork,
                         BindingResult bindingResult,
                         @RequestParam List<Task> selectedTasksId){
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()){
            return "works/new";
        }
        verificationWorkService.addTasks(verificationWork, selectedTasksId);
        System.out.println(verificationWork.getTasks());
        return "redirect:/works";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("work", verificationWorkService.findById(id));
        model.addAttribute("tasks", taskService.finAll());
        return "works/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("work") @Valid VerificationWork workToUpdate,
                         BindingResult bindingResult, @PathVariable("id") int id,
                         @RequestParam List<Task> selectedTasks){
//        personValidator.validate(person, bindingResult);//todo

        if (bindingResult.hasErrors()){
            return "works/edit";
        }
        verificationWorkService.update(id, workToUpdate, selectedTasks);
        return "redirect:/works";
    }
    @DeleteMapping("/{id}")
    public String deleteVerificationWork(@PathVariable("id") int id){
        verificationWorkService.deleteById(id);
        return "redirect:/works";
    }
    @PatchMapping("/{id}/choose")
    public String choose(@PathVariable("id") int id, @ModelAttribute("work") VerificationWork verificationWork,
                         @ModelAttribute("student") Student student){
        studentService.addVerificationWork(verificationWorkService.findById(id), student);
        return "redirect:/works/" + id;
    }
}