package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.repository.EmployeeRepository;
import com.example.sportcenterv1.repository.SpecializationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeSpecializationServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private SpecializationRepository specializationRepository;

    @InjectMocks
    private EmployeeSpecializationService employeeSpecializationService;

    private Employee employee;
    private Specialization specialization;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");

        specialization = new Specialization();
        specialization.setId(1L);
        specialization.setName("Specialization 1");
    }

    @Test
    void testAddSpecializationToEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(specializationRepository.findById(1L)).thenReturn(Optional.of(specialization));

        employeeSpecializationService.addSpecializationToEmployee(1L, 1L);

        assertTrue(employee.getSpecializations().contains(specialization));
        assertTrue(specialization.getEmployees().contains(employee));

        verify(employeeRepository, times(1)).save(employee);
        verify(specializationRepository, times(1)).save(specialization);
    }

    @Test
    void testDeleteSpecializationFromEmployee() {
        employee.getSpecializations().add(specialization);
        specialization.getEmployees().add(employee);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(specializationRepository.findById(1L)).thenReturn(Optional.of(specialization));

        employeeSpecializationService.deleteSpecializationFromEmployee(1L, 1L);

        assertFalse(employee.getSpecializations().contains(specialization));
        assertFalse(specialization.getEmployees().contains(employee));

        verify(employeeRepository, times(1)).save(employee);
        verify(specializationRepository, times(1)).save(specialization);
    }
}
