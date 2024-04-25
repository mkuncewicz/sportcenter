package com.example.sportcenterv1.repository;


import com.example.sportcenterv1.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e JOIN e.specializations s WHERE s.id = :specId")
    List<Employee> findEmployeesBySpecializationId(@Param("specId") Long specId);
}
