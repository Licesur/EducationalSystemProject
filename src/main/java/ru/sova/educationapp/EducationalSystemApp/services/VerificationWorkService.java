package ru.sova.educationapp.EducationalSystemApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public List<VerificationWork> findAll() {
        return verificationWorkRepository.findAll();
    }

    public VerificationWork findById(long id) {
        Optional<VerificationWork> foundVerificationWork = verificationWorkRepository.findById(id);

        return foundVerificationWork.orElse(null);
    }

    @Transactional(readOnly = false)
    public VerificationWork save(VerificationWork verificationWork) {
        return verificationWorkRepository.save(verificationWork);
    }

    @Transactional(readOnly = false)
    public boolean deleteById(long id) {
        verificationWorkRepository.deleteById(id);
        return !verificationWorkRepository.findById(id).isPresent();
    }

    @Transactional(readOnly = false)
    public boolean update(long id, VerificationWork verificationWork) {
        verificationWork.setId(id);
        verificationWorkRepository.save(verificationWork);
        return verificationWorkRepository.findById(id).isPresent()
                && verificationWork.equals(verificationWorkRepository.findById(id).get());
    }

    @Transactional(readOnly = false)
    public void fillTasks(VerificationWork verificationWork, List<Task> tasks) {
        verificationWork.setTasks(tasks);
        verificationWorkRepository.save(verificationWork);
    }
}
