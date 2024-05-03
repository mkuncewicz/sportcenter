package com.example.sportcenterv1.entity.space;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//Wielodziedziczenie
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "space_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
public class Room extends Space{

    @Column
    private int capacity;

    @Column
    private double squareFootage;
}
