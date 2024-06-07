package com.schedule.kstu.controller;

import com.schedule.kstu.model.Group;
import com.schedule.kstu.model.Role;
import com.schedule.kstu.model.User;
import com.schedule.kstu.service.auth.UserDetailsServiceImp;
import com.schedule.kstu.service.GroupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;
    private final UserDetailsServiceImp userService;

    public GroupController(GroupService groupService, UserDetailsServiceImp userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String getAllGroups(Model model) {
        List<Group> groups = groupService.getAllGroups();
        model.addAttribute("groups", groups);
        return "groups";
    }

    @GetMapping("/{id}")
    public String showGroupStudents(@PathVariable Integer id, Model model) {
        Group group = groupService.getGroupById(id);
        model.addAttribute("group", group);
        return "group-students";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("group", new Group());
        return "add-group";
    }

    @GetMapping("/add-student/{id}")
    public String showAddStudentForm(@PathVariable Integer id, Model model) {
        Group group = groupService.getGroupById(id);
        List<User> students = userService.getUsersByRole(Role.STUDENT).orElse(null);
        students = students.stream()
                .filter(student -> student.getStudent().getGroup() == null)
                .collect(Collectors.toList());
        model.addAttribute("group", group);
        model.addAttribute("students", students);
        return "add-student-to-group";
    }

    @PostMapping("/add-students/{id}")
    public String addStudentsToGroup(@PathVariable Integer id, @RequestParam("studentIds") List<Integer> studentIds) {
        for (Integer studentId : studentIds) {
            groupService.addStudentToGroup(id, studentId);
        }
        return "redirect:/groups/" + id;
    }

    @PostMapping("/add")
    public String addGroup(@ModelAttribute Group group) {
        groupService.addGroup(group);
        return "redirect:/groups/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Group group = groupService.getGroupById(id);
        model.addAttribute("group", group);
        return "edit-group";
    }

    @PostMapping("/edit")
    public String editGroup(@ModelAttribute Group group) {
        groupService.editGroup(group);
        return "redirect:/groups/";
    }

    @GetMapping("/delete/{id}")
    public String deleteGroup(@PathVariable("id") Integer id) {
        groupService.deleteGroup(id);
        return "redirect:/groups/";
    }
}
