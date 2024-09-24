package ru.sova.educationapp.EducationalSystemApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.repositories.VerificationWorkRepository;

import java.util.List;
import java.util.Optional;
/**
 * Класс сервиса сущности контрольной работы
 */
@Service
@Transactional(readOnly = true)
public class VerificationWorkService {

    private final VerificationWorkRepository verificationWorkRepository;
    /**
     * Конструктор сервиса
     * @param verificationWorkRepository репозиторий сущности контрольной работы для взаимодействия
     * с базой данных
     */
    @Autowired
    public VerificationWorkService(VerificationWorkRepository verificationWorkRepository) {
        this.verificationWorkRepository = verificationWorkRepository;
    }
    /**
     * Метод для получения списка контрольных работ
     * @return возвращает список контрольных работ из базы данных
     */
    public List<VerificationWork> findAll() {
        return verificationWorkRepository.findAll();
    }
    /**
     * Метод для получения контрольной работы по известному идентификатору
     * @param id уникальный идентификатор контрольной работы
     * @return возвращает контрольную работу, если она присутствует, или null
     */
    public VerificationWork findById(long id) {
        Optional<VerificationWork> foundVerificationWork = verificationWorkRepository.findById(id);

        return foundVerificationWork.orElse(null);
    }
    /**
     * Метод предназначенный для сохранения контрольной работы в базе данных
     * @param verificationWork сохраняемый объект контрольной работы
     * @return возвращает эту же контрольную работу
     */
    @Transactional(readOnly = false)
    public VerificationWork save(VerificationWork verificationWork) {
        return verificationWorkRepository.save(verificationWork);
    }
    /**
     * Метод предназначенный для удаления контрольной работы из базы данных по ее id
     * @param id уникальный идентификатор контрольной работы
     * @return возвращает boolean подтверждение отсутсвия контрольной работы в базе данных
     */
    @Transactional(readOnly = false)
    public boolean deleteById(long id) {
        verificationWorkRepository.deleteById(id);
        return !verificationWorkRepository.findById(id).isPresent();
    }
    /**
     * Метод предназначенный для обновления объекта контрольной работы в базе данных
     * @param id уникальный идентификатор обновляемой контрольной работы
     * @param verificationWork объект контрольной работы, содержащий обновленную информацию,
     * заносимую в базу данных
     * @return возвращает boolean подтверждение успешного обновления контрольной работы
     */
    @Transactional(readOnly = false)
    public boolean update(long id, VerificationWork verificationWork) {
        verificationWork.setId(id);
        verificationWorkRepository.save(verificationWork);
        return verificationWorkRepository.findById(id).isPresent()
                && verificationWork.equals(verificationWorkRepository.findById(id).get());
    }

    /**
     * Метод предназначенный для наполнения контрольной работы конкретными задачами
     * @param verificationWork заполняеая контрольноя работа
     * @param tasks список задач, которые добавляются в контрольную работу
     */
    @Transactional(readOnly = false)
    public void fillTasks(VerificationWork verificationWork, List<Task> tasks) {
        verificationWork.setTasks(tasks);
        verificationWorkRepository.save(verificationWork);
    }
}
