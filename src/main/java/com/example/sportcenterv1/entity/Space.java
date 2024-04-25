package com.example.sportcenterv1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private String type;


}
