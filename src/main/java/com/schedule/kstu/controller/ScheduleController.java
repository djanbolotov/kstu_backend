package com.schedule.kstu.controller;

import com.schedule.kstu.model.*;
import com.schedule.kstu.service.ScheduleService;
import com.schedule.kstu.service.StatementService;
import com.schedule.kstu.service.StudentService;
import com.schedule.kstu.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private StatementService statementService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/statement/{id}")
    public String getScheduleByStatementId(@PathVariable Integer id, Model model) {
        List<Schedule> schedules = scheduleService.getSchedulesByStatementId(id);
        model.addAttribute("schedules", schedules);

        if (!schedules.isEmpty()) {
            model.addAttribute("discipline", schedules.get(0).getStatement().getDiscipline().getName());
            model.addAttribute("teacher", schedules.get(0).getStatement().getTeacher().getUser().getFirstName() + " " + schedules.get(0).getStatement().getTeacher().getUser().getLastName());
        }
        model.addAttribute("statementId", id);
        return "schedules/list";
    }

    @GetMapping("/statement/{id}/new")
    public String showCreateFormForStatement(@PathVariable Integer id, Model model) {
        Schedule newSchedule = new Schedule();
        Statement statement = statementService.getStatementById(id);
        List<Group> groups = statement.getStudents().stream()
                .map(Student::getGroup)
                .distinct()
                .toList();
        newSchedule.setStatement(statement);

        model.addAttribute("groups", groups);
        model.addAttribute("schedule", newSchedule);
        model.addAttribute("statementId", id);
        return "schedules/new";
    }

    @GetMapping("student/{id}")
    public String getScheduleForStudent(@PathVariable Integer id,
                                        @RequestParam(required = false) String year,
                                        @RequestParam(required = false) String semester,
                                        Model model) {
        Student student = studentService.getStudentById(id);
        List<Statement> statementsAll = statementService.getStatementsForStudent(student, null, null);

        List<String> sortedYearRanges = statementsAll.stream().map(Statement::getYear).distinct().sorted(Comparator.comparingInt(y -> Integer.parseInt(y.split("-")[0]))).toList();

        Set<String> sortedYearSet = new LinkedHashSet<>(sortedYearRanges);

        boolean isYearCorrect = year != null && !year.trim().isEmpty();
        boolean isSemesterCorrect = semester != null && !semester.trim().isEmpty();

        List<Schedule> schedules;

        if(isYearCorrect) {
            schedules = scheduleService.findSchedulesByStudentIdAndYearAndSemester(student.getId(), year, semester);
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
            schedules = scheduleService.findSchedulesByStudentIdAndYearAndSemester(student.getId(), year, semester);
        }
        model.addAttribute("schedules", schedules);
        model.addAttribute("years", sortedYearSet);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedSemester", semester);
        model.addAttribute("studentId", id);
        return "schedules/student-schedule";
    }

    @GetMapping("teacher/{id}")
    public String getScheduleForTeacher(@PathVariable Integer id,
                                        @RequestParam(required = false) String year,
                                        @RequestParam(required = false) String semester,
                                        Model model) {
        Teacher teacher = teacherService.getTeacherById(id);
        List<Statement> statementsAll = statementService.getStatementsForTeacher(teacher, null, null);

        List<String> sortedYearRanges = statementsAll.stream().map(Statement::getYear).distinct().sorted(Comparator.comparingInt(y -> Integer.parseInt(y.split("-")[0]))).toList();

        Set<String> sortedYearSet = new LinkedHashSet<>(sortedYearRanges);

        boolean isYearCorrect = year != null && !year.trim().isEmpty();
        boolean isSemesterCorrect = semester != null && !semester.trim().isEmpty();

        List<Schedule> schedules;

        if(isYearCorrect) {
            schedules = scheduleService.findSchedulesByTeacherIdAndYearAndSemester(teacher.getId(), year, semester);
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
            schedules = scheduleService.findSchedulesByTeacherIdAndYearAndSemester(teacher.getId(), year, semester);
        }
        model.addAttribute("schedules", schedules);
        model.addAttribute("years", sortedYearSet);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedSemester", semester);
        model.addAttribute("teacherId", id);
        return "schedules/teacher-schedule";
    }

    @PostMapping("/statement/{id}")
    public String createScheduleForStatement(@PathVariable Integer id, @ModelAttribute Schedule schedule) {
        Statement statement = statementService.getStatementById(id);
        schedule.setStatement(statement);
        scheduleService.createSchedule(statement.getId(), schedule.getDayOfWeek(), schedule.getStartTime(), schedule.getEndTime(), schedule.getGroup().getId());
        return "redirect:/schedules/statement/" + id;
    }

    @GetMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable Integer id) {
        Schedule schedule = scheduleService.getScheduleById(id).orElseThrow();
        scheduleService.deleteSchedule(id);
        return "redirect:/schedules/statement/" + schedule.getStatement().getId();
    }
}
