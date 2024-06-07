package com.schedule.kstu.service;

import com.schedule.kstu.model.Statement;
import com.schedule.kstu.model.Student;
import com.schedule.kstu.model.Teacher;
import com.schedule.kstu.repository.StatementRepository;
import com.schedule.kstu.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatementService {

    @Autowired
    private StatementRepository statementRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<Statement> getAllStatements() {
        return statementRepository.findAll();
    }

    public Statement getStatementById(Integer id) {
        return statementRepository.findById(id).orElse(null);
    }

    public Statement saveStatement(Statement statement) {
        return statementRepository.save(statement);
    }

    public void deleteStatement(Integer id) {
        statementRepository.deleteById(id);
    }

    public List<Statement> getStatementsForStudent(Student student, String year, String semester) {
        boolean isYearCorrect = year != null && !year.trim().isEmpty();
        boolean isSemesterCorrect = semester != null && !semester.trim().isEmpty();
        if (isYearCorrect && isSemesterCorrect) {
            return statementRepository.findByStudentsContainsAndYearAndSemester(student.getId(), year, semester);
        } else if (isYearCorrect) {
            return statementRepository.findByStudentsContainsAndYear(student.getId(), year);
        } else if (isSemesterCorrect) {
            return statementRepository.findByStudentsContainsAndSemester(student.getId(), semester);
        } else {
            return statementRepository.findByStudentsContains(student.getId());
        }
    }

    public List<Statement> getStatementsForTeacher(Teacher teacher, String year, String semester) {
        boolean isYearCorrect = year != null && !year.trim().isEmpty();
        boolean isSemesterCorrect = semester != null && !semester.trim().isEmpty();
        if (isYearCorrect && isSemesterCorrect) {
            return statementRepository.findByTeacherContainsAndYearAndSemester(teacher.getId(), year, semester);
        } else if (isYearCorrect) {
            return statementRepository.findByTeacherContainsAndYear(teacher.getId(), year);
        } else if (isSemesterCorrect) {
            return statementRepository.findByTeacherContainsAndSemester(teacher.getId(), semester);
        } else {
            return statementRepository.findByTeacherContains(teacher.getId());
        }
    }

    public void addStudentsToStatement(Integer statementId, List<Integer> studentIds) {
        Statement statement = getStatementById(statementId);
        if (statement != null) {
            List<Student> students = studentRepository.findAllById(studentIds);
            statement.getStudents().addAll(students);
            statementRepository.save(statement);
        }
    }
}
