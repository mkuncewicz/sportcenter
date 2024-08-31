package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Contract;
import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.entity.enums.ContractStatus;
import com.example.sportcenterv1.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private Clock clock;

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    public List<Contract> getAllContractByEmployee(Employee employee) {
        return contractRepository.findAll().stream()
                .filter(e -> e.getEmployee().getId().equals(employee.getId()))
                .collect(Collectors.toList());
    }

    public List<Contract> getAllContractByEmployeeAndStatusType(Employee employee, ContractStatus contractStatusType) {
        List<Contract> result = contractRepository.findAll().stream()
                .filter(e -> e.getEmployee().getId().equals(employee.getId()))
                .collect(Collectors.toList());

        if (contractStatusType != null) {
            result = result.stream().filter(contract -> contract.getContractStatus() == contractStatusType).collect(Collectors.toList());
        }

        return result;
    }

    public Optional<Contract> getContract(Long contractID) {
        return contractRepository.findById(contractID);
    }

    public void saveContract(Contract contract) {
        contractRepository.save(contract);
    }

    public void updateContract(Long contractID, Contract updateContract) {
        Optional<Contract> optionalContract = contractRepository.findById(contractID);

        if (optionalContract.isPresent()) {
            Contract saveContract = optionalContract.get();
            if (updateContract.getSalary() > 0) saveContract.setSalary(updateContract.getSalary());
            if (updateContract.getDateStart() != null) saveContract.setDateStart(updateContract.getDateStart());
            if (updateContract.getDateEnd() != null) saveContract.setDateEnd(updateContract.getDateEnd());
            if (updateContract.getContractType() != null) saveContract.setContractType(updateContract.getContractType());

            contractRepository.save(saveContract);
        }
    }

    public boolean updateStatus(Long contractID, ContractStatus statusUpdate) {
        Optional<Contract> optionalContract = contractRepository.findById(contractID);

        if (optionalContract.isPresent()) {
            Contract saveContract = optionalContract.get();
            if (saveContract.getContractStatus() == ContractStatus.NEW ||
                    saveContract.getContractStatus() == ContractStatus.PENDING ||
                    saveContract.getContractStatus() == ContractStatus.REJECTED ||
                    saveContract.getContractStatus() == ContractStatus.CONFIRMED) {

                saveContract.setContractStatus(statusUpdate);
                contractRepository.save(saveContract);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void updateStatus() {
        List<Contract> list = contractRepository.findAll();
        Date nowDate = Date.from(Instant.now(clock));

        for (Contract contract : list) {
            if (contract.getContractStatus() == ContractStatus.CONFIRMED ||
                    contract.getContractStatus() == ContractStatus.IN_PROGRESS ||
                    contract.getContractStatus() == ContractStatus.EXPIRING) {

                Date dateStart = contract.getDateStart();
                Date dateEnd = contract.getDateEnd();

                long diffInMillies = Math.abs(dateEnd.getTime() - nowDate.getTime());
                long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                if (dateStart.before(nowDate)) {
                    contract.setContractStatus(ContractStatus.IN_PROGRESS);
                }
                if (diffInDays < 30) {
                    contract.setContractStatus(ContractStatus.EXPIRING);
                }
                if (dateEnd.before(nowDate)) {
                    contract.setContractStatus(ContractStatus.COMPLETED);
                }
                contractRepository.save(contract);
                System.out.println("Contract " + contract.getId() + " updated to status " + contract.getContractStatus());
            }
        }
    }

    public void updateStatus(Long contractID) {
        Optional<Contract> optionalContract = contractRepository.findById(contractID);

        if (optionalContract.isPresent()) {
            Date nowDate = Date.from(Instant.now(clock));
            Contract contract = optionalContract.get();
            if (contract.getContractStatus() == ContractStatus.CONFIRMED ||
                    contract.getContractStatus() == ContractStatus.IN_PROGRESS ||
                    contract.getContractStatus() == ContractStatus.EXPIRING) {

                Date dateStart = contract.getDateStart();
                Date dateEnd = contract.getDateEnd();

                long diffInMillies = Math.abs(dateEnd.getTime() - nowDate.getTime());
                long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                if (dateEnd.after(nowDate)) {
                    contract.setContractStatus(ContractStatus.IN_PROGRESS);
                }
                if (diffInDays < 30) {
                    contract.setContractStatus(ContractStatus.PENDING);
                }
                if (dateEnd.before(nowDate)) {
                    contract.setContractStatus(ContractStatus.COMPLETED);
                }
            }
        }
    }

    public void deleteContract(Long contractID) {
        contractRepository.deleteById(contractID);
    }
}
