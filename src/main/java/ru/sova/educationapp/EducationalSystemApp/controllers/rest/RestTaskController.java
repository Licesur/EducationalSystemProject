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

/**
 * REST контроллер для работы с сущностью задачи
 */
@RestController
@RequestMapping("/rest/tasks")
public class RestTaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    /**
     * Конструктор контроллера
     * @param taskService сервис для работы с сущностью задачи.
     * @param taskMapper маппер для трансформаций ТО сущности задачи.
     */
    @Autowired
    public RestTaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    /**
     * Метод, реалзиюущий get-запрос для получения списка всех задач.
     * @return возвращает ответ, содержащий статус запроса, и если запрос прошел успешно, также возвращает
     * список ТО задач.
     */
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasks() {
        final List<TaskDTO> tasks = taskService.findAll()
                .stream().map(taskMapper::toTaskDTO).toList();
        return !tasks.isEmpty()
                ? new ResponseEntity<>(tasks, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    /**
     * Метод, реалзиюущий get-запрос для получения конкретной задачи.
     * @return возвращает ответ, содержащий статус запроса, и если запрос прошел успешно, также возвращает
     * ТО задачи.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable("id") long id) {
        final TaskDTO taskDTO = taskMapper.toTaskDTO(taskService.findById(id));
        return taskDTO != null
                ? new ResponseEntity<>(taskDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    /**
     * Метод, реалзиюущий post-запрос для создания новой задачи.
     * @param taskDTO ТО сущности задачи, который содержит данные для создания новой задачи
     * @param bindingResult объект, хранящий данные об ошибках полей создаваемого объекта
     * @return при наличии ошибок пробрасывается исключение NotCreatedException, в случае если запрос
     * содержит корректный ТО задачи, создает задачу и возвращает статус CREATED
     */
    @PostMapping()
    public ResponseEntity<HttpStatus> createTask(@RequestBody @Valid TaskDTO taskDTO,
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
    /**
     * Метод, реалзиюущий patch-запрос для обновления задачи.
     * @param taskDTO ТО для обновления полй существующей задачи
     * @param bindingResult объект, хранящий данные об ошибках полей создаваемого объекта
     * @param id уникальный идентификатор обновляемой задачи
     * @return при наличии ошибок пробрасывается исключение NotUpdatedException, в случае если запрос
     * содержит корректный ТО задачи, обновляет задачу и возвращает статус OK
     */
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateTask(@RequestBody @Valid TaskDTO taskDTO,
                                                 BindingResult bindingResult, @PathVariable("id") long id) {
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotUpdatedException(errorMesssage+ ": the task wasn't updated");
        }
        final boolean updated = taskService.update(id, taskMapper.toTask(taskDTO));
        return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    /**
     * Метод, реалзиюущий delete-запрос для удаления задачи.
     * @param id уникальный идентификатор удаляемой задачи
     * @return вовзращает статус OK при успешном удалении студента и статус NOT_MODIFIED если удаление не удалось.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") long id) {
        Boolean deleted = taskService.deleteById(id);
        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

}