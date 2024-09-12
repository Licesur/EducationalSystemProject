package ru.sova.educationapp.EducationalSystemApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
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

    public List<Task> finAll(){
        return taskRepository.findAll();
    }

    public Task findById(int id){
        Optional<Task> foundTask = taskRepository.findById(id);

        return foundTask.orElse(null);
    }

    @Transactional(readOnly = false)
    public Task save(Task task){
        return taskRepository.save(task);
    }

    @Transactional(readOnly = false)
    public void deleteById(int id){
        taskRepository.deleteById(id);
    }

    @Transactional(readOnly = false)
    public void update(int id, Task task){
        task.setId(id);
        taskRepository.save(task); // соглашение - обновлять мтеодом сейв
    }

    public List<Task> findByVerificationWork(VerificationWork verificationWork) {
        return taskRepository.findAllByVerificationWorksContains(verificationWork);
    }
}
