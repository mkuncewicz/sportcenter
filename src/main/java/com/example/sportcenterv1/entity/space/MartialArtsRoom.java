package com.example.sportcenterv1.entity.space;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Wielodziedziczenie
@Entity
@DiscriminatorValue("MARTIAL_ARTS_ROOM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MartialArtsRoom extends Room{

    @Column
    private int matCount;
}
