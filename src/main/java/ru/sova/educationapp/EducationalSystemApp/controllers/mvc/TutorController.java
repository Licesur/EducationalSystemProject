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
/**
 * MVC контроллер для работы с сущностью преподавателя
 */
@Controller
@RequestMapping("/tutors")
public class TutorController {

    private final TutorService tutorService;
    private final StudentService studentService;
    private final TutorMapper tutorMapper;
    private final StudentMapper studentMapper;

    /**
     * Конструктор контроллера
     * @param tutorService сервис для работы с сущностью преподавателя
     * @param studentService сервис для работы с сущностью студента
     * @param tutorMapper маппер для трансформаций ТО сущности преподавателя
     * @param studentMapper маппер для трансформаий ТО сущности студента
     */
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

    /**
     * Метод, возвращающий представление со списком всех преподавателей, с которой можно перейти на персональные
     * страницы преподавателей или создать нового
     * @param model модель для передачи параметров на представление
     * @return возвращает представление страницы со всеми преподавателями
     */
    @GetMapping
    public String getTutors(Model model) {
        model.addAttribute("tutors", tutorService.findAll().stream().map(tutorMapper::toTutorDTO));
        return "tutors/show";
    }

    /**
     * Метод, возвращающий представление для конкретного преподавателя, где можно перейти на страницу
     * редактирования преподавателя или удалить его
     * @param id уникальный идентификатор преподавателя
     * @param model модель для передачи параметров на представление
     * @return возвращает представление персональной страницы преподавателя
     */
    @GetMapping("/{id}")
    public String getTutor(@PathVariable("id") long id, Model model,
                           @ModelAttribute("student") StudentDTO studentDTO) {
        Tutor tutor = tutorService.findById(id);
        model.addAttribute("tutor", tutorMapper.toTutorDTO(tutor));
        model.addAttribute("studentsOfTheTutor", tutorService.findById(id).getStudents());
        model.addAttribute("validStudents", studentService.findByTutorsNotContains(tutor));
        return "tutors/index";
    }
    /**
     * Метод, возвращающий представление с формой для создания нового преподавателя
     * @param tutorDTO ТО сущности задачи для получения данных с формы при создании преподавателя
     * @return возвращает представление для создания нового преподавателя
     */
    @GetMapping("/new")
    public String newTutor(@ModelAttribute("tutor") TutorDTO tutorDTO) {
        return "tutors/new";
    }
    /**
     * Метод, реализующий создание преподавателя при отправке данных с формы.
     * Если при заполнении формы были введены некорректные данные, он вернет представление
     * с той же формой создания преподавателя, где отобразятся сообщения об ошибках в полях.
     * Если были введены корректные данные, преподаватель будет создан, и метод
     * перенаправит пользователя на страницу со всеми преподавателями.
     * @param tutorDTO ТО сущности преподавателя для получения данных с формы и дальнейшей работы с ними
     * @param bindingResult сущность, хранящая в себе ошибки валидации формы создания нового преподавателя
     * @return возвращает представления для переопределения некорректных полей формы создания
     * преподавателя или перенаправляет на страницу списка всех преподавателей,
     * если ошибок валидации полей формы нет
     */
    @PostMapping()
    public String createTutor(@ModelAttribute("tutor") @Valid TutorDTO tutorDTO, BindingResult bindingResult) {
//        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "tutors/new";
        }
        tutorService.save(tutorMapper.toTutor(tutorDTO));
        return "redirect:/tutors";
    }
    /**
     * Метод, возвращающий представление с формой обновления полей конкретного преподавателя.
     * @param model модель для передачи параметров на представление
     * @param id уникальный идентификатор преподавателя
     * @return возвращает представление с формой обновления данных преподавателя
     */
    @GetMapping("/{id}/edit")
    public String editTutor(Model model, @PathVariable("id") long id) {
        model.addAttribute("tutor", tutorMapper.toTutorDTO(tutorService.findById(id)));
        return "tutors/edit";
    }
    /**
     * Метод, производящий обновление полей преподавателя данными, полученными с формы обновления преподавателя.
     * @param tutorDTO ТО сущности преподавателя хранящий полученные с формы данные
     * @param bindingResult сущность, хранящая ошибки валдиации полей формы
     * @param id уникальный идентификатор задачи
     * @return в случае наличия ошибок валидации полей формы обновления преподавателя возвращает ту же форму,
     * демонстрирующую какие ошибки при заполнении полей задачи совершил пользователь. Если ошибок нет,
     * обновляет преподавателя и перенаправляет пользователя к представлению со списком всех преподавателей
     */
    @PatchMapping("/{id}")
    public String updateTutor(@ModelAttribute("tutor") @Valid TutorDTO tutorDTO,
                              BindingResult bindingResult, @PathVariable("id") long id) {
//        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "tutors/edit";
        }
        tutorService.update(id, tutorMapper.toTutor(tutorDTO));
        return "redirect:/tutors";
    }

    /**
     * Метод, удаляющий преподавателя по его уникальному идентификатору.
     * @param id уникальный идентификатор преподавателя
     * @return перенаправляет пользователя на представление со списком всех преподавателей
     * после удаления конкретного
     */
    @DeleteMapping("/{id}")
    public String deleteTutor(@PathVariable("id") long id) {
        tutorService.deleteById(id);
        return "redirect:/tutors";
    }

    /**
     * Метод, добавляющий выбранного студента в список обучающихся студентов у преподавателя. Также обновляет
     * список преподавателей у студента.
     * @param id уникальный идентификатор преподавателя
     * @param tutor объект сущности преподавателя для работы представления
     * @param studentDTO ТО сущности студента для выбора его на представлении и назначения преподавателю
     * @return перенаправляет пользлователя на страницу конкретного преподавателя, кому был добавлен студент
     */
    @PatchMapping("/{id}/choose")
    public String chooseStudent(@PathVariable("id") long id, @ModelAttribute("tutor") Tutor tutor,
                                @ModelAttribute("student") StudentDTO studentDTO) {
        tutorService.addStudent(studentService.findById(studentMapper.toStudent(studentDTO).getId()),
                tutorService.findById(id));
        return "redirect:/tutors/" + id;
    }

    /**
     * Метод, исключающий студента из списка обучающихся у преподавателя, и обновляющий списко преподавателей
     * у этого студента.
     * @param id уникальный идентификатор преподавателя
     * @param studentDTO ТО сущности студента для выбора его на представлении и исключения из списка преподавателя
     * @return перенаправляет пользователя на персональную страницу преподавателя с обновленными данными,
     * у которого был исключен студент.
     */
    @PatchMapping("/{id}/exclude")
    public String excludeStudent(@PathVariable("id") long id, @ModelAttribute("student") StudentDTO studentDTO) {
        tutorService.excludeStudent(studentService.findById(studentMapper.toStudent(studentDTO).getId()),
                tutorService.findById(id));
        return "redirect:/tutors/" + id;
    }
}

