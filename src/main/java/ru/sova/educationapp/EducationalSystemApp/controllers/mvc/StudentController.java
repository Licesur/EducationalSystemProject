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
import ru.sova.educationapp.EducationalSystemApp.mappers.TutorMapper;
import ru.sova.educationapp.EducationalSystemApp.mappers.VerificationWorkMapper;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.services.VerificationWorkService;

import java.util.stream.Collectors;
/**
 * MVC контроллер для работы с сущностью студента
 */


@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;
    private final TutorMapper tutorMapper;
    private final VerificationWorkMapper verificationWorkMapper;
    private final VerificationWorkService verificationWorkService;

    /**
     * Конструктор контроллера
     * @param studentService сервис для работы с сущностью студента
     * @param studentMapper маппер для трансформаций ТО студента
     * @param tutorMapper маппер для трансформаций ТО преподавателя
     * @param verificationWorkMapper маппер для трансформаций ТО контрольных работ
     * @param verificationWorkService сервис для работы с сущностью контрольных работ
     */
    @Autowired
    public StudentController(StudentService studentService, StudentMapper studentMapper,
                             TutorMapper tutorMapper, VerificationWorkMapper verificationWorkMapper,
                             VerificationWorkService verificationWorkService) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
        this.tutorMapper = tutorMapper;
        this.verificationWorkMapper = verificationWorkMapper;
        this.verificationWorkService = verificationWorkService;
    }

    /**
     * Метод, возвращающий представление со списком всех студнтов, с которой можно перейти на персональные
     * страницы студентов или создать нового
     * @param model модель для передачи параметров на представление
     * @return возвращает представление страницы со всеми студентами
     */
    @GetMapping
    public String getStudents(Model model) {
        model.addAttribute("students", studentService.findAll()
                .stream().map(studentMapper::toStudentDTO));
        return "students/show";
    }

    /**
     * Метод, возвращающий представление для конкретного студента, где можно перейти на страницу
     * редактирования студента, удалить его
     * @param id уникальный идентификатор студента
     * @param model модель для передачи параметров на представление
     * @param verificationWorkDTO атрибут модели для отображения списка контрольных работ, которые назначены
     *                            сейчас студенту для выполнения
     * @return - возвращает представление персональной страницы студента
     */
    @GetMapping("/{id}")
    public String getStudent(@PathVariable("id") long id, Model model,
                             @ModelAttribute("work") VerificationWorkDTO verificationWorkDTO) {
        model.addAttribute("student", studentMapper.toStudentDTO(studentService.findById(id)));

        model.addAttribute("tutors", studentService.findById(id).getTutors()
                        .stream().map(tutorMapper::toTutorDTO).collect(Collectors.toList()));
        return "students/index";
    }

    /**
     * Метод, возвращающий представление с формой для создания нового студента
     * @param studentDTO ТО сущности студента для получения данных с формы при создании студента
     * @return возвращает представление для создания студента
     */
    @GetMapping("/new")
    public String newStudent(@ModelAttribute("student") StudentDTO studentDTO) {
        return "students/new";
    }

    /**
     * Метод, реализующий создание студента при отправке данных формы. Если при заполнении формы были введены
     * некорректные данные, он вернет представление с той же формой создания студента, где отобразятся
     * сообщения об ошибках в полях. Если были введены корректные данные, студент будет создан, и метод
     * перенаправит пользователя на страницу со всеми студентами.
     * @param studentDTO ТО сущности студента для получения данных с формы и дальнейшей работы с ними
     * @param bindingResult сущность, хранящая в себе ошибки валидации формы создания нового студента
     * @return представления для переопределения некорректных полей формы создания студента или перенаправляет
     * на страницу списка всех студентов
     */
    @PostMapping()
    public String createStudent(@ModelAttribute("student") @Valid StudentDTO studentDTO, BindingResult bindingResult) {
//        personValidator.validate(person, bindingResult);//todo

        if (bindingResult.hasErrors()) {
            return "students/new";
        }
        studentService.save(studentMapper.toStudent(studentDTO));
        return "redirect:/students";
    }

    /**
     * Метод, возвращающий представление с формой обновления полей конкретного студента.
     * @param model модель для передачи параметров на представление
     * @param id уникальный идентификатор студента
     * @return возвращает представление с формой обновления данных студента
     */
    @GetMapping("/{id}/edit")
    public String editStudent(Model model, @PathVariable("id") long id) {
        model.addAttribute("student", studentMapper.toStudentDTO(studentService.findById(id)));
        return "students/edit";
    }

    /**
     * Метод, производящий обновление полей студента данными, полученными с формы обновления студента.
     * @param studentDTO ТО сущности студента хранящий полученные с формы данные
     * @param bindingResult сущность, хранящая ошибки валдиации полей формы
     * @param id уникальный идентификатор студента
     * @return в случае наличия ошибок валидации полей формы обновления студента возвращает ту же форму,
     * демонстрирующую какие ошибки при заполнении полей студента совершил пользователь. Если ошибок нет,
     * обновляет студента и перенаправляет пользователя к представлению со списком всех студентов
     */
    @PatchMapping("/{id}")
    public String updateStudent(@ModelAttribute("student") @Valid StudentDTO studentDTO,
                                BindingResult bindingResult, @PathVariable("id") long id) {
//        personValidator.validate(person, bindingResult);//todo

        if (bindingResult.hasErrors()) {
            return "students/edit";
        }
        studentService.update(id, studentMapper.toStudent(studentDTO));
        return "redirect:/students";
    }

    /**
     * Метод, удаляющий студента по его уникальному идентификатору.
     * @param id уникальный идентификатор студента
     * @return перенаправляет пользователя на представление со списком всех студентов после удаления студента
     */
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable("id") long id) {
        studentService.deleteById(id);
        return "redirect:/students";
    }

    /**
     * Метод, удаляющий контрольную работу из списка назначенных сущности студента, а также студента из аналогичного
     * списка студентов у сущности контрольной работы.
     * @param id уникальный идентификатор студента
     * @param verificationWorkDTO ТО контрольной работы, передающий информацию о работе, которую нужно исключить
     * @return перенаправляет пользователя к представлению уникальной страницы студента
     */
    @PatchMapping("/{id}/excludeWork")
    public String excludeWork(@PathVariable("id") long id,
                              @ModelAttribute("workToExclude") VerificationWorkDTO verificationWorkDTO) {
        studentService.excludeWork(studentService.findById(id),
                verificationWorkService.findById(verificationWorkMapper
                        .toVerificationWork(verificationWorkDTO).getId()));
        return "redirect:/students/" + id;
    }
}
