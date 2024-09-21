package ru.sova.educationapp.EducationalSystemApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.repositories.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> finAll() {
        return taskRepository.findAll();
    }

    public Task findById(long id) {
        Optional<Task> foundTask = taskRepository.findById(id);

        return foundTask.orElse(null);
    }

    @Transactional(readOnly = false)
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Transactional(readOnly = false)
    public Boolean deleteById(long id) {
        taskRepository.deleteById(id);
        return !taskRepository.findById(id).isPresent();
    }

    @Transactional(readOnly = false)
    public Boolean update(long id, Task task) {
        task.setId(id);
        taskRepository.save(task);

        return taskRepository.findById(id).isPresent() && taskRepository.findById(id).get().equals(task);
    }

    public List<Task> findByVerificationWork(VerificationWork verificationWork) {
        return taskRepository.findAllByVerificationWorksContains(verificationWork);
    }
}
