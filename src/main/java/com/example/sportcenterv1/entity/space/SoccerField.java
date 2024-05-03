package com.example.sportcenterv1.entity.space;

import com.example.sportcenterv1.entity.enums.TurfType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//Wielodziedziczenie
@Entity
@DiscriminatorValue("SOCCER_FIELD")
@Getter
@Setter
public class SoccerField extends Space{

    @Column
    private double squareFootage;

    @Column
    private int goalCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private TurfType turfType;
}
