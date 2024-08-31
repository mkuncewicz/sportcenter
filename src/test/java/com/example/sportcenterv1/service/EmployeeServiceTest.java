package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Contract;
import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.entity.enums.ContractStatus;
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
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private SpecializationRepository specializationRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private Contract contract1;
    private Contract contract2;
    private Specialization specialization;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setPhoneNumber("123456789");
        employee.setDateOfBirth(new Date());
        employee.setContracts(new HashSet<>());

        contract1 = new Contract();
        contract1.setId(1L);
        contract1.setDateStart(new Date());
        contract1.setDateEnd(new Date(System.currentTimeMillis() + 86400000L)); // +1 day
        contract1.setContractStatus(ContractStatus.CONFIRMED);
        contract1.setEmployee(employee);

        contract2 = new Contract();
        contract2.setId(2L);
        contract2.setDateStart(new Date());
        contract2.setDateEnd(new Date(System.currentTimeMillis() + 86400000L * 2)); // +2 days
        contract2.setContractStatus(ContractStatus.IN_PROGRESS);
        contract2.setEmployee(employee);

        specialization = new Specialization();
        specialization.setId(1L);
        specialization.setName("Java");

        employee.setSpecializations(Set.of(specialization));
    }

    @Test
    void testGetEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployee(1L);
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(employee);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void testGetAllEmployeesByName() {
        List<Employee> employees = Arrays.asList(employee);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployeesByName("john");
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void testGetAllEmployeesBySpecialization() {
        List<Employee> employees = Arrays.asList(employee);
        when(employeeRepository.findAll()).thenReturn(employees);

        Set<Employee> result = employeeService.getAllEmployeesBySpecialization(List.of(specialization));
        assertEquals(1, result.size());
        assertTrue(result.contains(employee));
    }

    @Test
    void testCreateEmployee() {
        employeeService.createEmployee(employee);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testUpdateEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee updatedEmployee = new Employee();
        updatedEmployee.setFirstName("Jane");
        updatedEmployee.setLastName("Smith");
        updatedEmployee.setPhoneNumber("987654321");

        employeeService.updateEmployee(1L, updatedEmployee);

        assertEquals("Jane", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("987654321", employee.getPhoneNumber());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testDeleteEmployee() {
        employeeService.deleteEmployee(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetEmployeesWithSpecialization() {
        when(specializationRepository.findById(1L)).thenReturn(Optional.of(specialization));
        when(employeeRepository.findEmployeesBySpecializationId(1L)).thenReturn(List.of(employee));

        List<Employee> result = employeeService.getEmployeesWithSpecialization(1L);
        assertEquals(1, result.size());
        assertTrue(result.contains(employee));
    }

    @Test
    void testAddContractToEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.addContractToEmployee(1L, contract1);

        assertEquals(1, employee.getContracts().size());
        assertTrue(employee.getContracts().contains(contract1));
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testRemoveContract() {
        contract1.setContractStatus(ContractStatus.COMPLETED);
        employee.getContracts().add(contract1);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract1));

        employeeService.removeContract(1L, 1L);

        assertFalse(employee.getContracts().contains(contract1));
        verify(contractRepository, times(1)).delete(contract1);
    }

    @Test
    void testGetCountOfContracts() {
        employee.getContracts().add(contract1);
        employee.getContracts().add(contract2);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        int result = employeeService.getCountOfContracts(1L);
        assertEquals(2, result);
    }

    @Test
    void testGetDateToLastActiveContract() {
        employee.getContracts().add(contract1);
        employee.getContracts().add(contract2);

        Date lastActiveDate = contract2.getDateEnd();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Date result = employeeService.getDateToLastActiveContract(1L);
        assertEquals(lastActiveDate, result);
    }
}
