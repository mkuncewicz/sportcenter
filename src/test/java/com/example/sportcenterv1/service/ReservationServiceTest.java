package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Client;
import com.example.sportcenterv1.entity.Offer;
import com.example.sportcenterv1.entity.Reservation;
import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.entity.enums.ReservationStatus;
import com.example.sportcenterv1.entity.space.Space;
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
    private Client client;
    private Employee employee;
    private Offer offer;
    private Space space;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");

        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Jane");
        employee.setLastName("Smith");

        offer = new Offer();
        offer.setId(1L);
        offer.setName("Special Offer");

        space = new Space() {
            {
                setId(1L);
                setName("Main Hall");
            }
        };

        reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setDate(LocalDateTime.now());
        reservation1.setReservationStatus(ReservationStatus.PENDING);
        reservation1.setClient(client);
        reservation1.setEmployee(employee);
        reservation1.setOffer(offer);
        reservation1.setSpace(space);

        reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setDate(LocalDateTime.now().plusDays(1));
        reservation2.setReservationStatus(ReservationStatus.PAID);
        reservation2.setClient(client);
        reservation2.setEmployee(employee);
        reservation2.setOffer(offer);
        reservation2.setSpace(space);
    }

    @Test
    void testGetAllReservations() {
        List<Reservation> reservations = Arrays.asList(reservation1, reservation2);
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.getAllReservation();
        assertEquals(2, result.size());
        assertEquals(ReservationStatus.PENDING, result.get(0).getReservationStatus());
        assertEquals(ReservationStatus.PAID, result.get(1).getReservationStatus());
    }

    @Test
    void testGetReservationById() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation1));

        Optional<Reservation> result = reservationService.getReservationById(1L);
        assertTrue(result.isPresent());
        assertEquals(ReservationStatus.PENDING, result.get().getReservationStatus());
    }

    @Test
    void testCreateReservation() {
        reservationService.createReservation(reservation1);
        verify(reservationRepository, times(1)).save(reservation1);
    }

    @Test
    void testUpdateReservation() {
        Reservation updatedReservation = new Reservation();
        updatedReservation.setDate(LocalDateTime.now().plusDays(2));
        updatedReservation.setReservationStatus(ReservationStatus.PAID);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation1));
        reservationService.updateReservation(1L, updatedReservation);

        assertEquals(updatedReservation.getDate(), reservation1.getDate());
        assertEquals(ReservationStatus.PAID, reservation1.getReservationStatus());
        verify(reservationRepository, times(1)).save(reservation1);
    }

    @Test
    void testDeleteReservationById() {
        reservationService.deleteReservationById(1L);
        verify(reservationRepository, times(1)).deleteById(1L);
    }
}
