package ru.sova.educationapp.EducationalSystemApp.controllers.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.sova.educationapp.EducationalSystemApp.DTO.StudentDTO;
import ru.sova.educationapp.EducationalSystemApp.DTO.TaskDTO;
import ru.sova.educationapp.EducationalSystemApp.mappers.StudentMapper;
import ru.sova.educationapp.EducationalSystemApp.mappers.TaskListMapper;
import ru.sova.educationapp.EducationalSystemApp.services.StudentService;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotCreatedException;
import ru.sova.educationapp.EducationalSystemApp.exceptions.NotUpdatedException;

import java.util.List;
import java.util.Map;

/**
 * REST контроллер для работы с сущностью студента
 */
@RestController
@RequestMapping("/rest/students")
public class RestStudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;
    private final TaskListMapper taskListMapper;

    /**
     * Конструктор контроллера
     * @param studentService сервис для работы с сущностью студента
     * @param studentMapper маппер для трансформаций ТО сузности студента
     * @param taskListMapper маппер для трансформаций списков ТО сущности студента
     */
    @Autowired
    public RestStudentController(StudentService studentService,
                                 StudentMapper studentMapper,
                                 TaskListMapper taskListMapper) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
        this.taskListMapper = taskListMapper;
    }

    /**
     * Метод, реалзиюущий get-запрос для получения списка всех студентов.
     * @return возвращает ответ, содержащий статус запроса, и если запрос прошел успешно, также возвращает
     * список ТО студентов.
     */
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getStudents() {
        final List<StudentDTO> students = studentService.findAll()
                .stream().map(studentMapper::toStudentDTO).toList();
        return !students.isEmpty()
                ? new ResponseEntity<>(students, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    /**
     * Метод, реалзиюущий get-запрос для получения конкретного студента.
     * @return возвращает ответ, содержащий статус запроса, и если запрос прошел успешно, также возвращает
     * ТО студента.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable("id") long id) {
        final StudentDTO student = studentMapper.toStudentDTO(studentService.findById(id));
        return student != null
                ? new ResponseEntity<>(student, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Метод, реалзиюущий post-запрос для создания нового студента.
     * @param studentDTO ТО сущности студента, который содержит данные для создания нового студента
     * @param bindingResult объект, хранящий данные об ошибках полей создаваемого объекта
     * @return при наличии ошибок пробрасывается исключение NotCreatedException, в случае если запрос
     * содержит корректный ТО студента, создает студента и возвращает статус CREATED
     */
    @PostMapping()
    public ResponseEntity<HttpStatus> createStudent(@RequestBody @Valid
                                             @Parameter(description = "student object you want to create",
                                                     required = true) StudentDTO studentDTO,
                                                    BindingResult bindingResult) {
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotCreatedException(errorMesssage + ": the student wasn't created");
        }
        studentService.save(studentMapper.toStudent(studentDTO));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * Метод, реалзиюущий patch-запрос для обновления студента.
     * @param studentDTO ТО для обновления полй существующего студента
     * @param bindingResult объект, хранящий данные об ошибках полей создаваемого объекта
     * @param id уникальный идентификатор обновляемого студента
     * @return при наличии ошибок пробрасывается исключение NotUpdatedException, в случае если запрос
     * содержит корректный ТО студента, возвращает статус OK
     */
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateStudent(@RequestBody @Valid StudentDTO studentDTO,
                                                    BindingResult bindingResult, @PathVariable("id") long id) {
//        personValidator.validate(person, bindingResult);//todo
        if (bindingResult.hasErrors()) {
            StringBuilder errorMesssage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMesssage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new NotUpdatedException(errorMesssage + ": the student wasn't updated");
        }
        final boolean updated = studentService.update(id, studentMapper.toStudent(studentDTO));
        return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /**
     * Метод, реалзиюущий delete-запрос для удаления студента.
     * @param id уникальный идентификатор удаляемого студента
     * @return вовзращает статус OK при успешном удалении студента и статус NOT_MODIFIED если удаление не удалось.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") long id) {
        Boolean deleted = studentService.deleteById(id);
        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /**
     * Метод, реализующий get-запрос по которому предоставляются задачи из контрольной работы студента,
     * для их дальнейшего решения.
     * @param id уникальный идентификатор студента.
     * @param workId уникальный идентификатор работы, выбранной для решения пользователем.
     * @return в случае успешного запроса возвращает список задач и статус запроса OK. В случае отсутствия
     * задач возвращает статус NOT_FOUND.
     */
    @GetMapping("/{id}/works/{workId}")
    public ResponseEntity<?> getTasksFromVerificationWork(@PathVariable("id")
                                                          @Schema(description =
                                                                  "Введите айди ученика")
                                                          long id,
                                                          @PathVariable("workId")
                                                          @Schema(description
                                                                  = "Введите айди проверяемой работы")
                                                          long workId) {
        final List<TaskDTO> tasks = taskListMapper
                .taskListToTaskDTOList(studentService.findTasksFromVerificationWork(id, workId));
        return !tasks.isEmpty()
                ? new ResponseEntity<>(tasks, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Метод, осуществляющий проверку решений задач контрольной работы, отправляемых студентом.
     * @param id уникальный идентификатор студента.
     * @param workId уникальный идентификатор работы, выбранной для решения пользователем.
     * @param answers список ответов на задачи, представляющий собой список ТО задач, входящих в контрольную работу.
     * @return Возвращает Список пар ключ(айди задачи)-значение(статус ответа true/false) со статусом запроса OK.
     */
    @GetMapping("/{id}/works/{workId}/solution")
    public ResponseEntity<?> getSolutionStatus(@PathVariable("id")
                                               @Schema(description =
                                                       "Введите айди ученика")
                                               long id,
                                               @PathVariable("workId")
                                               @Schema(description = "Введите айди проверяемой работы")
                                               long workId,
                                               @RequestBody @Valid
                                               @Schema(description =
                                                       "Введите ответы в формате списка задач",
                                                       example = """
                                                               [
                                                                   {
                                                                       "id": 1,
                                                                       "definition":"",
                                                                       "answer": "5"
                                                                   },
                                                                   {
                                                                       "id": 2,
                                                                       "definition":"",
                                                                       "answer": "1"
                                                                   }
                                                               ]""")
                                               List<TaskDTO> answers) {

        Map<Long, Boolean> solution = studentService.checkAnswers(workId, answers);
        return !solution.isEmpty()
                ? new ResponseEntity<>(solution, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
}

