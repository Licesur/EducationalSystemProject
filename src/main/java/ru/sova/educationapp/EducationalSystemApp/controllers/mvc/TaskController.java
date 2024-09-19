package ru.sova.educationapp.EducationalSystemApp.controllers.mvc;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.DTO.TaskDTO;
import ru.sova.educationapp.EducationalSystemApp.mappers.TaskMapper;
import ru.sova.educationapp.EducationalSystemApp.services.TaskService;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping
    public String getTasks(Model model) {
        model.addAttribute("tasks", taskService.finAll().stream().map(taskMapper::toTaskDTO));
        return "tasks/show";
    }
    @GetMapping("/{id}")
    public String getTask(@PathVariable("id") int id, Model model) {
        model.addAttribute("task", taskMapper.toTaskDTO(taskService.findById(id)));
        return "tasks/index";
    }
    @GetMapping("/new")
    public String newTask(@ModelAttribute("task") TaskDTO taskDTO){
        return "tasks/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("task") @Valid TaskDTO taskDTO, BindingResult bindingResult){
//        personValidator.validate(person, bindingResult);//todo

        if (bindingResult.hasErrors()){
            return "tasks/new";
        }
        taskService.save(taskMapper.toTask(taskDTO));
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("task", taskMapper.toTaskDTO(taskService.findById(id)));
        return "tasks/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("task") @Valid TaskDTO taskDTO,
                         BindingResult bindingResult, @PathVariable("id") int id){
//        personValidator.validate(person, bindingResult);//todo

        if (bindingResult.hasErrors()){
            return "tasks/edit";
        }
        taskService.update(id, taskMapper.toTask(taskDTO));
        return "redirect:/tasks";
    }
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable("id") int id){
        taskService.deleteById(id);
        return "redirect:/tasks";
    }
}