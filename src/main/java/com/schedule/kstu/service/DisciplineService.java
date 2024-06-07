package com.schedule.kstu.service;

import com.schedule.kstu.model.Discipline;
import com.schedule.kstu.repository.DisciplineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplineService {
    private final DisciplineRepository disciplineRepository;

    public DisciplineService(DisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
    }

    public List<Discipline> getAllDisciplines() {
        return disciplineRepository.findAll();
    }

    public Discipline getDisciplineById(Integer id) {
        return disciplineRepository.findById(id).orElse(null);
    }

    public Discipline saveDiscipline(Discipline discipline) {
        return disciplineRepository.save(discipline);
    }

    public void deleteDiscipline(Integer id) {
        disciplineRepository.deleteById(id);
    }
}
