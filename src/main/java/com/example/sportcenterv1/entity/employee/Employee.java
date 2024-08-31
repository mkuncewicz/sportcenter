package com.example.sportcenterv1.entity.employee;

import com.example.sportcenterv1.entity.Address;
import com.example.sportcenterv1.entity.Contract;
import com.example.sportcenterv1.entity.Specialization;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employees")
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column
    private Date dateOfBirth;

    @Column(nullable = false)
    private String phoneNumber;

    @Embedded
    private Address address = new Address();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_specialization",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    private Set<Specialization> specializations = new HashSet<>();

    //Asocjacja kompozycja
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "employee")
    private Set<Contract> contracts = new HashSet<>();

}
