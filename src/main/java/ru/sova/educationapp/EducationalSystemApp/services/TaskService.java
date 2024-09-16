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
    public Boolean deleteById(int id){
        taskRepository.deleteById(id);
        return !taskRepository.findById(id).isPresent();
    }

    @Transactional(readOnly = false)
    public Boolean update(int id, Task task){
        task.setId(id);
        taskRepository.save(task); // соглашение - обновлять мтеодом сейв
        return taskRepository.findById(id).isPresent() &&
                taskRepository.findById(id).get().toString()
                        .equals(task.toString());// соглашение - обновлять мтеодом сейв

    }

    public List<Task> findByVerificationWork(VerificationWork verificationWork) {
        return taskRepository.findAllByVerificationWorksContains(verificationWork);
    }
}
