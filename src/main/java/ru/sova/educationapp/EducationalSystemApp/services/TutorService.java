package ru.sova.educationapp.EducationalSystemApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.repositories.TutorRepository;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
/**
 * Класс сервиса сущности преподавателя
 */
@Service
@Transactional(readOnly = true)
public class TutorService {

    private final TutorRepository tutorRepository;
    private final StudentService studentService;

    /**
     * Конструктор сервиса
     * @param tutorRepository репозиторий сущности преподавателя для взаимодействия с базой данных
     * @param studentService сервис для работы с сущностью студента
     */
    @Autowired
    public TutorService(TutorRepository tutorRepository, StudentService studentService) {
        this.tutorRepository = tutorRepository;
        this.studentService = studentService;
    }
    /**
     * Метод для получения списка преподавателей
     * @return возвращает список преподавателей из базы данных
     */
    public List<Tutor> findAll() {
        return tutorRepository.findAll();
    }
    /**
     * Метод для получения преподавателя по известному идентификатору
     * @param id уникальный идентификатор преподавателя
     * @return возвращает преподавателя, если он присутствует, или null
     */
    public Tutor findById(long id) {
        Optional<Tutor> foundBook = tutorRepository.findById(id);

        return foundBook.orElse(null);
    }
    /**
     * Метод предназначенный для сохранения преподавателя в базе данных
     * @param tutor сохраняемый объект преподавателя
     * @return возвращает этого же преподавателя
     */
    @Transactional(readOnly = false)
    public Tutor save(Tutor tutor) {
        return tutorRepository.save(tutor);
    }
    /**
     * Метод предназначенный для удаления преподавателя из базы данных по его id
     * @param id уникальный идентификатор преподавателя
     * @return возвращает boolean подтверждение отсутсвия преподавателя в базе данных
     */
    @Transactional(readOnly = false)
    public Boolean deleteById(long id) {
        tutorRepository.deleteById(id);
        return !tutorRepository.findById(id).isPresent();
    }
    /**
     * Метод предназначенный для обновления объекта преподавателя в базе данных
     * @param id уникальный идентификатор обновляемого преподавателя
     * @param tutor объект преподавателя, содержащий обновленную информацию, заносимую в базу данных
     * @return возвращает boolean подтверждение успешного обновления преподавателя
     */
    @Transactional(readOnly = false)
    public boolean update(long id, Tutor tutor) {
        tutor.setId(id);
        tutorRepository.save(tutor);

        return tutorRepository.findById(id).isPresent() && tutorRepository.findById(id).get().equals(tutor);
    }
    /**
     * Метод реализующий добавление студента в список обучающихся у преподавателя.
     * @param tutor объект преподавателя, обновляемый добавлением нового стдента
     * @param student объект студента, добавляемый к преподавателю
     * @return возвращает boolean значение подтверждающее добавление студента в список преподавателя
     */
    @Transactional(readOnly = false)
    public Boolean addStudent(Student student, Tutor tutor) {
        tutor = tutorRepository.findById(tutor.getId()).get();
        if (tutor.getStudents() == null) {
            tutor.setStudents(Collections.singletonList(student));
        } else if (!tutor.getStudents().contains(student)) {
            tutor.getStudents().add(student);
        }
        if (student.getTutors() == null) {
            student.setTutors(Collections.singletonList(tutor));
        } else if (!student.getTutors().contains(tutor)) {
            student.getTutors().add(tutor);
        }
        studentService.save(student);
        tutorRepository.save(tutor);
        return tutor.getStudents().contains(student);
    }
    /**
     * Метод, предназначенный для исключения студента из списка обсучающихся у преподавателя.
     * @param student исключаемый студент
     * @param tutor обновляемый преподаватель
     */
    @Transactional(readOnly = false)
    public Boolean excludeStudent(Student student, Tutor tutor) {
        tutor.getStudents().remove(student);
        student.getTutors().remove(tutor);
        studentService.save(student);
        tutorRepository.save(tutor);
        return !tutor.getStudents().contains(student);
    }
}
