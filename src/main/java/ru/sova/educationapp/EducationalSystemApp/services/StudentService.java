package ru.sova.educationapp.EducationalSystemApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sova.educationapp.EducationalSystemApp.DTO.TaskDTO;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.repositories.StudentRepository;

import java.util.*;

/**
 * Класс сервиса сущности студента
 */
@Service
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final VerificationWorkService verificationWorkService;
    private final TaskService taskService;

    /**
     * Конструктор сервиса
     * @param studentRepository репозиторий сущности студента для взаимодействия с базой данных
     * @param verificationWorkService сервис для работы с сущностью контрольной работы
     * @param taskService сервис для работы с сущностью задачи
     */
    @Autowired
    public StudentService(StudentRepository studentRepository, VerificationWorkService verificationWorkService,
                          TaskService taskService) {
        this.studentRepository = studentRepository;
        this.verificationWorkService = verificationWorkService;
        this.taskService = taskService;
    }

    /**
     * Метод реализующий добавление контрольной работы студенту.
     * @param verificationWork объект контрольной работы, добавляемый в список студенту
     * @param student объект студента, обновляемый добавлением контрольной работы
     * @return возвращает boolean значение подтверждающее добавление работы студенту
     */
    @Transactional(readOnly = false)
    public Boolean addVerificationWork(VerificationWork verificationWork, Student student) {
        student = studentRepository.findById(student.getId()).get();
        if (student.getVerificationWorks() == null) {
            student.setVerificationWorks(Collections.singletonList(verificationWork));
        } else if (!student.getVerificationWorks().contains(verificationWork)) {
            student.getVerificationWorks().add(verificationWork);
        }

        if (verificationWork.getStudents() == null) {
            verificationWork.setStudents(Collections.singletonList(student));
        } else if (!verificationWork.getStudents().contains(student)) {
            verificationWork.getStudents().add(student);
        }
        studentRepository.save(student);
        verificationWorkService.save(verificationWork);
        return student.getVerificationWorks().contains(verificationWork);
    }

    /**
     * Метод для получения списка студентов
     * @return возвращает список студентов из базы данных
     */
    public List<Student> findAll() {
        List<Student> students = studentRepository.findAll();
        return students;
    }

    /**
     * Метод для получения студента по известному идентификатору
     * @param id уникальный идентификатор студента
     * @return возвращает студента, если он присутствует, или null
     */
    public Student findById(long id) {
        Optional<Student> foundPerson = studentRepository.findById(id);

        return foundPerson.orElse(null);
    }

    /**
     * Метод предназначенный для сохранения студента в базе данных
     * @param student сохраняемый объект студента
     * @return возвращает этого же студента
     */
    @Transactional(readOnly = false)
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    /**
     * Метод предназначенный для удаления студента из базы данных по его id
     * @param id уникальный идентификатор студента
     * @return возвращает boolean подтверждение отсутсвия студента в базе данных
     */
    @Transactional(readOnly = false)
    public Boolean deleteById(long id) {
        studentRepository.deleteById(id);
        return !studentRepository.findById(id).isPresent();
    }

    /**
     * Метод предназначенный для обновления объекта студента в базе данных
     * @param id уникальный идентификатор обновляемого студента
     * @param student объект студент, содержащий обновленную информацию, заносимую в базу данных
     * @return возвращает boolean подтверждение успешного обновления студента
     */
    @Transactional(readOnly = false)
    public Boolean update(long id, Student student) {
        student.setId(id);
        studentRepository.save(student);

        return studentRepository.findById(id).isPresent() && studentRepository.findById(id).get().equals(student);
    }

    /**
     * Метод предназначенный для поиска всех студентов, кому была назначена конкретная контрольная работа
     * @param verificationWork контрольная работа, которая должна быть назначена к выполнению у найденного студента
     * @return возвращает список всех найденных студентов, у которых назначена данная контрольная работа.
     * Возвращаемый список может быть пустым
     */
    public List<Student> findByVerificationWork(VerificationWork verificationWork) {
        List<Student> students = studentRepository.findAllByVerificationWorksContains(verificationWork);
        return students == null ? Collections.emptyList() : students;
    }

    /**
     * Аналогичный метод, с противоположной идеей - ищет всех студентов, кому еще не была назначена данная
     * контрольная работа
     * @param verificationWork искомая контрольная работа
     * @return возвращает список всех найденных студентов, у которых не назначена данная контрольная работа.
     * Возвращаемый список может быть пустым
     */
    public List<Student> findByVerificationWorkNotContains(VerificationWork verificationWork) {
        List<Student> validStudents = studentRepository.findAllByVerificationWorksNotContains(verificationWork);
        return validStudents == null ? Collections.emptyList() : validStudents;
    }

    /**
     * Метод, предназначенный для поиска всех студентов, которые еще не обсучаются у конкретного преподавателя
     * @param tutor преподаватель, у которого не должны обучаться найденные студенты
     * @return возвращает список студентов, доступных для назначения выбранному преподавателю
     */
    public List<Student> findByTutorsNotContains(Tutor tutor) {
        return studentRepository.findByTutorsNotContains(tutor);
    }

    /**
     * Метод, предназначенный для исключения работы из списка назначенных студенту.
     * @param student обновляемый студент
     * @param verificationWork исключаемая работа
     */
    @Transactional(readOnly = false)
    public void excludeWork(Student student, VerificationWork verificationWork) {
        student.getVerificationWorks().remove(verificationWork);
        verificationWork.getStudents().remove(student);
        studentRepository.save(student);
        verificationWorkService.save(verificationWork);
    }

    /**
     * Метод предназначенный дл япроверки полученных от студента ответов по контрольной работы
     * @param workId уникальный идентификатор проверяемой контрольной работы
     * @param answers список ТО задач с ответами, введенными обучающимся
     * @return возвращает список пар ключ-значение из id задачи и boolean статуса полученного ответа
     */
    public Map<Long, Boolean> checkAnswers(long workId, List<TaskDTO> answers) {
        final List<Task> tasks = verificationWorkService.findById(workId).getTasks();
        Map<Long, Boolean> answersStatus = new HashMap<>();
        for (TaskDTO task : answers) {
            if (task.getAnswer().equals(taskService.findById(task.getId()).getAnswer())) {
                answersStatus.put(task.getId(), true);
            } else {
                answersStatus.put(task.getId(), false);
            }
        }
        for (Task task : tasks) {
            if (!answersStatus.containsKey(task.getId())) {
                answersStatus.put(task.getId(), false);
            }
        }
        return answersStatus;
    }

    /**
     * Метод предназначенный для возвращения студенту списка задач, относящихся к конкретной контрольной работы
     * @param id уникальный идентификатор студента
     * @param workId уникальный идентификатор запрашиваемой контрольной работы
     * @return возвращает список задач, относящихся к запрашиваемой контрольной работе
     */
    public List<Task> findTasksFromVerificationWork(long id, long workId) {
        return studentRepository.findById(id).get().getVerificationWorks().stream()
                .filter(s -> s.getId() == workId).findAny().get().getTasks();
    }
}
