package com.example.sportcenterv1.entity;

import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.entity.space.Space;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
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

    @ManyToMany(mappedBy = "specializations", fetch = FetchType.LAZY)
    private Set<Employee> employees = new HashSet<>();

    @ManyToMany(mappedBy = "specializations", fetch = FetchType.LAZY)
    private Set<Space> spaces = new HashSet<>();

    @ManyToMany(mappedBy = "specializations", fetch = FetchType.LAZY)
    private Set<Offer> offers = new HashSet<>();


    @Override
    public String toString() {
        return name;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Specialization that = (Specialization) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
