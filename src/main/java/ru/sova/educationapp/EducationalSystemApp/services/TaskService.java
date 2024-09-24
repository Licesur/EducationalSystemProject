package ru.sova.educationapp.EducationalSystemApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.repositories.TaskRepository;

import java.util.List;
import java.util.Optional;
/**
 * Класс сервиса сущности задачи
 */
@Service
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;

    /**
     * Конструктор сервиса
     * @param taskRepository репозиторий сущности задачи для взаимодействия с базой данных
     */
    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    /**
     * Метод для получения списка задач
     * @return возвращает список задач из базы данных
     */
    public List<Task> findAll() {
        return taskRepository.findAll();
    }
    /**
     * Метод для получения задачи по известному идентификатору
     * @param id уникальный идентификатор задачи
     * @return возвращает задачу, если она присутствует, или null
     */
    public Task findById(long id) {
        Optional<Task> foundTask = taskRepository.findById(id);

        return foundTask.orElse(null);
    }
    /**
     * Метод предназначенный для сохранения задачи в базе данных
     * @param task сохраняемый объект задачи
     * @return возвращает эту же задачу
     */
    @Transactional(readOnly = false)
    public Task save(Task task) {
        return taskRepository.save(task);
    }
    /**
     * Метод предназначенный для удаления задачи из базы данных по ее id
     * @param id уникальный идентификатор задачи
     * @return возвращает boolean подтверждение отсутсвия задачи в базе данных
     */
    @Transactional(readOnly = false)
    public Boolean deleteById(long id) {
        taskRepository.deleteById(id);
        return !taskRepository.findById(id).isPresent();
    }
    /**
     * Метод предназначенный для обновления объекта задачи в базе данных
     * @param id уникальный идентификатор обновляемой задачи
     * @param task объект задачи, содержащий обновленную информацию, заносимую в базу данных
     * @return возвращает boolean подтверждение успешного обновления задачи
     */
    @Transactional(readOnly = false)
    public Boolean update(long id, Task task) {
        task.setId(id);
        taskRepository.save(task);

        return taskRepository.findById(id).isPresent() && taskRepository.findById(id).get().equals(task);
    }
    /**
     * Метод предназначенный для поиска всех задач, относящихся к конкретной контрольной работе
     * @param verificationWork контрольная работа, к которой должны относиться задачи
     * @return возвращает список всех найденных задач, относящихся к данной контрольной работе.
     * Возвращаемый список может быть пустым
     */
    public List<Task> findByVerificationWork(VerificationWork verificationWork) {
        return taskRepository.findAllByVerificationWorksContains(verificationWork);
    }
}
