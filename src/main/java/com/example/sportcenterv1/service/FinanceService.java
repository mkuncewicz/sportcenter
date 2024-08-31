package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Offer;
import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.repository.ContractRepository;
import com.example.sportcenterv1.repository.OfferRepository;
import com.example.sportcenterv1.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinanceService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public double getCostFromAllContractsByMonth(LocalDate localDate){

        Double totalCosts = contractRepository.findTotalSalaryByMonth(localDate);
        return totalCosts != null ? totalCosts : 0.0;
    }

    public double getProfitFromAllReservationByMonth(LocalDate localDate) {

        Double totalIncome = reservationRepository.findTotalIncomeByMonth(localDate);
        return totalIncome != null ? totalIncome : 0.0;
    }

    public List<Object[]> getTopOffersByReservationsInMonth(LocalDate date) {
        return reservationRepository.findTopOffersByReservationsInMonth(date);
    }

    public List<Object[]> getTopOffersBySpecializationInMonth(LocalDate date, Specialization specialization) {
        return reservationRepository.findTopOffersBySpecializationInMonth(date, specialization.getId());
    }
}
