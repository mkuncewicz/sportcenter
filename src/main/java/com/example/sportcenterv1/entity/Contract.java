package com.example.sportcenterv1.entity;

import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.entity.enums.ContractStatus;
import com.example.sportcenterv1.entity.enums.ContractType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "contracts")
@Getter
@Setter
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double salary;

    @Temporal(TemporalType.DATE)
    private Date dateStart;

    @Temporal(TemporalType.DATE)
    private Date dateEnd;

    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    //Asocjacja kompozycja
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus = ContractStatus.NEW;

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", salary=" + salary +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", contractType=" + contractType +
                ", employee=" + employee +
                ", contractStatusType=" + contractStatus +
                '}';
    }
}
