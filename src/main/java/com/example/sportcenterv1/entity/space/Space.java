package com.example.sportcenterv1.entity.space;

import com.example.sportcenterv1.entity.Specialization;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "spaces")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "space_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
public abstract class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;


}
