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
/**
 * MVC контроллер для работы с сущностью задачи
 */
@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    /**
     * Конструктор контроллера
     * @param taskService сервис для работы с сущностью задачи
     * @param taskMapper маппер для трансформаций ТО сущности задачи
     */
    @Autowired
    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    /**
     * Метод, возвращающий представление со списком всех задач, с которой можно перейти на персональные
     * страницы задач или создать новую
     * @param model модель для передачи параметров на представление
     * @return возвращает представление страницы со всеми задачами
     */
    @GetMapping
    public String getTasks(Model model) {
        model.addAttribute("tasks", taskService.findAll().stream().map(taskMapper::toTaskDTO));
        return "tasks/show";
    }

    /**
     * Метод, возвращающий представление для конкретной задачи, где можно перейти на страницу
     * редактирования задачи, удалить ее
     * @param id уникальный идентификатор задачи
     * @param model модель для передачи параметров на представление
     * @return возвращает представление персональной страницы задачи
     */
    @GetMapping("/{id}")
    public String getTask(@PathVariable("id") long id, Model model) {
        model.addAttribute("task", taskMapper.toTaskDTO(taskService.findById(id)));
        return "tasks/index";
    }

    /**
     * Метод, возвращающий представление с формой для создания нового студента
     * @param taskDTO ТО сущности задачи для получения данных с формы при создании студента
     * @return возвращает представление для создания новой задачи
     */
    @GetMapping("/new")
    public String newTask(@ModelAttribute("task") TaskDTO taskDTO) {
        return "tasks/new";
    }

    /**
     * Метод, реализующий создание задачи при отправке данных формы. Если при заполнении формы были введены
     * некорректные данные, он вернет представление с той же формой создания задачи, где отобразятся
     * сообщения об ошибках в полях. Если были введены корректные данные, задача будет создана, и метод
     * перенаправит пользователя на страницу со всеми задачами.
     * @param taskDTO ТО сущности задачи для получения данных с формы и дальнейшей работы с ними
     * @param bindingResult сущность, хранящая в себе ошибки валидации формы создания новой задачи
     * @return возвращает представления для переопределения некорректных полей формы создания
     * студента или перенаправляет на страницу списка всех студентов, если ошибок валидации полей формы нет
     */
    @PostMapping()
    public String createTask(@ModelAttribute("task") @Valid TaskDTO taskDTO, BindingResult bindingResult) {
//        personValidator.validate(person, bindingResult);//todo

        if (bindingResult.hasErrors()) {
            return "tasks/new";
        }
        taskService.save(taskMapper.toTask(taskDTO));
        return "redirect:/tasks";
    }

    /**
     * Метод, возвращающий представление с формой обновления полей конкретной задачи.
     * @param model модель для передачи параметров на представление
     * @param id уникальный идентификатор задачи
     * @return возвращает представление с формой обновления данных задачи
     */
    @GetMapping("/{id}/edit")
    public String editTask(Model model, @PathVariable("id") long id) {
        model.addAttribute("task", taskMapper.toTaskDTO(taskService.findById(id)));
        return "tasks/edit";
    }

    /**
     * Метод, производящий обновление полей задачи данными, полученными с формы обновления задачи.
     * @param taskDTO ТО сущности задачи хранящий полученные с формы данные
     * @param bindingResult сущность, хранящая ошибки валдиации полей формы
     * @param id уникальный идентификатор задачи
     * @return в случае наличия ошибок валидации полей формы обновления задачи возвращает ту же форму,
     * демонстрирующую какие ошибки при заполнении полей задачи совершил пользователь. Если ошибок нет,
     * обновляет задачу и перенаправляет пользователя к представлению со списком всех задач
     */
    @PatchMapping("/{id}")
    public String updateTask(@ModelAttribute("task") @Valid TaskDTO taskDTO,
                             BindingResult bindingResult, @PathVariable("id") long id) {
//        personValidator.validate(person, bindingResult);//todo

        if (bindingResult.hasErrors()) {
            return "tasks/edit";
        }
        taskService.update(id, taskMapper.toTask(taskDTO));
        return "redirect:/tasks";
    }

    /**
     * Метод, удаляющий задачу по его уникальному идентификатору.
     * @param id уникальный идентификатор задачи
     * @return перенаправляет пользователя на представление со списком всех задач после удаления конкретной
     */
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable("id") long id) {
        taskService.deleteById(id);
        return "redirect:/tasks";
    }
}