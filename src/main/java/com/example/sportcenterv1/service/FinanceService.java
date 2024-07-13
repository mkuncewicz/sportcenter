package com.example.sportcenterv1.service;

import com.example.sportcenterv1.repository.ContractRepository;
import com.example.sportcenterv1.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class FinanceService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ReservationRepository reservationRepository;


    public double getCostFromAllContractsByMonth(LocalDate localDate){

        return contractRepository.findTotalSalaryByMonth(localDate);
    }

    public double getProfitFromAllReservationByMonth(LocalDate localDate) {

        Double totalIncome = reservationRepository.findTotalIncomeByMonth(localDate);
        return totalIncome != null ? totalIncome : 0.0;
    }
}
