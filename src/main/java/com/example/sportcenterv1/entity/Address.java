package com.example.sportcenterv1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Address {

    @Column
    private String city;

    @Column
    private String street;

    @Column
    private String buildingNumber;

    @Column(nullable = false)
    private String apartmentNumber;
}
