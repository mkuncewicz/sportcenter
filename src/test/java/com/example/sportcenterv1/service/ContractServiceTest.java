package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Contract;
import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.entity.enums.ContractStatus;
import com.example.sportcenterv1.entity.enums.ContractType;
import com.example.sportcenterv1.repository.ContractRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private Clock clock;

    @InjectMocks
    private ContractService contractService;

    private Contract contract1;
    private Contract contract2;
    private Employee employee;
    private Instant fixedInstant;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);

        fixedInstant = Instant.parse("2024-07-15T00:00:00Z");

        contract1 = new Contract();
        contract1.setId(1L);
        contract1.setSalary(5000);
        contract1.setDateStart(Date.from(fixedInstant.minusSeconds(864000))); // -10 days
        contract1.setDateEnd(Date.from(fixedInstant.plusSeconds(2764800))); // +32 days
        contract1.setContractType(ContractType.EMPLOYMENT_CONTRACT);
        contract1.setEmployee(employee);
        contract1.setContractStatus(ContractStatus.CONFIRMED);

        contract2 = new Contract();
        contract2.setId(2L);
        contract2.setSalary(6000);
        contract2.setDateStart(Date.from(fixedInstant.minusSeconds(1728000))); // -20 days
        contract2.setDateEnd(Date.from(fixedInstant.minusSeconds(86400))); // -1 day
        contract2.setContractType(ContractType.MANDATE_CONTRACT);
        contract2.setEmployee(employee);
        contract2.setContractStatus(ContractStatus.CONFIRMED);

        lenient().when(clock.instant()).thenReturn(fixedInstant);
        lenient().when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    }

    @Test
    void testGetAllContracts() {
        List<Contract> contracts = Arrays.asList(contract1, contract2);
        when(contractRepository.findAll()).thenReturn(contracts);

        List<Contract> result = contractService.getAllContracts();
        assertEquals(2, result.size());
        assertEquals(5000, result.get(0).getSalary());
        assertEquals(6000, result.get(1).getSalary());
    }

    @Test
    void testGetAllContractByEmployee() {
        List<Contract> contracts = Arrays.asList(contract1, contract2);
        when(contractRepository.findAll()).thenReturn(contracts);

        List<Contract> result = contractService.getAllContractByEmployee(employee);
        assertEquals(2, result.size());
        assertTrue(result.contains(contract1));
        assertTrue(result.contains(contract2));
    }

    @Test
    void testGetAllContractByEmployeeAndStatusType() {
        List<Contract> contracts = Arrays.asList(contract1, contract2);
        when(contractRepository.findAll()).thenReturn(contracts);

        List<Contract> result = contractService.getAllContractByEmployeeAndStatusType(employee, ContractStatus.CONFIRMED);
        assertEquals(2, result.size());

        result = contractService.getAllContractByEmployeeAndStatusType(employee, null);
        assertEquals(2, result.size());
    }

    @Test
    void testGetContract() {
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract1));

        Optional<Contract> result = contractService.getContract(1L);
        assertTrue(result.isPresent());
        assertEquals(5000, result.get().getSalary());
    }

    @Test
    void testSaveContract() {
        contractService.saveContract(contract1);
        verify(contractRepository, times(1)).save(contract1);
    }

    @Test
    void testUpdateContract() {
        Contract updatedContract = new Contract();
        updatedContract.setSalary(7000);
        updatedContract.setDateEnd(Date.from(fixedInstant.plusSeconds(259200))); // +3 days

        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract1));
        contractService.updateContract(1L, updatedContract);

        assertEquals(7000, contract1.getSalary());
        assertEquals(updatedContract.getDateEnd(), contract1.getDateEnd());
        verify(contractRepository, times(1)).save(contract1);
    }

    @Test
    void testUpdateStatus() {
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract1));

        boolean result = contractService.updateStatus(1L, ContractStatus.COMPLETED);
        assertTrue(result);
        assertEquals(ContractStatus.COMPLETED, contract1.getContractStatus());
        verify(contractRepository, times(1)).save(contract1);

        contract1.setContractStatus(ContractStatus.IN_PROGRESS);
        result = contractService.updateStatus(1L, ContractStatus.REJECTED);
        assertFalse(result);
        verify(contractRepository, times(1)).save(contract1);
    }

    @Test
    void testUpdateStatusByDate() {
        // Ustawienie dat, aby test działał poprawnie
        contract1.setDateStart(Date.from(fixedInstant.minusSeconds(864000))); // -10 days
        contract1.setDateEnd(Date.from(fixedInstant.plusSeconds(2764800))); // +32 days

        contract2.setDateStart(Date.from(fixedInstant.minusSeconds(1728000))); // -20 days
        contract2.setDateEnd(Date.from(fixedInstant.minusSeconds(86400))); // -1 day

        contract1.setContractStatus(ContractStatus.CONFIRMED);
        contract2.setContractStatus(ContractStatus.CONFIRMED);

        when(contractRepository.findAll()).thenReturn(Arrays.asList(contract1, contract2));

        contractService.updateStatus();

        verify(contractRepository, times(2)).save(any(Contract.class));

        assertEquals(ContractStatus.IN_PROGRESS, contract1.getContractStatus());
        assertEquals(ContractStatus.COMPLETED, contract2.getContractStatus());
    }

    @Test
    void testDeleteContract() {
        contractService.deleteContract(1L);
        verify(contractRepository, times(1)).deleteById(1L);
    }
}
