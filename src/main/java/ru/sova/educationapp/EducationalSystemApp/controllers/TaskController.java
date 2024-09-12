package ru.sova.educationapp.EducationalSystemApp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.services.TaskService;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String getTasks(Model model) {
        model.addAttribute("tasks", taskService.finAll());
        return "tasks/show";
    }
    @GetMapping("/{id}")
    public String getTask(@PathVariable("id") int id, Model model) {
        model.addAttribute("task", taskService.findById(id));
        return "tasks/index";
    }
    @GetMapping("/new")
    public String newTask(@ModelAttribute("task") Task task){
        return "tasks/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("task") @Valid Task task, BindingResult bindingResult){
//        personValidator.validate(person, bindingResult);//todo

        if (bindingResult.hasErrors()){
            return "tasks/new";
        }
        taskService.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("task", taskService.findById(id));
        return "tasks/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("task") @Valid Task task,
                         BindingResult bindingResult, @PathVariable("id") int id){
//        personValidator.validate(person, bindingResult);//todo

        if (bindingResult.hasErrors()){
            return "tasks/edit";
        }
        taskService.update(id, task);
        return "redirect:/tasks";
    }
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable("id") int id){
        taskService.deleteById(id);
        return "redirect:/tasks";
    }
}