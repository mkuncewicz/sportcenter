package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Contract;
import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.repository.ContractRepository;
import com.example.sportcenterv1.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ContractRepository contractRepository;

    public Optional<Employee> getEmployee(Long employeeID){
        return employeeRepository.findById(employeeID);
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public void createEmployee(Employee employee){

        if(employee.getFirstName() == null) return;
        if(employee.getLastName() == null) return;
        if(employee.getPhoneNumber() == null) return;

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

            employeeRepository.save(empDB);  //
        }
    }

    public void deleteEmployee(Long employeeId){
        employeeRepository.deleteById(employeeId);
    }

    public List<Employee> getEmployeesWithSpecialization(Long specId){
        return employeeRepository.findEmployeesBySpecializationId(specId);
    }

    public void addContractToEmployee(Long employeeID, Contract contract){

        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeID);

        if(optionalEmployee.isPresent()){

            Employee employee = optionalEmployee.get();

            employee.getContracts().add(contract);
            contract.setEmployee(employee);

            employeeRepository.save(employee);

            contractRepository.save(contract);
        }
    }

    public void removeContract(Long employeeID, Long contractID){

        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeID);
        Optional<Contract> optionalContract = contractRepository.findById(contractID);

        if (optionalEmployee.isPresent() && optionalContract.isPresent()){

            Employee employee = optionalEmployee.get();
            Contract contract = optionalContract.get();

            employee.getContracts().remove(contract);
            contract.setEmployee(null);

            contractRepository.delete(contract);
        }
    }
}

