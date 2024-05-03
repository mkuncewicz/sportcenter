package com.example.sportcenterv1.entity;

import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.entity.space.Space;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "specializations")
@Getter
@Setter
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String level;

    @ManyToMany(mappedBy = "specializations", fetch = FetchType.LAZY)
    private Set<Employee> employees;

    @ManyToMany(mappedBy = "specializations", fetch = FetchType.LAZY)
    private Set<Space> spaces;

    @Override
    public String toString() {
        return name;
    }
}
