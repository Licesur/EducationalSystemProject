package ru.sova.educationapp.EducationalSystemApp.controllers.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.DTO.StudentDTO;
import ru.sova.educationapp.EducationalSystemApp.DTO.VerificationWorkDTO;
import ru.sova.educationapp.EducationalSystemApp.mappers.StudentMapper;
import ru.sova.educationapp.EducationalSystemApp.mappers.VerificationWorkMapper;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.services.VerificationWorkService;
import ru.sova.educationapp.EducationalSystemApp.exceptions.*;

import java.util.List;
/**
 * REST контроллер для работы с сущностью контрольной работы
 */
@RestController
@RequestMapping("/rest/works")
public class RestVerificationWorkController {

    private final VerificationWorkService verificationWorkService;
    private final StudentService studentService;
    private final VerificationWorkMapper verificationWorkMapper;
    private final StudentMapper studentMapper;

    /**
     * Конструктор контроллера
     * @param verificationWorkService сервис для работы с сущностью контрольной работы
     * @param studentService сервис для работы с сущностью студента
     * @param verificationWorkMapper маппер для трансформаций ТО сущности контрольной работы
     * @param studentMapper маппер для трансформаций ТО сущности студента
     */
    @Autowired
    public RestVerificationWorkController(VerificationWorkService verificationWorkService,
                                          StudentService studentService,
                                          VerificationWorkMapper verificationWorkMapper,
                                          StudentMapper studentMapper) {
        this.verificationWorkService = verificationWorkService;
        this.studentService = studentService;
        this.verificationWorkMapper = verificationWorkMapper;
        this.studentMapper = studentMapper;
    }
    /**
     * Метод, реалзиюущий get-запрос для получения списка всех контрольных работ.
     * @return возвращает ответ, содержащий статус запроса, и если запрос прошел успешно, также возвращает
     * список контрольных работ.
     */
    @GetMapping
    public ResponseEntity<List<VerificationWorkDTO>> getVerificationWorks() {
        final List<VerificationWorkDTO> verificationWorks = verificationWorkService.findAll()
                .stream().map(verificationWorkMapper::toVerificationWorkDTO).toList();
        return !verificationWorks.isEmpty()
                ? new ResponseEntity<>(verificationWorks, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    /**
     * Метод, реалзиюущий get-запрос для получения конкретной контрольной работы.
     * @return возвращает ответ, содержащий статус запроса, и если запрос прошел успешно, также возвращает
     * ТО контрольной работы.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VerificationWorkDTO> getVerificationWork(@PathVariable("id") long id) {
        final VerificationWorkDTO verificationWorkDTO = verificationWorkMapper
                .toVerificationWorkDTO(verificationWorkService.findById(id));
        return verificationWorkDTO != null
                ? new ResponseEntity<>(verificationWorkDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    /**
     * Метод, реалзиюущий post-запрос для создания новой контрольной работы.
     * @param verificationWorkDTO ТО сущности контрольной работы, который содержит данные для создания
     * новой контрольной работы
     * @param bindingResult объект, хранящий данные об ошибках полей создаваемого объекта
     * @return при наличии ошибок пробрасывается исключение NotCreatedException, в случае если запрос
     * содержит корректный ТО контрольной работы, создает контрольную работу и возвращает статус CREATED
     */
    @PostMapping()
    public ResponseEntity<HttpStatus> createVerificationWork(@RequestBody @Valid VerificationWorkDTO verificationWorkDTO,
                                                             BindingResult bindingResult) {
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotCreatedException(errorMesssage + ": work wasn't created");
        }
        verificationWorkService.save(verificationWorkMapper.toVerificationWork(verificationWorkDTO));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
    /**
     * Метод, реалзиюущий patch-запрос для обновления контрольной работы.
     * @param workToUpdateDTO ТО для обновления полй существующей контрольной работы
     * @param bindingResult объект, хранящий данные об ошибках полей создаваемого объекта
     * @param id уникальный идентификатор обновляемой контрольной работы
     * @return при наличии ошибок пробрасывается исключение NotUpdatedException, в случае если запрос
     * содержит корректный ТО контрольной работы, обновляет контрольную работу и возвращает статус OK
     */
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateVerificationWork(@RequestBody @Valid VerificationWorkDTO workToUpdateDTO,
                                                             BindingResult bindingResult, @PathVariable("id") long id) {
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotUpdatedException(errorMesssage + ": the chosen work wasn't updated");
        }
        final boolean updated = verificationWorkService.update(id, verificationWorkMapper
                .toVerificationWork(workToUpdateDTO));
        return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    /**
     * Метод, реалзиюущий delete-запрос для удаления контрольной работы.
     * @param id уникальный идентификатор удаляемой контрольной работы
     * @return вовзращает статус OK при успешном удалении контрольной работы и статус NOT_MODIFIED,
     * если удаление не удалось.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVerificationWork(@PathVariable("id") long id) {
        boolean deleted = verificationWorkService.deleteById(id);
        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /**
     * Метод реализующий назначение контрольной работы выбранному студенту, и, также добавляющий ее в список
     * контрольных работ студента.
     * @param id уникальный идентификатор контрольной работы
     * @param studentDTO ТО сущности студента, выбранного для назначения контрольной работы
     * @param bindingResult объект, хранящий данные об ошибках полей создаваемого объекта
     * @return при наличии ошибок пробрасывается исключение NotAssignedException, в случае если запрос
     * содержит корректный ТО студента, назначает контрольную работу и возвращает статус OK
     */
    @PatchMapping("/{id}/choose")
    public ResponseEntity<HttpStatus> assignToStudent(@PathVariable("id") long id,
                                                      @RequestBody @Valid StudentDTO studentDTO,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotAssignedException(errorMesssage +
                    ": work wasn't assigned to the chosen student");
        }
        Boolean added = studentService.addVerificationWork(verificationWorkService.findById(id),
                studentMapper.toStudent(studentDTO));
        return added ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}