package com.example.sportcenterv1.entity.space;


import com.example.sportcenterv1.entity.enums.CourtType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//Wielodziedziczenie
@Entity
@DiscriminatorValue("BASKETBALL_ROOM")
@Getter
@Setter
public class BasketballRoom extends Room{

    @Column
    private int hoopCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private CourtType courtType;
}
