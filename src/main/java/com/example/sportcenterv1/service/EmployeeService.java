package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Contract;
import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.entity.enums.ContractStatusType;
import com.example.sportcenterv1.repository.ContractRepository;
import com.example.sportcenterv1.repository.EmployeeRepository;
import com.example.sportcenterv1.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private SpecializationRepository specializationRepository;

    public Optional<Employee> getEmployee(Long employeeID){
        return employeeRepository.findById(employeeID);
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public List<Employee> getAllEmployeesByName(String name){

        List<Employee> list = employeeRepository.findAll();

        List<Employee> result = new ArrayList<>();

        for(Employee employee : list){
            String nameOfEmployee = employee.getFirstName() + " " + employee.getLastName();
            String lowerName = nameOfEmployee.toLowerCase();

            if (lowerName.contains(name.toLowerCase())) result.add(employee);
        }

        return result;
    }

    public void createEmployee(Employee employee){

        if(employee.getFirstName() == null) {
            return;
        }
        if(employee.getLastName() == null) {
            return;
        }
        if(employee.getPhoneNumber() == null) {
            return;
        };
        if(employee.getDateOfBirth() == null) {
            return;
        }
        if (employee.getAddress() == null) {
            return;
        }

        employeeRepository.save(employee);
    }

    public void updateEmployee(Long employeeID, Employee employeeToSave) {
        Optional<Employee> employeeDB = employeeRepository.findById(employeeID);

        if (employeeDB.isPresent()) {
            Employee empDB = employeeDB.get();

            if (employeeToSave.getFirstName() != null) {
                empDB.setFirstName(employeeToSave.getFirstName());
            }
            if (employeeToSave.getLastName() != null) {
                empDB.setLastName(employeeToSave.getLastName());
            }
            if (employeeToSave.getPhoneNumber() != null) {
                empDB.setPhoneNumber(employeeToSave.getPhoneNumber());
            }
            if (employeeToSave.getDateOfBirth() != null){
                empDB.setDateOfBirth(employeeToSave.getDateOfBirth());
            }
            if (employeeToSave.getAddress().getStreet() != null){
                empDB.getAddress().setStreet(employeeToSave.getAddress().getStreet());
            }
            if (employeeToSave.getAddress().getCity() != null){
                empDB.getAddress().setCity(employeeToSave.getAddress().getCity());
            }
            if (employeeToSave.getAddress().getBuildingNumber() != null){
                empDB.getAddress().setBuildingNumber(employeeToSave.getAddress().getBuildingNumber());
            }
            if (employeeToSave.getAddress().getApartmentNumber() != null){
                empDB.getAddress().setApartmentNumber(employeeToSave.getAddress().getBuildingNumber());
            }

            employeeRepository.save(empDB);  //
        }
    }

    public void deleteEmployee(Long employeeId){
        employeeRepository.deleteById(employeeId);
    }

    public List<Employee> getEmployeesWithSpecialization(Long specId){

        Optional<Specialization> optionalSpecialization = specializationRepository.findById(specId);

        if (optionalSpecialization.isEmpty()) return employeeRepository.findAll();

        return employeeRepository.findEmployeesBySpecializationId(specId);
    }

    public void addContractToEmployee(Long employeeID, Contract contract){

        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeID);

        if(optionalEmployee.isPresent()){

            Employee employee = optionalEmployee.get();

            employee.getContracts().add(contract);
            contract.setEmployee(employee);

            employeeRepository.save(employee);
        }
    }

    //Usuwanie kontraktow tylko wylacznie kiedy ich status jest inny niz:
    //(Potwierdzony), (W trakcie), (Wygasający)
    public void removeContract(Long employeeID, Long contractID){

        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeID);
        Optional<Contract> optionalContract = contractRepository.findById(contractID);

        if (optionalEmployee.isPresent() && optionalContract.isPresent()){

            Employee employee = optionalEmployee.get();
            Contract contract = optionalContract.get();

            if (contract.getContractStatusType() == ContractStatusType.IN_PROGRESS ||
                    contract.getContractStatusType() == ContractStatusType.EXPIRING ||
                    contract.getContractStatusType() == ContractStatusType.CONFIRMED)
            {
                System.out.println("Nie dozwolony status");
                return;
            }
            employee.getContracts().remove(contract);
            contract.setEmployee(null);

            contractRepository.delete(contract);
        }
    }

    //Pobieranie ilosci aktualnych kontraktów
    public int getCountOfContracts(Long employeeID){

        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeID);

        if (optionalEmployee.isPresent()){

            Employee employee = optionalEmployee.get();

            List<Contract> activeContracts = employee.getContracts().stream().filter(x -> x.getContractStatusType() == ContractStatusType.EXPIRING ||
                    x.getContractStatusType() == ContractStatusType.CONFIRMED || x.getContractStatusType() == ContractStatusType.IN_PROGRESS).toList();

            return activeContracts.size();

        }

        return 0;
    }

    public int getCountOfContracts(Employee employee){

        List<Contract> activeContracts = employee.getContracts().stream().filter(x -> x.getContractStatusType() == ContractStatusType.EXPIRING ||
                x.getContractStatusType() == ContractStatusType.CONFIRMED || x.getContractStatusType() == ContractStatusType.IN_PROGRESS).toList();

        return activeContracts.size();
    }

    //Pobieranie daty ostatniego aktywnego kontraktu
    public Date getDateToLastActiveContract(Employee employee){

        List<Date>  listOfDate;

        listOfDate = employee.getContracts().stream().filter(x -> x.getContractStatusType() == ContractStatusType.EXPIRING ||
                x.getContractStatusType() == ContractStatusType.CONFIRMED || x.getContractStatusType() == ContractStatusType.IN_PROGRESS)
                .map(Contract::getDateEnd)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        if (listOfDate.size() > 0){
            return listOfDate.get(0);
        }else {
            return null;
        }
    }

    public Date getDateToLastActiveContract(Long employeeID){

        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeID);

        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            List<Date> listOfDate;

            listOfDate = employee.getContracts().stream().filter(x -> x.getContractStatusType() == ContractStatusType.EXPIRING ||
                            x.getContractStatusType() == ContractStatusType.CONFIRMED || x.getContractStatusType() == ContractStatusType.IN_PROGRESS)
                    .map(Contract::getDateEnd)
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());

            if (listOfDate.size() > 0) {
                return listOfDate.get(0);
            } else {
                return null;
            }
        }
        return null;
    }
}

