package com.schedule.kstu.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "discipline")
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "academic_hours")
    private Integer academicHours;

    @OneToMany(mappedBy = "discipline")
    private List<Statement> statements = new ArrayList<>();

    // Constructors
    public Discipline() {}

    public Discipline(String name) {
        this.name = name;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAcademicHours() {return academicHours;}

    public void setAcademicHours(Integer academicHours) {this.academicHours = academicHours;}

    public List<Statement> getStatements() {
        return statements;
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }
}
