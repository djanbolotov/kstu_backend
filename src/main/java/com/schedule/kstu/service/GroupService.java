package com.schedule.kstu.service;

import com.schedule.kstu.model.Group;
import com.schedule.kstu.model.Student;
import com.schedule.kstu.model.User;
import com.schedule.kstu.repository.GroupRepository;
import com.schedule.kstu.repository.StudentRepository;
import com.schedule.kstu.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    private final StudentRepository studentRepository;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group getGroupById(Integer id) {
        return groupRepository.findById(id).orElse(null);
    }

    public void addGroup(Group group) {
        groupRepository.save(group);
    }

    public void addStudentToGroup(Integer groupId, Integer studentId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Invalid group Id:" + groupId));
        User user = userRepository.findById(studentId).orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + studentId));

        Student student = studentRepository.findStudentByUserId(user.getId());
        System.out.println("STUDENT: " + student);
        if (student == null) {
            throw new IllegalArgumentException("User is not a student");
        }

        student.setGroup(group);
        studentRepository.save(student);
        group.getStudents().add(student);
        groupRepository.save(group);
    }

    public List<Group> getGroupsByStatementId(Integer statementId) {
        return groupRepository.findGroupsByStatementId(statementId);
    }

    public void editGroup(Group group) {
        groupRepository.save(group);
    }

    public void deleteGroup(Integer id) {
        groupRepository.deleteById(id);
    }

    public Group findById(Integer groupId) {
        return groupRepository.findById(groupId).orElseThrow();
    }
}