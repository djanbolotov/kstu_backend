package com.schedule.kstu.controllerREST;

import com.schedule.kstu.DTO.AttendanceDTO;
import com.schedule.kstu.DTO.AttendanceForCreateOrEditDTO;
import com.schedule.kstu.DTO.AttendanceForTeacherDTO;
import com.schedule.kstu.DTO.DisciplineDTO;
import com.schedule.kstu.mapper.AttendanceRequest;
import com.schedule.kstu.mapper.AttendanceResponse;
import com.schedule.kstu.mapper.student.AttendancesWithDisciplines;
import com.schedule.kstu.mapper.student.AttendanceStatisticResponse;
import com.schedule.kstu.mapper.teacher.AttendanceListForTeacherResponse;
import com.schedule.kstu.mapper.teacher.AttendancesWithDisciplinesAndGroups;
import com.schedule.kstu.mapper.teacher.AttendancesWithSameDate;
import com.schedule.kstu.mapper.teacher.TeacherAttendanceStatisticResponse;
import com.schedule.kstu.model.*;
import com.schedule.kstu.service.*;
import com.schedule.kstu.service.auth.UserDetailsServiceImp;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceRESTController {
    private final AttendanceService attendanceService;
    private final StatementService statementService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final UserDetailsServiceImp userService;
    private final GroupService groupService;

    public AttendanceRESTController(AttendanceService attendanceService, StatementService statementService, StudentService studentService, TeacherService teacherService, UserDetailsServiceImp userService, GroupService groupService) {
        this.attendanceService = attendanceService;
        this.statementService = statementService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.userService = userService;
        this.groupService = groupService;
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

    @CrossOrigin
    @GetMapping("/student")
    public ResponseEntity<AttendanceStatisticResponse> getStudentAttendanceStatistics(Principal principal,
                                                                                      @RequestParam(required = false) String year,
                                                                                      @RequestParam(required = false) String semester) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        Student student = studentService.getStudentById(user.getStudent().getId());

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

        return ResponseEntity.ok(attendanceStatisticResponse);
    }


    @CrossOrigin
    @GetMapping("/teacher")
    public ResponseEntity<TeacherAttendanceStatisticResponse> getTeacherAttendanceStatistics(Principal principal,
                                                                                      @RequestParam(required = false) String year,
                                                                                      @RequestParam(required = false) String semester) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        Teacher teacher = teacherService.getTeacherById(user.getTeacher().getId());

        List<Statement> statementsAll = statementService.getStatementsForTeacher(teacher, null, null);

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

        TeacherAttendanceStatisticResponse attendanceStatisticResponse = new TeacherAttendanceStatisticResponse();
        AttendancesWithDisciplinesAndGroups attendancesWithDisciplines;
        List<AttendanceForTeacherDTO> attendanceDTOS;
        List<AttendancesWithDisciplinesAndGroups> attendancesWithDisciplinesList = new ArrayList<AttendancesWithDisciplinesAndGroups>();
        List<Attendance> attendances;
        for(Statement statement : statements) {
            List<Group> groupList = groupService.getGroupsByStatementId(statement.getId());
            for(Group group : groupList) {
                attendanceDTOS = new ArrayList<AttendanceForTeacherDTO>();
                attendancesWithDisciplines = new AttendancesWithDisciplinesAndGroups();
                attendances = attendanceService.getAttendanceByStatementId(statement.getId());
                attendancesWithDisciplines.setDiscipline(new DisciplineDTO(statement.getDiscipline().getId(), statement.getDiscipline().getName()));
                Integer countOfAttendance = 0;
                Integer countOfLessons = 0;
                if(!attendances.isEmpty()) {
                    for(Attendance attendance : attendances) {
                        if(Objects.equals(attendance.getStudent().getGroup().getId(), group.getId())) {
                            countOfLessons++;
                            attendanceDTOS.add(new AttendanceForTeacherDTO(attendance.getId(), attendance.getAttendanceStatus(), attendance.getStudent().getUser().getFirstName() + " " + attendance.getStudent().getUser().getLastName() ,attendance.getAttendanceDate()));
                            if(attendance.getAttendanceStatus().equals(AttendanceSTATUS.PRESENT) || attendance.getAttendanceStatus().equals(AttendanceSTATUS.LATE)){
                                countOfAttendance++;
                            }
                        }
                    }
                    attendancesWithDisciplines.setAttendances(attendanceDTOS);
                    attendancesWithDisciplines.setCountOfLessons(countOfLessons);
                    attendancesWithDisciplines.setCountOfAttendance(countOfAttendance);
                    attendancesWithDisciplines.setPercentageOfAttendance((double) (100.*countOfAttendance.doubleValue()/attendances.size()));
                }
                attendancesWithDisciplines.setGroupName(group.getName());
                attendancesWithDisciplines.setStatementId(statement.getId());
                attendancesWithDisciplinesList.add(attendancesWithDisciplines);
            }
        }
        attendanceStatisticResponse.setAttendancesWithDisciplines(attendancesWithDisciplinesList);
        attendanceStatisticResponse.setSelectedSemester(semester);
        attendanceStatisticResponse.setSelectedYear(year);
        attendanceStatisticResponse.setYears(sortedYearRanges);

        return ResponseEntity.ok(attendanceStatisticResponse);
    }

    @CrossOrigin
    @GetMapping("/teacher/list")
    public ResponseEntity<AttendanceListForTeacherResponse> getTeacherAttendanceList(Principal principal,
                                                                                     @RequestParam(required = false) String discipline,
                                                                                     @RequestParam(required = false) String group,
                                                                                     @RequestParam(required = false) String year,
                                                                                     @RequestParam(required = false) String semester) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        Teacher teacher = teacherService.getTeacherById(user.getTeacher().getId());

        List<Statement> statementsAll = statementService.getStatementsForTeacher(teacher, null, null);

        List<String> sortedYearRanges = statementsAll.stream().map(Statement::getYear).distinct().sorted(Comparator.comparingInt(y -> Integer.parseInt(y.split("-")[0]))).toList();
        boolean isYearCorrect = year != null && !year.trim().isEmpty();
        boolean isSemesterCorrect = semester != null && !semester.trim().isEmpty();
        boolean isDisciplineCorrect = discipline != null && !discipline.trim().isEmpty();
        boolean isGroupCorrect = group != null && !group.trim().isEmpty();
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
        List<String> disciplines = statements.stream()
                .map(Statement::getDiscipline)
                .distinct()
                .map(Discipline::getName)
                .distinct()
                .toList();
        String finalDiscipline1 = discipline;
        if(isDisciplineCorrect && !disciplines.stream().filter(g -> g.equals(finalDiscipline1)).toList().isEmpty()) {
            String finalDiscipline = discipline;
            statements = statements.stream().filter(statement -> statement.getDiscipline().getName().equals(finalDiscipline)).toList();
        } else{
            isDisciplineCorrect = false;
        }
        if(!isDisciplineCorrect && !statements.isEmpty()) {
            discipline = disciplines.get(0);
        }
        if(!isDisciplineCorrect && disciplines.isEmpty()) {
            discipline = null;
        }
        List<String> groups = statements.stream()
                .flatMap(statement -> statement.getStudents().stream())
                .map(student -> student.getGroup().getName())
                .distinct()
                .toList();
        AttendanceListForTeacherResponse attendanceListForTeacherResponse = new AttendanceListForTeacherResponse();
        List<AttendancesWithSameDate> withSameDateList = new ArrayList<>();
        List<AttendanceForCreateOrEditDTO> attendanceDTOS;
        List<Attendance> attendances;

        String finalGroup1 = group;
        if(group == null) {
            if(!groups.isEmpty()) {
                group = groups.get(0);
            }
        }
        if(group != null && !group.isEmpty() && groups.stream().filter(g -> g.equals(finalGroup1)).toList().isEmpty()) {
            if(!groups.isEmpty()) {
                group = groups.get(0);
            }
        }
        if(groups.isEmpty()) {
            group = null;
        }
        String finalGroup = group;
        for(Statement statement : statements) {
            attendances = attendanceService.getAttendanceByStatementId(statement.getId());

            attendances = attendances.stream().filter(attendance -> attendance.getStudent().getGroup().getName().equals(finalGroup)).toList();
            List<String> attendancesWithSameDate = attendances.stream().map(attendance -> attendance.getAttendanceDate().toString()).distinct().toList();
            for(String date : attendancesWithSameDate) {
                attendanceDTOS = new ArrayList<AttendanceForCreateOrEditDTO>();
                for(Attendance attendance : attendances) {
                        if(attendance.getAttendanceDate().toString().equals(date)){
                            attendanceDTOS.add(new AttendanceForCreateOrEditDTO(attendance.getId(), attendance.getAttendanceStatus(), attendance.getStudent().getId(),attendance.getStudent().getUser().getFirstName() + " " + attendance.getStudent().getUser().getLastName(), attendance.getStudent().getSubgroup(),attendance.getAttendanceDate()));
                        }
                }
                AttendancesWithSameDate withSameDate = new AttendancesWithSameDate(date, attendanceDTOS);
                withSameDateList.add(withSameDate);
            }

        }
        attendanceListForTeacherResponse.setAttendancesWithSameDates(withSameDateList);
        attendanceListForTeacherResponse.setSelectedSemester(semester);
        attendanceListForTeacherResponse.setSelectedYear(year);
        attendanceListForTeacherResponse.setYears(sortedYearRanges);
        attendanceListForTeacherResponse.setDisciplines(disciplines);
        attendanceListForTeacherResponse.setGroups(groups);
        attendanceListForTeacherResponse.setSelectedDiscipline(discipline);
        attendanceListForTeacherResponse.setSelectedGroup(group);
        if(!statements.isEmpty()){
            attendanceListForTeacherResponse.setStatementId(statements.get(0).getId());
        }else {
            attendanceListForTeacherResponse.setStatementId(null);
        }
        return ResponseEntity.ok(attendanceListForTeacherResponse);
    }

    @CrossOrigin
    @PostMapping("/save")
    public ResponseEntity<AttendanceResponse> saveAttendances(@RequestBody AttendanceRequest attendanceRequest) {
        LocalDateTime attendanceDate = LocalDateTime.parse(attendanceRequest.getDate());

        Statement statement = statementService.getStatementById(attendanceRequest.getStatementId());
        List<Attendance> attendances = new ArrayList<>();

        for(AttendanceForCreateOrEditDTO a : attendanceRequest.getAttendances()) {
            Attendance attendance = new Attendance();
            if(a.getId() != null) {
                attendance.setId(a.getId());
            }
            Integer studentId = a.getStudentId();
            Student student = studentService.getStudentById(studentId);

            attendance.setStudent(student);
            attendance.setStatement(statement);
            attendance.setAttendanceDate(attendanceDate);
            attendance.setAttendanceStatus(a.getAttendanceStatus());

            attendances.add(attendance);
        }

        attendanceService.createAttendances(attendances);
        AttendanceResponse attendanceResponse = new AttendanceResponse();
        List<AttendanceDTO> attendanceDTOS = attendances.stream()
                .map(attendance -> new AttendanceDTO(attendance.getId(), attendance.getAttendanceStatus(), attendance.getAttendanceDate())) // вручную преобразуем
                .toList();
        attendanceResponse.setAttendances(attendanceDTOS);
        return ResponseEntity.ok(attendanceResponse);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteAttendance(@PathVariable Integer id, @RequestParam String date) {
        List<Attendance> attendances = attendanceService.getAttendanceByStatementId(id);
        LocalDateTime attendanceDate = LocalDateTime.parse(date);
        attendances = attendances.stream().filter(attendance -> attendance.getAttendanceDate().equals(attendanceDate)).toList();
        for(Attendance attendance : attendances) {
            attendanceService.deleteAttendance(attendance.getId());
        }
        return ResponseEntity.ok("deleted");
    }

    @GetMapping("/create/student-list")
    public ResponseEntity<List<AttendanceForCreateOrEditDTO>> getStudentListForAttendance(@RequestParam String group, @RequestParam String statementId) {
        List<AttendanceForCreateOrEditDTO> attendances = new ArrayList<>();
        List<Student> students = studentService.getStudentsByStatement(Integer.parseInt(statementId));
        students = students.stream().filter(student -> student.getGroup().getName().equals(group)).toList();
        for(Student student : students) {
            AttendanceForCreateOrEditDTO attendance = new AttendanceForCreateOrEditDTO(null, AttendanceSTATUS.PRESENT, student.getId(), student.getUser().getFirstName() + " " + student.getUser().getLastName(), student.getSubgroup(), LocalDateTime.now());
            attendances.add(attendance);
        }
        return ResponseEntity.ok(attendances);
    }
}