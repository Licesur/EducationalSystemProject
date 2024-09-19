package ru.sova.educationapp.EducationalSystemApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sova.educationapp.EducationalSystemApp.mappers.TaskMapper;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.repositories.VerificationWorkRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class VerificationWorkService {

    private final VerificationWorkRepository verificationWorkRepository;

    @Autowired
    public VerificationWorkService(VerificationWorkRepository verificationWorkRepository) {
        this.verificationWorkRepository = verificationWorkRepository;
    }
    public List<VerificationWork> finAll(){
        return verificationWorkRepository.findAll();
    }

    public VerificationWork findById(int id){
        Optional<VerificationWork> foundVerificationWork = verificationWorkRepository.findById(id);

        return foundVerificationWork.orElse(null);
    }

    @Transactional(readOnly = false)
    public VerificationWork save(VerificationWork verificationWork){
        return verificationWorkRepository.save(verificationWork);
    }

    @Transactional(readOnly = false)
    public boolean deleteById(int id){
        verificationWorkRepository.deleteById(id);
        return !verificationWorkRepository.findById(id).isPresent();
    }

    @Transactional(readOnly = false)
    public boolean update(int id, VerificationWork verificationWork){
        verificationWork.setId(id);
        verificationWorkRepository.save(verificationWork); // соглашение - обновлять мтеодом сейв
        return verificationWorkRepository.findById(id).isPresent()
                && verificationWork.getTasks().toString()
                .equals(verificationWorkRepository.findById(id).get().getTasks().toString());
    }
    @Transactional(readOnly = false)
    public void addTasks(VerificationWork verificationWork, List<Task> tasks) {
        verificationWork.setTasks(tasks);
        verificationWorkRepository.save(verificationWork);
    }
}
