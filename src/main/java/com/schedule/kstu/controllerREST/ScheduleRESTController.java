package com.schedule.kstu.controllerREST;

import com.schedule.kstu.DTO.DisciplineDTO;
import com.schedule.kstu.DTO.ScheduleDTO;
import com.schedule.kstu.mapper.ScheduleResponse;
import com.schedule.kstu.model.*;
import com.schedule.kstu.service.ScheduleService;
import com.schedule.kstu.service.StatementService;
import com.schedule.kstu.service.auth.UserDetailsServiceImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ScheduleRESTController {

    private final ScheduleService scheduleService;

    private final StatementService statementService;

    private final UserDetailsServiceImp userService;

    public ScheduleRESTController(ScheduleService scheduleService, StatementService statementService, UserDetailsServiceImp userService) {
        this.scheduleService = scheduleService;
        this.statementService = statementService;
        this.userService = userService;
    }

    @CrossOrigin
    @GetMapping("/schedule/student")
    public ResponseEntity<ScheduleResponse> getScheduleForStudent(Principal principal,
                                                                  @RequestParam(required = false) String year,
                                                                  @RequestParam(required = false) String semester) {

        ScheduleResponse scheduleResponse = new ScheduleResponse();
        User user = (User) userService.loadUserByUsername(principal.getName());
        Student student = user.getStudent();

        List<Statement> statementsAll = statementService.getStatementsForStudent(student, null, null);
        List<String> sortedYearRanges = statementsAll.stream().map(Statement::getYear).distinct().sorted(Comparator.comparingInt(y -> Integer.parseInt(y.split("-")[0]))).toList();

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
        List<Schedule> sortedSchedule = schedules.stream()
                .distinct()
                .sorted(Comparator.comparingInt(schedule -> Integer.parseInt(schedule.getStartTime().replace(":", "")))).toList();
        List<ScheduleDTO> scheduleDTOList = sortedSchedule.stream()
                        .map(s -> new ScheduleDTO(s.getId(), s.getStatement().getTeacher().getUser().getFirstName(),
                                s.getStartTime(), s.getEndTime(),
                                new DisciplineDTO(s.getStatement().getDiscipline().getId(), s.getStatement().getDiscipline().getName()) ,s.getGroup().getName(), s.getDayOfWeek())).toList();
        scheduleResponse.setYears(sortedYearRanges);
        scheduleResponse.setDaysOfWeek(Arrays.stream(DaysOfWeek.values()).toList());
        scheduleResponse.setSelectedSemester(semester);
        scheduleResponse.setSelectedYear(year);
        scheduleResponse.setSchedules(scheduleDTOList);
        return ResponseEntity.ok(scheduleResponse);
    }

    @CrossOrigin
    @GetMapping("/schedule/teacher")
    public ResponseEntity<ScheduleResponse> getScheduleForTeacher(Principal principal,
                                        @RequestParam(required = false) String year,
                                        @RequestParam(required = false) String semester) {

        ScheduleResponse scheduleResponse = new ScheduleResponse();
        User user = (User) userService.loadUserByUsername(principal.getName());
        Teacher teacher = user.getTeacher();

        List<Statement> statementsAll = statementService.getStatementsForTeacher(teacher, null, null);
        List<String> sortedYearRanges = statementsAll.stream().map(Statement::getYear).distinct().sorted(Comparator.comparingInt(y -> Integer.parseInt(y.split("-")[0]))).toList();

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
        List<Schedule> sortedSchedule = schedules.stream()
                .distinct()
                .sorted(Comparator.comparingInt(schedule -> Integer.parseInt(schedule.getStartTime().replace(":", "")))).toList();

        List<ScheduleDTO> scheduleDTOList = sortedSchedule.stream()
                .map(s -> new ScheduleDTO(s.getId(), s.getStatement().getTeacher().getUser().getFirstName(),
                        s.getStartTime(), s.getEndTime(),
                        new DisciplineDTO(s.getStatement().getDiscipline().getId(), s.getStatement().getDiscipline().getName()) ,s.getGroup().getName(), s.getDayOfWeek())).toList();
        scheduleResponse.setYears(sortedYearRanges);
        scheduleResponse.setDaysOfWeek(Arrays.stream(DaysOfWeek.values()).toList());
        scheduleResponse.setSelectedSemester(semester);
        scheduleResponse.setSelectedYear(year);
        scheduleResponse.setSchedules(scheduleDTOList);
        return ResponseEntity.ok(scheduleResponse);
    }
}
