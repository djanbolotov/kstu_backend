package com.schedule.kstu.controllerREST;

import com.schedule.kstu.DTO.DisciplineDTO;
import com.schedule.kstu.mapper.student.StudentStatementResponse;
import com.schedule.kstu.mapper.teacher.TeacherStatementResponse;
import com.schedule.kstu.model.Statement;
import com.schedule.kstu.model.Student;
import com.schedule.kstu.model.Teacher;
import com.schedule.kstu.model.User;
import com.schedule.kstu.service.StatementService;
import com.schedule.kstu.service.auth.UserDetailsServiceImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class StatementRESTController {
    private final StatementService statementService;

    private final UserDetailsServiceImp userService;

    public StatementRESTController(StatementService statementService, UserDetailsServiceImp userService) {
        this.statementService = statementService;
        this.userService = userService;
    }

    @CrossOrigin
    @GetMapping("/statements/student")
    public ResponseEntity<StudentStatementResponse> getStudentStatements(Principal principal,
                                                                         @RequestParam(required = false) String year,
                                                                         @RequestParam(required = false) String semester) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        Student student = user.getStudent();

        List<Statement> statementsAll = statementService.getStatementsForStudent(student, null, null);

        Set<String> years = statementsAll.stream().map(Statement::getYear).collect(Collectors.toSet());
        Set<String> semesters = statementsAll.stream().map(Statement::getSemester).collect(Collectors.toSet());
        List<String> sortedYearRanges = statementsAll.stream().map(Statement::getYear).distinct().sorted(Comparator.comparingInt(y -> Integer.parseInt(y.split("-")[0]))).toList();
        boolean isYearCorrect = year != null && !year.trim().isEmpty();
        boolean isSemesterCorrect = semester != null && !semester.trim().isEmpty();
        List<Statement> statements;

        if(isYearCorrect) {
            statements = statementService.getStatementsForStudent(student, year, semester);
        } else {
            year = sortedYearRanges.get(sortedYearRanges.size() - 1);
            Date thisDate = new Date();
            int thisMonth = thisDate.getMonth();
            if (!isSemesterCorrect) {
                if(thisMonth > 7) {
                    semester = "Осенний семестр";
                } else {
                    semester = "Весенний семестр";
                }
            }
            statements = statementService.getStatementsForStudent(student, year, semester);
        }

        List<DisciplineDTO> disciplines = statements.stream()
                .map(Statement::getDiscipline)
                .distinct()
                .map(d -> new DisciplineDTO(d.getId(), d.getName()))
                .collect(Collectors.toList());

        StudentStatementResponse response = new StudentStatementResponse();
        response.setFirstName(student.getUser().getFirstName());
        response.setLastName(student.getUser().getLastName());
        response.setGroup(student.getGroup().getName());
        response.setYears(new ArrayList<>(years));
        response.setSelectedSemester(semester);
        response.setSelectedYear(year);
        response.setDisciplines(disciplines);

        return ResponseEntity.ok(response);
    }

    @CrossOrigin
    @GetMapping("/statements/teacher")
    public ResponseEntity<TeacherStatementResponse> getTeacherStatements(Principal principal,
                                                                         @RequestParam(required = false) String year,
                                                                         @RequestParam(required = false) String semester) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        Teacher teacher = user.getTeacher();

        List<Statement> statementsAll = statementService.getStatementsForTeacher(teacher, null, null);

        Set<String> years = statementsAll.stream().map(Statement::getYear).collect(Collectors.toSet());
        Set<String> semesters = statementsAll.stream().map(Statement::getSemester).collect(Collectors.toSet());
        List<String> sortedYearRanges = statementsAll.stream().map(Statement::getYear).distinct().sorted(Comparator.comparingInt(y -> Integer.parseInt(y.split("-")[0]))).toList();
        boolean isYearCorrect = year != null && !year.trim().isEmpty();
        boolean isSemesterCorrect = semester != null && !semester.trim().isEmpty();
        List<Statement> statements;

        if(isYearCorrect) {
            statements = statementService.getStatementsForTeacher(teacher, year, semester);
        } else {
            year = sortedYearRanges.get(sortedYearRanges.size() - 1);
            Date thisDate = new Date();
            int thisMonth = thisDate.getMonth();
            if (!isSemesterCorrect) {
                if(thisMonth > 7) {
                    semester = "Осенний семестр";
                } else {
                    semester = "Весенний семестр";
                }
            }
            statements = statementService.getStatementsForTeacher(teacher, year, semester);
        }

        // Убедимся, что отправляем только необходимые данные
        List<DisciplineDTO> disciplines = statements.stream()
                .map(Statement::getDiscipline)
                .distinct()
                .map(d -> new DisciplineDTO(d.getId(), d.getName()))
                .collect(Collectors.toList());

        TeacherStatementResponse response = new TeacherStatementResponse();
        response.setFirstName(teacher.getUser().getFirstName());
        response.setLastName(teacher.getUser().getLastName());
        response.setYears(new ArrayList<>(years));
        response.setSelectedSemester(semester);
        response.setSelectedYear(year);
        response.setDisciplines(disciplines);

        return ResponseEntity.ok(response);
    }

}
