package com.schedule.kstu.controller;

import com.schedule.kstu.model.Discipline;
import com.schedule.kstu.service.DisciplineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/disciplines")
public class DisciplineController {
    private final DisciplineService disciplineService;

    public DisciplineController(DisciplineService disciplineService) {
        this.disciplineService = disciplineService;
    }

    @GetMapping("/")
    public String getAllDisciplines(Model model) {
        model.addAttribute("disciplines", disciplineService.getAllDisciplines());
        return "disciplines";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("discipline", new Discipline());
        return "add-discipline";
    }

    @PostMapping("/add")
    public String addDiscipline(@ModelAttribute Discipline discipline) {
        disciplineService.saveDiscipline(discipline);
        return "redirect:/disciplines/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Discipline discipline = disciplineService.getDisciplineById(id);
        if (discipline != null) {
            model.addAttribute("discipline", discipline);
            return "edit-discipline";
        } else {
            return "redirect:/disciplines/";
        }
    }

    @PostMapping("/edit")
    public String editDiscipline(@ModelAttribute Discipline discipline) {
        disciplineService.saveDiscipline(discipline);
        return "redirect:/disciplines/";
    }

    @GetMapping("/delete/{id}")
    public String deleteDiscipline(@PathVariable Integer id) {
        disciplineService.deleteDiscipline(id);
        return "redirect:/disciplines/";
    }
}
