package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Reservation;
import com.example.sportcenterv1.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;


    public List<Reservation> getAllReservation(){

        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(Long reservationID){

        return reservationRepository.findById(reservationID);
    }

    public void createReservation(Reservation reservation){

        reservationRepository.save(reservation);
    }

    public void updateReservation(Long reservationID, Reservation updateReservation) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationID);

        if (optionalReservation.isPresent()) {
            Reservation saveReservation = optionalReservation.get();
            LocalDateTime existingDateTime = saveReservation.getDate();

            if (updateReservation.getDate() != null) {
                LocalDate updateDate = updateReservation.getDate().toLocalDate();
                LocalTime updateTime = updateReservation.getDate().toLocalTime();

                if (updateTime.equals(LocalTime.MIDNIGHT) && updateDate.equals(LocalDate.of(1970, 1, 1))) {
                    // Aktualizuj sam czas
                    saveReservation.setDate(LocalDateTime.of(existingDateTime.toLocalDate(), updateTime));
                } else if (updateTime.equals(LocalTime.MIDNIGHT)) {
                    // Aktualizuj samą datę
                    saveReservation.setDate(LocalDateTime.of(updateDate, existingDateTime.toLocalTime()));
                } else {
                    // Aktualizuj pełną datę i czas
                    saveReservation.setDate(updateReservation.getDate());
                }
            }

            if (updateReservation.getReservationStatus() != null) {
                saveReservation.setReservationStatus(updateReservation.getReservationStatus());
            }
            if (updateReservation.getOffer() != null) {
                saveReservation.setOffer(updateReservation.getOffer());
            }
            if (updateReservation.getClient() != null) {
                saveReservation.setClient(updateReservation.getClient());
            }
            if (updateReservation.getEmployee() != null) {
                saveReservation.setEmployee(updateReservation.getEmployee());
            }
            if (updateReservation.getSpace() != null) {
                saveReservation.setSpace(updateReservation.getSpace());
            }

            reservationRepository.save(saveReservation);
        }
    }

    public void deleteReservationById(Long reservationID){

        reservationRepository.deleteById(reservationID);
    }
}
