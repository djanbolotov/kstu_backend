package com.schedule.kstu.service;

import com.schedule.kstu.model.*;
import com.schedule.kstu.repository.GroupRepository;
import com.schedule.kstu.repository.ScheduleRepository;
import com.schedule.kstu.repository.StatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private StatementRepository statementRepository;

    @Autowired
    private GroupRepository groupRepository;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Optional<Schedule> getScheduleById(Integer id) {
        return scheduleRepository.findById(id);
    }

    public List<Schedule> getSchedulesByStatementId(Integer id) {
        return scheduleRepository.findByStatementId(id);
    }

    public Schedule createSchedule(Integer statementId, DaysOfWeek dayOfWeek, String startTime, String endTime, Integer groupId) {
        Optional<Statement> statement = statementRepository.findById(statementId);
        Optional<Group> group = groupRepository.findById(groupId);
        if (statement.isPresent() && group.isPresent()) {
            Schedule schedule = new Schedule(statement.get(), group.get(), dayOfWeek, startTime, endTime);
            return scheduleRepository.save(schedule);
        } else if(statement.isEmpty()) {
            throw new RuntimeException("Statement not found with id " + statementId);
        } else {
            throw new RuntimeException("Group not found with id " + groupId);
        }
    }

    public Schedule updateSchedule(Integer id, DaysOfWeek dayOfWeek, String startTime, String endTime, Integer groupId) {
        Optional<Schedule> scheduleOptional = scheduleRepository.findById(id);
        Optional<Group> group = groupRepository.findById(groupId);
        if (scheduleOptional.isPresent() && group.isPresent()) {
            Schedule schedule = scheduleOptional.get();
            schedule.setDayOfWeek(dayOfWeek);
            schedule.setStartTime(startTime);
            schedule.setEndTime(endTime);
            schedule.setGroup(group.get());
            return scheduleRepository.save(schedule);
        } else {
            throw new RuntimeException("Schedule not found with id " + id);
        }
    }

    public List<Schedule> findSchedulesByTeacherIdAndYearAndSemester(Integer teacherId,String year, String semester) {
        boolean isYearCorrect = year != null && !year.trim().isEmpty();
        boolean isSemesterCorrect = semester != null && !semester.trim().isEmpty();
        if (isYearCorrect && isSemesterCorrect) {
            return scheduleRepository.findSchedulesByTeacherIdAndYearAndSemester(teacherId, year, semester);
        } else if (isYearCorrect) {
            return scheduleRepository.findSchedulesByTeacherIdAndYear(teacherId, year);
        } else if (isSemesterCorrect) {
            return scheduleRepository.findSchedulesByTeacherIdAndSemester(teacherId, semester);
        } else {
            return scheduleRepository.findSchedulesByTeacherId(teacherId);
        }
    }

    public List<Schedule> findSchedulesByStudentIdAndYearAndSemester(Integer studentId, String year, String semester) {
        boolean isYearCorrect = year != null && !year.trim().isEmpty();
        boolean isSemesterCorrect = semester != null && !semester.trim().isEmpty();
        if (isYearCorrect && isSemesterCorrect) {
            return scheduleRepository.findSchedulesByStudentIdAndYearAndSemester(studentId, year, semester);
        } else if (isYearCorrect) {
            return scheduleRepository.findSchedulesByStudentIdAndYear(studentId, year);
        } else if (isSemesterCorrect) {
            return scheduleRepository.findSchedulesByStudentIdAndSemester(studentId, semester);
        } else {
            return scheduleRepository.findSchedulesByStudentId(studentId);
        }
    }


    public void deleteSchedule(Integer id) {
        scheduleRepository.deleteById(id);
    }
}
