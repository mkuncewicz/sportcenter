package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Employee;
import com.example.sportcenterv1.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

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
}
