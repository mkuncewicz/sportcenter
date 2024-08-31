package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Client;
import com.example.sportcenterv1.entity.Offer;
import com.example.sportcenterv1.entity.Reservation;
import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.entity.enums.ReservationStatus;
import com.example.sportcenterv1.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation1;
    private Reservation reservation2;

    @BeforeEach
    void setUp() {
        Client client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");

        Offer offer = new Offer();
        offer.setName("Offer 1");

        Employee employee = new Employee();
        employee.setFirstName("Jane");
        employee.setLastName("Smith");

        reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setDate(LocalDateTime.of(2024, 7, 15, 10, 0)); // 2-godzinna rezerwacja
        reservation1.setClient(client);
        reservation1.setOffer(offer);
        reservation1.setEmployee(employee);
        reservation1.setReservationStatus(ReservationStatus.PAID);

        reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setDate(LocalDateTime.of(2024, 7, 15, 14, 0)); // 1-godzinna rezerwacja
        reservation2.setClient(client);
        reservation2.setOffer(offer);
        reservation2.setEmployee(employee);
        reservation2.setReservationStatus(ReservationStatus.PAID);
    }

    @Test
    void testGetAllReservation() {
        List<Reservation> reservations = Arrays.asList(reservation1, reservation2);
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.getAllReservation();
        assertEquals(2, result.size());
        assertEquals(reservation1, result.get(0));
        assertEquals(reservation2, result.get(1));
    }

    @Test
    void testGetAllReservationByName() {
        List<Reservation> reservations = Arrays.asList(reservation1, reservation2);
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.getAllReservationByName("john");
        assertEquals(2, result.size());

        result = reservationService.getAllReservationByName("Offer 1");
        assertEquals(2, result.size());

        result = reservationService.getAllReservationByName("jane");
        assertEquals(2, result.size());

        result = reservationService.getAllReservationByName(" ");
        assertEquals(2, result.size());
    }

    @Test
    void testGetReservationById() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation1));

        Optional<Reservation> result = reservationService.getReservationById(1L);
        assertTrue(result.isPresent());
        assertEquals(reservation1, result.get());
    }

    @Test
    void testCanMakeReservation() {
        when(reservationRepository.findByEmployeeId(1L)).thenReturn(Arrays.asList(reservation1, reservation2));

        // Test 1: Kolizja z reservation1 (10:00 i 10:45 zakładając że trwa godzine)
        LocalDateTime newReservationTime = LocalDateTime.of(2024, 7, 15, 10, 45);
        assertFalse(reservationService.canMakeReservation(1L, newReservationTime, 60));

        // Test 2: Brak kolizji (10:00 i 9:30 zakładając że trwa godzine)
        newReservationTime = LocalDateTime.of(2024, 7, 15, 9, 30);
        assertFalse(reservationService.canMakeReservation(1L, newReservationTime, 60));

        // Test 3: Brak kolizji (rezerwacja od 13:00)
        newReservationTime = LocalDateTime.of(2024, 7, 15, 13, 0);
        assertTrue(reservationService.canMakeReservation(1L, newReservationTime, 60));

        // Test 4: Brak kolizji (ta sama godzina inny dzien)
        newReservationTime = LocalDateTime.of(2024, 7, 16, 10, 0);
        assertTrue(reservationService.canMakeReservation(1L, newReservationTime, 60));

        // Test 5: Brak kolizji (ta sama godzina inny miesiac)
        newReservationTime = LocalDateTime.of(2024, 8, 15, 10, 0);
        assertTrue(reservationService.canMakeReservation(1L, newReservationTime, 60));

        // Test 6: Brak kolizji (ta sama godzina inny rok)
        newReservationTime = LocalDateTime.of(2025, 7, 15, 10, 0);
        assertTrue(reservationService.canMakeReservation(1L, newReservationTime, 60));
    }

    @Test
    void testCreateReservation() {
        reservationService.createReservation(reservation1);
        verify(reservationRepository, times(1)).save(reservation1);
    }

    @Test
    void testUpdateReservation() {
        Reservation updatedReservation = new Reservation();
        updatedReservation.setDate(LocalDateTime.of(2024, 7, 16, 10, 0));
        updatedReservation.setReservationStatus(ReservationStatus.PAID);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation1));

        reservationService.updateReservation(1L, updatedReservation);

        assertEquals(LocalDateTime.of(2024, 7, 16, 10, 0), reservation1.getDate());
        assertEquals(ReservationStatus.PAID, reservation1.getReservationStatus());
        verify(reservationRepository, times(1)).save(reservation1);
    }

    @Test
    void testDeleteReservationById() {
        reservationService.deleteReservationById(1L);
        verify(reservationRepository, times(1)).deleteById(1L);
    }
}
