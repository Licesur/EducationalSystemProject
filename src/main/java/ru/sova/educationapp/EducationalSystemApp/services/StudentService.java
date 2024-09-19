package ru.sova.educationapp.EducationalSystemApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;
import ru.sova.educationapp.EducationalSystemApp.repositories.StudentRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final VerificationWorkService verificationWorkService;

    @Autowired
    public StudentService(StudentRepository studentRepository, VerificationWorkService verificationWorkService) {
        this.studentRepository = studentRepository;
        this.verificationWorkService = verificationWorkService;
    }

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

    public List<Student> finAll(){
        return studentRepository.findAll();
    }

    public Student findById(int id){
        Optional<Student> foundPerson = studentRepository.findById(id);

        return foundPerson.orElse(null);
    }

    @Transactional(readOnly = false)
    public Student save(Student student){
        return studentRepository.save(student);
    }

    @Transactional(readOnly = false)
    public Boolean deleteById(int id){
        studentRepository.deleteById(id);
        return !studentRepository.findById(id).isPresent();
    }

    @Transactional(readOnly = false)
    public Boolean update(int id, Student student){
        student.setId(id);
        studentRepository.save(student);
        //todo во имя света! реализуй здесь что-то человеческое а не это говно!
        return studentRepository.findById(id).isPresent() && studentRepository.findById(id).get().toString().equals(student.toString());// соглашение - обновлять мтеодом сейв
    }

    public List<Student> findByVerificationWork(VerificationWork verificationWork) {
        List<Student> students = studentRepository.findAllByVerificationWorksContains(verificationWork);
        return students == null ? Collections.emptyList() : students;
    }

    public List<Student> findByVerificationWorkNotContains(VerificationWork verificationWork) {
        List<Student> validStudents = studentRepository.findAllByVerificationWorksNotContains(verificationWork);
        return validStudents == null ? Collections.emptyList() : validStudents;
    }

    public List<Student> findByTutorsNotContains(Tutor tutor) {
        return studentRepository.findByTutorsNotContains(tutor);
    }

    @Transactional(readOnly = false)
    public void excludeWork(Student student, VerificationWork verificationWork) {
        student.getVerificationWorks().remove(verificationWork);
        verificationWork.getStudents().remove(student);
        studentRepository.save(student);
        verificationWorkService.save(verificationWork);
    }
}
