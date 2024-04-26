package com.example.sportcenterv1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "spaces")
@Getter
@Setter
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private double sqrMeters;

    @Column
    private double width;

    @Column
    private double length;

    @Column
    private double depth;

    @Column
    private String type;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "space_specialization",
            joinColumns = @JoinColumn(name = "space_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    private Set<Specialization> specializations;


}
