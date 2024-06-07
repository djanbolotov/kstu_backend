package com.schedule.kstu.controller;

import com.schedule.kstu.model.Group;
import com.schedule.kstu.model.Student;
import com.schedule.kstu.service.GroupService;
import com.schedule.kstu.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private GroupService groupService;

    // Метод для отображения списка студентов определенной группы с возможностью изменения подгруппы
    @GetMapping("/students/{groupId}")
    public String showStudentsWithSubgroups(@PathVariable("groupId") Integer groupId, Model model) {
        Group group = groupService.findById(groupId);
        List<Student> students = studentService.getStudentsByGroup(group);
        model.addAttribute("students", students);
        model.addAttribute("groupId", groupId); // Добавляем groupId в модель
        return "students_with_subgroups";
    }

    // Метод для обновления подгруппы студентов
    @PostMapping("/students/updateSubgroups")
    public String updateSubgroups(@RequestParam("studentIds") List<Integer> studentIds,
                                  @RequestParam("newSubgroups") List<Integer> newSubgroups,
                                  @RequestParam("groupId") Integer groupId) {
        for (int i = 0; i < studentIds.size(); i++) {
            Integer studentId = studentIds.get(i);
            Integer newSubgroup = newSubgroups.get(i);
            studentService.updateSubgroup(studentId, newSubgroup);
        }
        return "redirect:/groups/" + groupId;
    }
}
