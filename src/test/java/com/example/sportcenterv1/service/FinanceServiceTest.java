package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.repository.ContractRepository;
import com.example.sportcenterv1.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinanceServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private FinanceService financeService;

    private LocalDate date;
    private Specialization specialization;

    @BeforeEach
    void setUp() {
        date = LocalDate.of(2023, 7, 1);
        specialization = new Specialization();
        specialization.setId(1L);
        specialization.setName("Java");
    }

    @Test
    void testGetCostFromAllContractsByMonth() {
        when(contractRepository.findTotalSalaryByMonth(date)).thenReturn(10000.0);

        double result = financeService.getCostFromAllContractsByMonth(date);
        assertEquals(10000.0, result);

        when(contractRepository.findTotalSalaryByMonth(date)).thenReturn(null);

        result = financeService.getCostFromAllContractsByMonth(date);
        assertEquals(0.0, result);
    }

    @Test
    void testGetProfitFromAllReservationByMonth() {
        when(reservationRepository.findTotalIncomeByMonth(date)).thenReturn(15000.0);

        double result = financeService.getProfitFromAllReservationByMonth(date);
        assertEquals(15000.0, result);

        when(reservationRepository.findTotalIncomeByMonth(date)).thenReturn(null);

        result = financeService.getProfitFromAllReservationByMonth(date);
        assertEquals(0.0, result);
    }

    @Test
    void testGetTopOffersByReservationsInMonth() {
        List<Object[]> expected = Arrays.asList(
                new Object[]{"Offer 1", 10L},
                new Object[]{"Offer 2", 5L}
        );

        when(reservationRepository.findTopOffersByReservationsInMonth(date)).thenReturn(expected);

        List<Object[]> result = financeService.getTopOffersByReservationsInMonth(date);
        assertEquals(expected, result);
    }

    @Test
    void testGetTopOffersBySpecializationInMonth() {
        List<Object[]> expected = Arrays.asList(
                new Object[]{"Offer 1", 8L},
                new Object[]{"Offer 2", 6L}
        );

        when(reservationRepository.findTopOffersBySpecializationInMonth(date, specialization.getId())).thenReturn(expected);

        List<Object[]> result = financeService.getTopOffersBySpecializationInMonth(date, specialization);
        assertEquals(expected, result);
    }
}
