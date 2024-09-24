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
/**
 * MVC контроллер для работы с сущностью контрольной работы
 */
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

    /**
     * Конструктор контроллера
     * @param verificationWorkService сервис для работы с сущностью контрольной работы
     * @param taskService сервис для работы с сущностью задачи
     * @param studentService сервис для работы с сущностью студента
     * @param verificationWorkMapper маппер для трансформаций ТО сущности контрольной работы
     * @param taskMapper маппер для трансформаций ТО сущности задачи
     * @param studentMapper маппер для трансформаций ТО сущности студента
     * @param taskListMapper маппер для трансформаций ТО списка задач
     */
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
    /**
     * Метод, возвращающий представление со списком всех контрольный работ, с которой можно перейти на персональные
     * страницы работ или создать новую
     * @param model модель для передачи параметров на представление
     * @return возвращает представление страницы со всеми контрольными работами
     */
    @GetMapping
    public String getVerificationWorks(Model model) {
        model.addAttribute("works", verificationWorkService.findAll().stream()
                .map(verificationWorkMapper::toVerificationWorkDTO));
        return "works/show";
    }
    /**
     * Метод, возвращающий представление для конкретной контрольной рыботы, где можно перейти на страницу
     * редактирования этой работы или удалить ее
     * @param id уникальный идентификатор контрольной работы
     * @param model модель для передачи параметров на представление
     * @param studentDTO ТО сущности студента для работы представления
     * @return возвращает представление персональной страницы контрольной работы
     */
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
    /**
     * Метод, возвращающий представление с формой для создания новой контрольной работы
     * @param verificationWorkDTO ТО сущности контрольной работы для получения данных с формы при создании
     *                            контрольной работы
     * @return возвращает представление для создания новой контрольной работы
     */
    @GetMapping("/new")
    public String newVerificationWork(@ModelAttribute("work") VerificationWorkDTO verificationWorkDTO,
                                      Model model) {
        model.addAttribute("tasksDTO", taskListMapper.taskListToTaskDTOList(taskService.findAll()));
        return "works/new";
    }
    /**
     * Метод, реализующий создание контрольной работы при отправке данных с формы.
     * Если при заполнении формы были введены некорректные данные, он вернет представление
     * с той же формой создания преподавателя, где отобразятся сообщения об ошибках в полях.
     * Если были введены корректные данные, контрольная работа будет создана, и метод
     * перенаправит пользователя на страницу со всеми контрольными работами.
     * @param verificationWorkDTO ТО сущности контрольной работы для получения данных с формы и дальнейшей
     * работы с ними.
     * @param bindingResult сущность, хранящая в себе ошибки валидации формы создания новой контрольной работы.
     * @param selectedTasksDtoId список задач, выбранных при создании контрольной работы в качестве ее наполнения.
     * @return возвращает представления для переопределения некорректных полей формы создания
     * контрольной работы или перенаправляет на страницу списка всех контрольных работ,
     * если ошибок валидации полей формы нет
     */
    @PostMapping()
    public String createVerificationWork(@ModelAttribute("work") @Valid VerificationWorkDTO verificationWorkDTO,
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
    /**
     * Метод, возвращающий представление с формой обновления полей конкретной контрольной работы.
     * @param model модель для передачи параметров на представление
     * @param id уникальный идентификатор контрольной работы
     * @return возвращает представление с формой обновления данных контрольной работы
     */
    @GetMapping("/{id}/edit")
    public String editVerificationWork(Model model, @PathVariable("id") long id) {
        model.addAttribute("work", verificationWorkMapper
                .toVerificationWorkDTO(verificationWorkService.findById(id)));
        model.addAttribute("tasks", taskService.findAll().stream()
                .map(taskMapper::toTaskDTO).collect(Collectors.toList()));
        return "works/edit";
    }
    /**
     * Метод, производящий обновление полей контрольной работы данными, полученными с формы обновления
     * контрольной работы.
     * @param workToUpdateDTO ТО сущности контрольной работы хранящий полученные с формы данные для обновления.
     * @param bindingResult сущность, хранящая ошибки валдиации полей формы.
     * @param id уникальный идентификатор контрольной работы.
     * @param selectedTasksDtoId список выбранных задач, которые были выбраны для добавления в обновленную
     * контрольную работу.
     * @return в случае наличия ошибок валидации полей формы обновления контрольной работы возвращает
     * ту же форму, демонстрирующую какие ошибки при заполнении полей формы совершил пользователь.
     * Если ошибок нет, обновляет контрольную работу и перенаправляет пользователя к представлению со списком
     * всех контрольных работ.
     */
    @PatchMapping("/{id}")
    public String updateVerificationWork(@ModelAttribute("work") @Valid VerificationWorkDTO workToUpdateDTO,
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

    /**
     * Метод, удаляющий контрольную работу по его уникальному идентификатору.
     * @param id уникальный идентификатор контрольной работы
     * @return перенаправляет пользователя на представление со списком всех контрольных работ
     * после удаления конкретной
     */
    @DeleteMapping("/{id}")
    public String deleteVerificationWork(@PathVariable("id") long id) {
        verificationWorkService.deleteById(id);
        return "redirect:/works";
    }
    /**
     * Метод, добавляющий выбранного студента в список студентов выполняющих контрольную работу. Также обновляет
     * список контрольных работ, назначенных к выполнению, у студента.
     * @param id уникальный идентификатор контрольной работы.
     * @param verificationWorkDTO ТО сущности контрольной работы для работы представления.
     * @param studentDTO ТО сущности студента для выбора его на представлении и назначения ему контрольной работы.
     * @return перенаправляет пользлователя на страницу конкретной контрольной работы, кому был добавлен студент
     */
    @PatchMapping("/{id}/choose")
    public String chooseStudent(@PathVariable("id") long id,
                                @ModelAttribute("work") VerificationWorkDTO verificationWorkDTO,
                                @ModelAttribute("student") StudentDTO studentDTO) {
        studentService.addVerificationWork(verificationWorkService.findById(id),
                studentMapper.toStudent(studentDTO));
        return "redirect:/works/" + id;
    }
}