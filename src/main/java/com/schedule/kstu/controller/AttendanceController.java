package com.schedule.kstu.controller;

import com.schedule.kstu.DTO.AttendanceDTO;
import com.schedule.kstu.DTO.DisciplineDTO;
import com.schedule.kstu.mapper.student.AttendancesWithDisciplines;
import com.schedule.kstu.mapper.student.AttendanceStatisticResponse;
import com.schedule.kstu.model.*;
import com.schedule.kstu.service.AttendanceService;
import com.schedule.kstu.service.StatementService;
import com.schedule.kstu.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/attendances")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private StatementService statementService;

    @Autowired
    private StudentService studentService;

    @GetMapping
    public String getAllAttendances(Model model) {
        model.addAttribute("attendances", attendanceService.getAllAttendances());
        return "attendances/list";
    }

    @GetMapping("/{id}")
    public String getAttendanceById(@PathVariable Integer id, Model model) {
        Optional<Attendance> attendance = attendanceService.getAttendanceById(id);
        if (attendance.isPresent()) {
            model.addAttribute("attendance", attendance.get());
            return "attendances/view";
        } else {
            throw new RuntimeException("Attendance not found with id " + id);
        }
    }

    @GetMapping("/create/statement/{id}")
    public String showCreateForm(@PathVariable Integer id,
                                 @RequestParam(required = false) String group,
                                 @RequestParam(required = false) Integer subgroup,
                                 Model model) {
        Statement statement = statementService.getStatementById(id);
        List<Student> students = studentService.getStudentsByStatement(id);

        List<Group> groups = statement.getStudents().stream()
                .map(Student::getGroup)
                .distinct()
                .toList();
        // Create a list of Attendance objects, one for each student


        if(group != null && !group.trim().isEmpty()) {
            students = students.stream().filter(student -> student.getGroup().getName().equals(group)).toList();
            System.out.println("Subgroup: " + subgroup);
            if(subgroup != null && !(subgroup < 1)) {
                students = students.stream().filter(student -> student.getSubgroup() == subgroup).toList();
            }
            List<Attendance> attendances = students.stream()
                    .map(student -> new Attendance(student, statement, null, null))
                    .collect(Collectors.toList());
            model.addAttribute("attendances", attendances);
            model.addAttribute("statement", statement);
            model.addAttribute("groups", groups);
            model.addAttribute("selectedGroup", group);
            model.addAttribute("selectedSubgroup", subgroup);
        } else {
            model.addAttribute("statement", statement);
            model.addAttribute("groups", groups);
        }

        return "attendances/new";
    }

    @GetMapping("/student/{id}")
    public String getStudentAttendanceStatistics(@PathVariable Integer id,
                                                 @RequestParam(required = false) String year,
                                                 @RequestParam(required = false) String semester,
                                                 Model model) {
        Student student = studentService.getStudentById(id);

        List<Statement> statementsAll = statementService.getStatementsForStudent(student, null, null);

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

        AttendanceStatisticResponse attendanceStatisticResponse = new AttendanceStatisticResponse();
        AttendancesWithDisciplines attendancesWithDisciplines;
        List<AttendancesWithDisciplines> attendancesWithDisciplinesList = new ArrayList<AttendancesWithDisciplines>();
        List<Attendance> attendances;
        List<AttendanceDTO> attendanceDTOS = new ArrayList<AttendanceDTO>();
        for(Statement statement : statements) {
            attendancesWithDisciplines = new AttendancesWithDisciplines();
            attendances = attendanceService.getAttendanceByStudentIdAndStatementId(student.getId(), statement.getId());
            attendancesWithDisciplines.setDiscipline(new DisciplineDTO(statement.getDiscipline().getId(), statement.getDiscipline().getName()));
            Integer countOfAttendance = 0;
            if(!attendances.isEmpty()) {
                for(Attendance attendance : attendances) {
                    attendanceDTOS.add(new AttendanceDTO(attendance.getId(), attendance.getAttendanceStatus(), attendance.getAttendanceDate()));
                    if(attendance.getAttendanceStatus().equals(AttendanceSTATUS.PRESENT) || attendance.getAttendanceStatus().equals(AttendanceSTATUS.LATE)){
                        countOfAttendance++;
                    }
                }
                attendancesWithDisciplines.setAttendances(attendanceDTOS);
                attendancesWithDisciplines.setCountOfLessons(attendances.size());
                attendancesWithDisciplines.setCountOfAttendance(countOfAttendance);
                attendancesWithDisciplines.setPercentageOfAttendance((double) (100.*countOfAttendance.doubleValue()/attendances.size()));
            }
            attendancesWithDisciplinesList.add(attendancesWithDisciplines);
        }
        attendanceStatisticResponse.setAttendancesWithDisciplines(attendancesWithDisciplinesList);
        attendanceStatisticResponse.setSelectedSemester(semester);
        attendanceStatisticResponse.setSelectedYear(year);
        attendanceStatisticResponse.setYears(sortedYearRanges);

        model.addAttribute("data", attendanceStatisticResponse);
        return "attendance/student-statistic";
    }

    @PostMapping("/create/statement/{id}")
    public String createAttendances(@PathVariable Integer id,
                                    @RequestParam("attendanceDate") String attendanceDateStr,
                                    @RequestParam Map<String, String> params) {
        LocalDateTime attendanceDate = LocalDateTime.parse(attendanceDateStr);

        Statement statement = statementService.getStatementById(id);
        List<Attendance> attendances = new ArrayList<>();

        int i = 0;
        while (params.containsKey("attendances[" + i + "].student.id")) {
            Attendance attendance = new Attendance();
            Integer studentId = Integer.valueOf(params.get("attendances[" + i + "].student.id"));
            Student student = studentService.getStudentById(studentId);

            attendance.setStudent(student);
            attendance.setStatement(statement);
            attendance.setAttendanceDate(attendanceDate);
            attendance.setAttendanceStatus(AttendanceSTATUS.valueOf(params.get("attendances[" + i + "].attendanceStatus")));

            attendances.add(attendance);
            i++;
        }

        attendanceService.createAttendances(attendances);
        return "redirect:/attendances";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<Attendance> attendance = attendanceService.getAttendanceById(id);
        if (attendance.isPresent()) {
            model.addAttribute("attendance", attendance.get());
            model.addAttribute("statements", statementService.getAllStatements());
            model.addAttribute("students", studentService.getAllStudents());
            return "attendances/edit";
        } else {
            throw new RuntimeException("Attendance not found with id " + id);
        }
    }

    @PostMapping("/update/{id}")
    public String updateAttendance(@PathVariable Integer id, @ModelAttribute Attendance attendance) {
        attendanceService.updateAttendance(id, attendance);
        return "redirect:/attendances";
    }

    @GetMapping("/delete/{id}")
    public String deleteAttendance(@PathVariable Integer id) {
        attendanceService.deleteAttendance(id);
        return "redirect:/attendances";
    }
}