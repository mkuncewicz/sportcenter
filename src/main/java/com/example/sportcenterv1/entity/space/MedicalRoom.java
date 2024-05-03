package com.example.sportcenterv1.entity.space;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

//Wielodziedziczenie
@Entity
@DiscriminatorValue("MEDICAL_ROOM")
@Setter
@Getter
public class MedicalRoom extends Room{

    @Column
    private boolean isSterile;

}
