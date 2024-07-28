package com.example.sportcenterv1.repository;

import com.example.sportcenterv1.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    // Metoda zwracająca koszt kontraktów z danego miesiąca
    @Query(value = "SELECT SUM(salary) FROM contracts WHERE (date_start <= LAST_DAY(:date)) AND (date_end >= :date OR (YEAR(date_end) = YEAR(:date) AND MONTH(date_end) = MONTH(:date))) AND contract_status IN ('IN_PROGRESS', 'COMPLETED')", nativeQuery = true)
    Double findTotalSalaryByMonth(@Param("date") LocalDate date);
}

