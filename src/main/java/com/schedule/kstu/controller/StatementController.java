package com.schedule.kstu.controller;

import com.schedule.kstu.model.*;
import com.schedule.kstu.service.DisciplineService;
import com.schedule.kstu.service.StatementService;
import com.schedule.kstu.service.StudentService;
import com.schedule.kstu.service.TeacherService;
import com.schedule.kstu.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/statements")
public class StatementController {

    @Autowired
    private StatementService statementService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private DisciplineService disciplineService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private GroupService groupService;

    @GetMapping
    public String getAllStatements(Model model) {
        List<Statement> statements = statementService.getAllStatements();
        model.addAttribute("statements", statements);
        return "statement-list";
    }

    @GetMapping("/{id}")
    public String getStatementById(@PathVariable Integer id, Model model) {
        Statement statement = statementService.getStatementById(id);
        List<Group> groups = groupService.getAllGroups();
        model.addAttribute("statement", statement);
        model.addAttribute("groups", groups);
        model.addAttribute("filteredStudents", statement.getStudents());
        return "statement-detail";
    }


    @GetMapping("/add")
    public String showAddStatementForm(Model model) {
        List<Teacher> teachers = teacherService.getAllTeachers();
        List<Discipline> disciplines = disciplineService.getAllDisciplines();
        model.addAttribute("statement", new Statement());
        model.addAttribute("teachers", teachers);
        model.addAttribute("disciplines", disciplines);
        return "statement-form";
    }

    @GetMapping("/student/{studentId}/statements")
    public String getStudentStatements(@PathVariable Integer studentId,
                                       @RequestParam(required = false) String year,
                                       @RequestParam(required = false) String semester,
                                       Model model) {
        Student student = studentService.getStudentById(studentId);
        List<Statement> statementsAll = statementService.getStatementsForStudent(student, null, null);

        Set<String> years = statementsAll.stream().map(Statement::getYear).collect(Collectors.toSet());
        Set<String> semesters = statementsAll.stream().map(Statement::getSemester).collect(Collectors.toSet());

        List<Statement> statements = statementService.getStatementsForStudent(student, year, semester);
        model.addAttribute("statements", statements);
        model.addAttribute("years", years);
        model.addAttribute("selectedYear", year);
        model.addAttribute("semesters", semesters);
        model.addAttribute("selectedSemester", semester);
        model.addAttribute("studentId", studentId);
        return "student-statements";
    }

    @PostMapping("/add")
    public String addStatement(@ModelAttribute Statement statement) {
        statementService.saveStatement(statement);
        return "redirect:/statements";
    }

    @GetMapping("/edit/{id}")
    public String showEditStatementForm(@PathVariable Integer id, Model model) {
        Statement statement = statementService.getStatementById(id);
        List<Teacher> teachers = teacherService.getAllTeachers();
        List<Discipline> disciplines = disciplineService.getAllDisciplines();
        model.addAttribute("statement", statement);
        model.addAttribute("teachers", teachers);
        model.addAttribute("disciplines", disciplines);
        return "statement-form";
    }

    @PostMapping("/edit")
    public String updateStatement(@ModelAttribute Statement statement) {
        statementService.saveStatement(statement);
        return "redirect:/statements";
    }

    @GetMapping("/delete/{id}")
    public String deleteStatement(@PathVariable Integer id) {
        statementService.deleteStatement(id);
        return "redirect:/statements";
    }

    @GetMapping("/{id}/add-students")
    public String showAddStudentsForm(@PathVariable Integer id, @RequestParam(required = false) Integer groupId, Model model) {
        Statement statement = statementService.getStatementById(id);
        List<Group> groups = groupService.getAllGroups();
        List<Student> allStudents = studentService.getAllStudents();

        List<Student> students = allStudents.stream()
                .filter(student -> !statement.getStudents().contains(student))
                .collect(Collectors.toList());

        if (groupId != null) {
            students = students.stream()
                    .filter(student -> student.getGroup().getId().equals(groupId))
                    .collect(Collectors.toList());
        }

        model.addAttribute("statement", statement);
        model.addAttribute("groups", groups);
        model.addAttribute("students", students);
        model.addAttribute("selectedGroup", groupId);
        return "add-students-to-statement";
    }

    @PostMapping("/{id}/add-students")
    public String addStudentsToStatement(@PathVariable Integer id, @RequestParam List<Integer> studentIds) {
        statementService.addStudentsToStatement(id, studentIds);
        return "redirect:/statements/" + id;
    }


    @GetMapping("/{id}/filter")
    public String filterStudentsByGroup(@PathVariable Integer id, @RequestParam(required = false) Integer groupId, Model model) {
        Statement statement = statementService.getStatementById(id);
        List<Group> groups = groupService.getAllGroups();
        List<Student> filteredStudents;
        if (groupId != null) {
            filteredStudents = statement.getStudents().stream()
                    .filter(student -> student.getGroup().getId().equals(groupId))
                    .collect(Collectors.toList());
        }else {
            filteredStudents = statement.getStudents();
        }

        model.addAttribute("statement", statement);
        model.addAttribute("groups", groups);
        model.addAttribute("filteredStudents", filteredStudents);
        model.addAttribute("selectedGroup", groupId);
        return "statement-detail";
    }
}
