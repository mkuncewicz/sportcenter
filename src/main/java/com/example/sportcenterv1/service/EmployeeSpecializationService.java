package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Employee;
import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.repository.EmployeeRepository;
import com.example.sportcenterv1.repository.SpecializationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeSpecializationService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SpecializationRepository specializationRepository;

    @Transactional
    public void addSpecializationToEmployee(Long employeeID, Long specID) {
        Optional<Employee> employee = employeeRepository.findById(employeeID);
        Optional<Specialization> specialization = specializationRepository.findById(specID);

        if (employee.isPresent() && specialization.isPresent()) {
            Employee saveEmployee = employee.get();
            Specialization saveSpec = specialization.get();

            saveEmployee.getSpecializations().add(saveSpec);
            saveSpec.getEmployees().add(saveEmployee);

            employeeRepository.save(saveEmployee);
            specializationRepository.save(saveSpec);
        }
    }

    @Transactional
    public void deleteSpecializationFromEmployee(Long employeeID, Long specID) {
        Optional<Employee> employee = employeeRepository.findById(employeeID);
        Optional<Specialization> specialization = specializationRepository.findById(specID);

        if (employee.isPresent() && specialization.isPresent()) {
            Employee saveEmployee = employee.get();
            Specialization saveSpec = specialization.get();

            saveEmployee.getSpecializations().remove(saveSpec);
            saveSpec.getEmployees().remove(saveEmployee);

            employeeRepository.save(saveEmployee);
            specializationRepository.save(saveSpec);
        }
    }
}
