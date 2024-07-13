package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Contract;
import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.entity.enums.ContractStatusType;
import com.example.sportcenterv1.entity.enums.ContractType;
import com.example.sportcenterv1.repository.ContractRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private ContractService contractService;

    private Contract contract1;
    private Contract contract2;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");

        contract1 = new Contract();
        contract1.setId(1L);
        contract1.setSalary(5000.0);
        contract1.setDateStart(new Date());
        contract1.setDateEnd(new Date());
        contract1.setContractType(ContractType.EMPLOYMENT_CONTRACT);
        contract1.setEmployee(employee);
        contract1.setContractStatusType(ContractStatusType.NEW);

        contract2 = new Contract();
        contract2.setId(2L);
        contract2.setSalary(3000.0);
        contract2.setDateStart(new Date());
        contract2.setDateEnd(new Date());
        contract2.setContractType(ContractType.B2B_CONTRACT);
        contract2.setEmployee(employee);
        contract2.setContractStatusType(ContractStatusType.CONFIRMED);
    }

    @Test
    void testGetAllContracts() {
        List<Contract> contracts = Arrays.asList(contract1, contract2);
        when(contractRepository.findAll()).thenReturn(contracts);

        List<Contract> result = contractService.getAllContracts();
        assertEquals(2, result.size());
        assertEquals(5000.0, result.get(0).getSalary());
        assertEquals(3000.0, result.get(1).getSalary());
    }

    @Test
    void testGetAllContractByEmployee() {
        List<Contract> contracts = Arrays.asList(contract1, contract2);
        when(contractRepository.findAll()).thenReturn(contracts);

        List<Contract> result = contractService.getAllContractByEmployee(employee);
        assertEquals(2, result.size());
        assertEquals(employee.getId(), result.get(0).getEmployee().getId());
        assertEquals(employee.getId(), result.get(1).getEmployee().getId());
    }

    @Test
    void testGetAllContractByEmployeeAndStatusType() {
        List<Contract> contracts = Arrays.asList(contract1, contract2);
        when(contractRepository.findAll()).thenReturn(contracts);

        List<Contract> result = contractService.getAllContractByEmployeeAndStatusType(employee, ContractStatusType.CONFIRMED);
        assertEquals(1, result.size());
        assertEquals(ContractStatusType.CONFIRMED, result.get(0).getContractStatusType());
    }

    @Test
    void testGetContract() {
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract1));

        Optional<Contract> result = contractService.getContract(1L);
        assertTrue(result.isPresent());
        assertEquals(5000.0, result.get().getSalary());
    }

    @Test
    void testSaveContract() {
        contractService.saveContract(contract1);
        verify(contractRepository, times(1)).save(contract1);
    }

    @Test
    void testUpdateContract() {
        Contract updatedContract = new Contract();
        updatedContract.setSalary(6000.0);
        updatedContract.setDateStart(new Date());
        updatedContract.setDateEnd(new Date());
        updatedContract.setContractType(ContractType.B2B_CONTRACT);

        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract1));
        contractService.updateContract(1L, updatedContract);

        assertEquals(6000.0, contract1.getSalary());
        assertEquals(updatedContract.getDateStart(), contract1.getDateStart());
        assertEquals(updatedContract.getDateEnd(), contract1.getDateEnd());
        assertEquals(ContractType.B2B_CONTRACT, contract1.getContractType());
        verify(contractRepository, times(1)).save(contract1);
    }

    @Test
    void testUpdateStatus() {
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract1));

        boolean result = contractService.updateStatus(1L, ContractStatusType.CONFIRMED);
        assertTrue(result);
        assertEquals(ContractStatusType.CONFIRMED, contract1.getContractStatusType());
        verify(contractRepository, times(1)).save(contract1);
    }

    @Test
    void testUpdateStatus_invalidStatus() {
        contract1.setContractStatusType(ContractStatusType.IN_PROGRESS);
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract1));

        boolean result = contractService.updateStatus(1L, ContractStatusType.REJECTED);
        assertFalse(result);
        assertEquals(ContractStatusType.IN_PROGRESS, contract1.getContractStatusType());
        verify(contractRepository, times(0)).save(contract1);
    }

    @Test
    void testUpdateStatusByDate() {
        Date now = new Date();
        contract1.setDateStart(new Date(now.getTime() - TimeUnit.DAYS.toMillis(10)));
        contract1.setDateEnd(new Date(now.getTime() + TimeUnit.DAYS.toMillis(20)));
        contract1.setContractStatusType(ContractStatusType.CONFIRMED);

        List<Contract> contracts = Arrays.asList(contract1);
        when(contractRepository.findAll()).thenReturn(contracts);

        contractService.updateStatus();

        assertEquals(ContractStatusType.IN_PROGRESS, contract1.getContractStatusType());
        verify(contractRepository, times(1)).save(contract1);
    }

    @Test
    void testDeleteContract() {
        contractService.deleteContract(1L);
        verify(contractRepository, times(1)).deleteById(1L);
    }
}
