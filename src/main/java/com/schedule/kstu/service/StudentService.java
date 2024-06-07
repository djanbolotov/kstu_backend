package com.schedule.kstu.service;

import com.schedule.kstu.model.Group;
import com.schedule.kstu.model.Student;
import com.schedule.kstu.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Integer id) {
        return studentRepository.findById(id).orElse(null);
    }
    public List<Student> getStudentsByStatement(Integer id) {
        return studentRepository.findStudentsByStatementsContainingAndId(id);
    }

    public List<Student> getStudentsByGroupId(Integer id) {
        return studentRepository.findStudentsByGroupId(id);
    }

    public void updateStudents(List<Student> students) {
        studentRepository.saveAll(students);
    }

    public List<Student> getStudentsByGroup(Group group) {
        return studentRepository.findStudentsByGroupId(group.getId());
    }

    // Метод для изменения подгруппы студента
    public void updateSubgroup(Integer studentId, Integer newSubgroup) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setSubgroup(newSubgroup);
            studentRepository.save(student);
        } else {
            // Обработка случая, если студент не найден
        }
    }
}
