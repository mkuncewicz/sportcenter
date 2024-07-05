package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Contract;
import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.repository.ContractRepository;
import com.example.sportcenterv1.repository.EmployeeRepository;
import com.example.sportcenterv1.repository.SpecializationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private SpecializationRepository specializationRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee1;

    private Employee employee2;

    private Specialization specialization;

    @BeforeEach
    void setUp() {
        employee1 = new Employee();
        employee1.setId(1L);
        employee1.setFirstName("John");
        employee1.setLastName("Doe");
        employee1.setPhoneNumber("123456789");
        employee1.setDateOfBirth(new Date());

        employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFirstName("Mirosław");
        employee2.setLastName("Nowak");
        employee2.setPhoneNumber("987654321");
        employee2.setDateOfBirth(new Date());

        specialization.setId(1L);
        specialization.setName("Koszykówka");

        employee1.getSpecializations().add(specialization);
    }

    @Test
    void testGetEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));

        Optional<Employee> result = employeeService.getEmployee(1L);
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(employee1,employee2);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();
        assertEquals(2, result.size());
    }

    @Test
    void testCreateEmployee() {
        employeeService.createEmployee(employee1);
        verify(employeeRepository, times(1)).save(employee1);
    }

    @Test
    void testUpdateEmployee() {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setFirstName("Jane");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        employeeService.updateEmployee(1L, updatedEmployee);

        assertEquals("Jane", employee1.getFirstName());
        verify(employeeRepository, times(1)).save(employee1);
    }

    @Test
    void testDeleteEmployee() {
        employeeService.deleteEmployee(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllEmployeesByName() {
        List<Employee> employees = Arrays.asList(employee1);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployeesByName("john");
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void testGetAllEmployeesBySpecialization() {
        Specialization specialization = new Specialization();
        specialization.setId(1L);
        employee1.setSpecializations(Set.of(specialization));

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1));

        Set<Employee> result = employeeService.getAllEmployeesBySpecialization(List.of(specialization));
        assertEquals(1, result.size());
        assertEquals(employee1, result.iterator().next());
    }

}
