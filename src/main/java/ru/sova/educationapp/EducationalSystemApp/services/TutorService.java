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

@Service
@Transactional(readOnly = true)
public class TutorService {

    private final TutorRepository tutorRepository;
    private final StudentService studentService;

    @Autowired
    public TutorService(TutorRepository tutorRepository, StudentService studentService) {
        this.tutorRepository = tutorRepository;
        this.studentService = studentService;
    }
    public List<Tutor> finAll(){
        return tutorRepository.findAll();
    }

    public Tutor findById(int id){
        Optional<Tutor> foundBook = tutorRepository.findById(id);

        return foundBook.orElse(null);
    }

    @Transactional(readOnly = false)
    public Tutor save(Tutor tutor){
        return tutorRepository.save(tutor);
    }

    @Transactional(readOnly = false)
    public Boolean deleteById(int id){
        tutorRepository.deleteById(id);
        return !tutorRepository.findById(id).isPresent();
    }

    @Transactional(readOnly = false)
    public boolean update(int id, Tutor tutor){
        Tutor tutorToBeUpdated = tutorRepository.findById(id).get();
        tutor.setId(id);
        //двухфакторная аунтификация!
        System.out.println(tutor.getStudents());
        tutorToBeUpdated.setStudents(tutor.getStudents());
        tutorRepository.save(tutor);
        return tutorRepository.findById(id).isPresent() &&
                tutorRepository.findById(id).get().toString()
                        .equals(tutor.toString());// соглашение - обновлять мтеодом сейв
    }

    @Transactional(readOnly = false)
    public Boolean addStudent(Student student, Tutor tutor){
        tutor = tutorRepository.findById(tutor.getId()).get();
        if (tutor.getStudents() == null){
            tutor.setStudents(Collections.singletonList(student));
        } else if (!tutor.getStudents().contains(student)) {
            tutor.getStudents().add(student);
        }
        if (student.getTutors() == null){
            student.setTutors(Collections.singletonList(tutor));
        } else if (!student.getTutors().contains(tutor)) {
            student.getTutors().add(tutor);
        }
        studentService.save(student);
        tutorRepository.save(tutor);
        return tutor.getStudents().contains(student);
    }
    @Transactional(readOnly = false)
    public Boolean excludeStudent(Student student, Tutor tutor) {
        tutor.getStudents().remove(student);
        student.getTutors().remove(tutor);
        studentService.save(student);
        tutorRepository.save(tutor);
        return !tutor.getStudents().contains(student);
    }
}
