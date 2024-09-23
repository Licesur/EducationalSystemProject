package ru.sova.educationapp.EducationalSystemApp.controllers.mvc;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.DTO.StudentDTO;
import ru.sova.educationapp.EducationalSystemApp.DTO.VerificationWorkDTO;
import ru.sova.educationapp.EducationalSystemApp.mappers.StudentMapper;
import ru.sova.educationapp.EducationalSystemApp.mappers.TaskListMapper;
import ru.sova.educationapp.EducationalSystemApp.mappers.TaskMapper;
import ru.sova.educationapp.EducationalSystemApp.mappers.VerificationWorkMapper;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.services.TaskService;
import ru.sova.educationapp.EducationalSystemApp.services.VerificationWorkService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/works")
public class VerificationWorkController {

    private final VerificationWorkService verificationWorkService;
    private final TaskService taskService;
    private final StudentService studentService;
    private final VerificationWorkMapper verificationWorkMapper;
    private final TaskMapper taskMapper;
    private final StudentMapper studentMapper;
    private final TaskListMapper taskListMapper;

    @Autowired
    public VerificationWorkController(VerificationWorkService verificationWorkService,
                                      TaskService taskService, StudentService studentService,
                                      VerificationWorkMapper verificationWorkMapper,
                                      TaskMapper taskMapper, StudentMapper studentMapper,
                                      TaskListMapper taskListMapper) {
        this.verificationWorkService = verificationWorkService;
        this.taskService = taskService;
        this.studentService = studentService;
        this.verificationWorkMapper = verificationWorkMapper;
        this.taskMapper = taskMapper;
        this.studentMapper = studentMapper;
        this.taskListMapper = taskListMapper;
    }

    @GetMapping
    public String getVerificationWorks(Model model) {
        model.addAttribute("works", verificationWorkService.findAll().stream()
                .map(verificationWorkMapper::toVerificationWorkDTO));
        return "works/show";
    }

    @GetMapping("/{id}")
    public String getVerificationWork(@PathVariable("id") long id, Model model,
                                      @ModelAttribute("student") StudentDTO studentDTO) {
        model.addAttribute("tasks",
                taskService.findByVerificationWork(verificationWorkService.findById(id)).stream()
                        .map(taskMapper::toTaskDTO).collect(Collectors.toList()));
        model.addAttribute("work", verificationWorkMapper
                .toVerificationWorkDTO(verificationWorkService.findById(id)));
        model.addAttribute("students",
                studentService.findByVerificationWork(verificationWorkService.findById(id)).stream()
                        .map(studentMapper::toStudentDTO).collect(Collectors.toList()));
        model.addAttribute("validStudents",
                studentService.findByVerificationWorkNotContains(verificationWorkService.findById(id))
                        .stream().map(studentMapper::toStudentDTO).collect(Collectors.toList()));
        return "works/index";
    }

    @GetMapping("/new")
    public String newVerificationWork(@ModelAttribute("work") VerificationWorkDTO verificationWorkDTO,
                                      Model model) {
        model.addAttribute("tasksDTO", taskListMapper.taskListToTaskDTOList(taskService.findAll()));
//        taskService.finAll().stream()
//                .map(taskMapper::toTaskDTO).forEach(s -> System.out.println(s.getId()));
        return "works/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("work") @Valid VerificationWorkDTO verificationWorkDTO,
                         BindingResult bindingResult,
                         @RequestParam(name = "selectedTasks") List<String> selectedTasksDtoId) {
//        personValidator.validate(person, bindingResult);//todo

        if (bindingResult.hasErrors()) {
            return "works/new";
        }
        verificationWorkService.fillTasks(verificationWorkMapper.toVerificationWork(verificationWorkDTO),
                selectedTasksDtoId.stream().map(Integer::parseInt).map(taskService::findById).toList());
        return "redirect:/works";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        model.addAttribute("work", verificationWorkMapper
                .toVerificationWorkDTO(verificationWorkService.findById(id)));
        model.addAttribute("tasks", taskService.findAll().stream()
                .map(taskMapper::toTaskDTO).collect(Collectors.toList()));
        return "works/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("work") @Valid VerificationWorkDTO workToUpdateDTO,
                         BindingResult bindingResult, @PathVariable("id") long id,
                         @RequestParam(name = "selectedTasks") List<String> selectedTasksDtoId) {
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()) {
            return "works/edit";
        }
        //costile as fuck, but it works
        workToUpdateDTO.setTasks(selectedTasksDtoId.stream().map(Integer::parseInt)
                .map(taskService::findById).map(taskMapper::toTaskDTO).toList());
        verificationWorkService.update(id, verificationWorkMapper.toVerificationWork(workToUpdateDTO));

        return "redirect:/works";
    }

    @DeleteMapping("/{id}")
    public String deleteVerificationWork(@PathVariable("id") long id) {
        verificationWorkService.deleteById(id);
        return "redirect:/works";
    }

    @PatchMapping("/{id}/choose")
    public String choose(@PathVariable("id") long id,
                         @ModelAttribute("work") VerificationWorkDTO verificationWorkDTO,
                         @ModelAttribute("student") StudentDTO studentDTO) {
        studentService.addVerificationWork(verificationWorkService.findById(id),
                studentMapper.toStudent(studentDTO));
        return "redirect:/works/" + id;
    }
}