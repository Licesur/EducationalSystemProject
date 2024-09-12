package ru.sova.educationapp.EducationalSystemApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
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
    public void addVerificationWork(VerificationWork verificationWork, Student student) {
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
    public void deleteById(int id){
        studentRepository.deleteById(id);
    }

    @Transactional(readOnly = false)
    public void update(int id, Student student){
        student.setId(id);
        studentRepository.save(student); // соглашение - обновлять мтеодом сейв
    }

    public List<Student> findByVerificationWork(VerificationWork verificationWork) {
        List<Student> students = studentRepository.findAllByVerificationWorksContains(verificationWork);
        return students == null ? Collections.emptyList() : students;
    }

    public List<Student> findByVerificationWorkNotContains(VerificationWork verificationWork) {
        List<Student> validStudents = studentRepository.findAllByVerificationWorksNotContains(verificationWork);
        return validStudents == null ? Collections.emptyList() : validStudents;
    }

//    public List<Book> getBooksByPersonId(int id) {
//        Optional<Person> person = personRepository.findById(id);
//        if (person.isPresent()) {
//            Hibernate.initialize(person.get().getBooks());
//            person.get().getBooks().forEach(book -> {
//                long daysDiff = LocalDate.now().getDayOfYear() - book.getGetDay().getDayOfYear();
//                book.setTimeToGetBack(daysDiff > 10);
//            });
//            return person.get().getBooks();
//        } else {
//            return Collections.emptyList();
//        }
//    }
}
