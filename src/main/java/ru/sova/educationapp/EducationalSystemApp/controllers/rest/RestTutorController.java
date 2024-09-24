package ru.sova.educationapp.EducationalSystemApp.controllers.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.DTO.StudentDTO;
import ru.sova.educationapp.EducationalSystemApp.DTO.TutorDTO;
import ru.sova.educationapp.EducationalSystemApp.mappers.TutorMapper;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.services.TutorService;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotAssignedException;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotCreatedException;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotExcludedException;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotUpdatedException;

import java.util.List;
/**
 * REST контроллер для работы с сущностью преподавателя
 */
@RestController
@RequestMapping("/rest/tutors")
public class RestTutorController {

    private final TutorService tutorService;
    private final StudentService studentService;
    private final TutorMapper tutorMapper;

    /**
     * Конструктор контроллера
     * @param tutorService сервис для работы с сущностью преподавателя
     * @param studentService сервис для работы с сущностью студента
     * @param tutorMapper маппер для трансформаций ТО сущности преподавателя
     */
    @Autowired
    public RestTutorController(TutorService tutorService,
                               StudentService studentService,
                               TutorMapper tutorMapper) {
        this.tutorService = tutorService;
        this.studentService = studentService;
        this.tutorMapper = tutorMapper;
    }
    /**
     * Метод, реалзиюущий get-запрос для получения списка всех преподавателей.
     * @return возвращает ответ, содержащий статус запроса, и если запрос прошел успешно, также возвращает
     * список ТО преподавателей.
     */
    @GetMapping
    public ResponseEntity<List<TutorDTO>> getTutors() {
        final List<TutorDTO> tutors = tutorService.findAll()
                .stream().map(tutorMapper::toTutorDTO).toList();
        return !tutors.isEmpty()
                ? new ResponseEntity<>(tutors, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    /**
     * Метод, реалзиюущий get-запрос для получения конкретного преподавателя.
     * @return возвращает ответ, содержащий статус запроса, и если запрос прошел успешно, также возвращает
     * ТО преподавателя.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TutorDTO> getTutor(@PathVariable("id") long id) {
        final TutorDTO tutorDTO = tutorMapper.toTutorDTO(tutorService.findById(id));
        return tutorDTO != null
                ? new ResponseEntity<>(tutorDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    /**
     * Метод, реалзиюущий post-запрос для создания нового преподавателя.
     * @param tutorDTO ТО сущности преподавателя, который содержит данные для создания нового преподавателя
     * @param bindingResult объект, хранящий данные об ошибках полей создаваемого объекта
     * @return при наличии ошибок пробрасывается исключение NotCreatedException, в случае если запрос
     * содержит корректный ТО преподавателя, создает преподавателя и возвращает статус CREATED
     */
    @PostMapping()
    public ResponseEntity<HttpStatus> createTutor(@RequestBody @Valid TutorDTO tutorDTO,
                                                  BindingResult bindingResult) {
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotCreatedException(errorMesssage + ": the tutor wasn't created");
        }
        tutorService.save(tutorMapper.toTutor(tutorDTO));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * Метод, реалзиюущий patch-запрос для обновления преподавателя.
     * @param tutorDTO ТО для обновления полй существующего преподавателя
     * @param bindingResult объект, хранящий данные об ошибках полей создаваемого объекта
     * @param id уникальный идентификатор обновляемого преподавателя
     * @return при наличии ошибок пробрасывается исключение NotUpdatedException, в случае если запрос
     * содержит корректный ТО преподавателя, обновляет преподавателя и возвращает статус OK
     */
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateTutor(@RequestBody @Valid TutorDTO tutorDTO,
                                                  BindingResult bindingResult, @PathVariable("id") long id) {
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotUpdatedException(errorMesssage + ": the tutor wasn't updated");
        }
        final boolean updated = tutorService.update(id, tutorMapper.toTutor(tutorDTO));
        return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    /**
     * Метод, реалзиюущий delete-запрос для удаления преподавателя.
     * @param id уникальный идентификатор удаляемого преподавателя
     * @return вовзращает статус OK при успешном удалении преподавателя и статус NOT_MODIFIED,
     * если удаление не удалось.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTutor(@PathVariable("id") long id) {
        Boolean deleted = tutorService.deleteById(id);
        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /**
     * Метод назначающий выбранного студента преподавателю, и обновляющий список преподавателей у выбранного
     * студента.
     * @param id уникальный идентификатор преподавателя
     * @param studentDTO ТО студента, выбранного для назначения преподавателю
     * @param bindingResult объект, хранящий данные об ошибках полей создаваемого объекта
     * @return при наличии ошибок пробрасывается исключение NotCreatedException, в случае успешного
     * добавления возвращает статус запроса OK, если же добавление не удалось,возвращается статус NOT_MODIFIED
     */
    @PatchMapping("/{id}/choose")
    public ResponseEntity<HttpStatus> assignStudent(@PathVariable("id") long id,
                                                    @RequestBody StudentDTO studentDTO,
                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotAssignedException(errorMesssage+ ": the student wasn't assigned");
        }
        Boolean added = tutorService.addStudent(studentService.findById(studentDTO.getId()),
                tutorService.findById(id));
        return added ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /**
     * Метод, исключающий студента из списка обучающихся у преподавателя, а также обновляющий списко преподавателей
     * у исключаемого студента
     * @param id уникальный идентификатор преподавателя
     * @param studentDTO ТО сущность студента выбранного для исключения
     * @param bindingResult объект, хранящий данные об ошибках полей создаваемого объекта
     * @return при наличии ошибок пробрасывается исключение NotCreatedException, в случае успешного исключения
     * возвращает статус запроса OK, если же исключение не удалось, возвращается статус NOT_MODIFIED
     */
    @PatchMapping("/{id}/exclude")
    public ResponseEntity<HttpStatus> excludeStudent(@PathVariable("id") long id,
                                                     @RequestBody StudentDTO studentDTO,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotExcludedException(errorMesssage + ": the student wasn't excluded");
        }
        Boolean excluded = tutorService.excludeStudent(studentService.findById(studentDTO.getId()),
                tutorService.findById(id));
        return excluded ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}

