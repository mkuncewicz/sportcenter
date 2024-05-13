package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Contract;
import com.example.sportcenterv1.entity.enums.ContractStatusType;
import com.example.sportcenterv1.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;


    public List<Contract> getAllContracts(){
        return contractRepository.findAll();
    }

    public Optional<Contract> getContract(Long contractID){
        return contractRepository.findById(contractID);
    }

    public void saveContract(Contract contract){
        contractRepository.save(contract);
    }

    public void updateContract(Long contractID, Contract updateContract){

        Optional<Contract> optionalContract = contractRepository.findById(contractID);

        if (optionalContract.isPresent()){
            Contract saveContract = optionalContract.get();

            if (updateContract.getSalary() > 0) saveContract.setSalary(updateContract.getSalary());
            if (updateContract.getDateStart() != null) saveContract.setDateStart(updateContract.getDateStart());
            if (updateContract.getDateEnd() != null) saveContract.setDateEnd(updateContract.getDateEnd());
            if (updateContract.getContractType() != null) saveContract.setContractType(updateContract.getContractType());

            contractRepository.save(saveContract);
        }
    }

    //Przeciążenie metody
    public void updateStatus(Long contractID, ContractStatusType statusUpdate){
        Optional<Contract> optionalContract = contractRepository.findById(contractID);

        if (optionalContract.isPresent()){
            Contract saveContract = optionalContract.get();

            saveContract.setContractStatusType(statusUpdate);

            contractRepository.save(saveContract);
        }
    }

    public void updateStatus(Long contractID){
        Optional<Contract> optionalContract = contractRepository.findById(contractID);

        if (optionalContract.isPresent()){
            Date nowDate = new Date();
            Contract contract = optionalContract.get();
            Date dateStart = contract.getDateStart();
            Date dateEnd = contract.getDateEnd();

            long diffInMillies = Math.abs(dateEnd.getTime() - nowDate.getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (dateEnd.after(nowDate)){
                contract.setContractStatusType(ContractStatusType.IN_PROGRESS);
            }
            if (diffInDays < 30){
                contract.setContractStatusType(ContractStatusType.PENDING);
            }
            if (dateEnd.before(nowDate)){
                contract.setContractStatusType(ContractStatusType.COMPLETED);
            }
        }
    }

    public void deleteContract(Long contractID){
        contractRepository.deleteById(contractID);
    }


}