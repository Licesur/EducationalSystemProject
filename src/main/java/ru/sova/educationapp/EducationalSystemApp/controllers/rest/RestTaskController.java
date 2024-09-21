package ru.sova.educationapp.EducationalSystemApp.controllers.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.DTO.TaskDTO;
import ru.sova.educationapp.EducationalSystemApp.mappers.TaskMapper;
import ru.sova.educationapp.EducationalSystemApp.services.TaskService;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotCreatedException;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotUpdatedException;

import java.util.List;

@RestController
@RequestMapping("/rest/tasks")
public class RestTaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @Autowired
    public RestTaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasks() {
        final List<TaskDTO> tasks = taskService.finAll()
                .stream().map(taskMapper::toTaskDTO).toList();
        return !tasks.isEmpty()
                ? new ResponseEntity<>(tasks, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable("id") long id) {
        final TaskDTO taskDTO = taskMapper.toTaskDTO(taskService.findById(id));
        return taskDTO != null
                ? new ResponseEntity<>(taskDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid TaskDTO taskDTO,
                                             BindingResult bindingResult) {
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotCreatedException(errorMesssage+ ": the task wasn't created");
        }
        taskService.save(taskMapper.toTask(taskDTO));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid TaskDTO taskDTO,
                                             BindingResult bindingResult, @PathVariable("id") long id) {
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotUpdatedException(errorMesssage.toString() + ": the task wasn't updated");
        }
        final boolean updated = taskService.update(id, taskMapper.toTask(taskDTO));
        return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") long id) {
        Boolean deleted = taskService.deleteById(id);
        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

}