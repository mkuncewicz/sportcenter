package com.example.sportcenterv1.entity.space;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

//Wielodziedziczenie
@Entity
@DiscriminatorValue("SWIMMING_POOL")
@Getter
@Setter
public class SwimmingPool extends Space{

    @Column
    private double poolLength;

    @Column
    private double poolDepth;

    @Column
    private int laneCount;
}
