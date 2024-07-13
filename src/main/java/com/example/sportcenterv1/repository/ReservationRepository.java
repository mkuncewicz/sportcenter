package com.example.sportcenterv1.repository;

import com.example.sportcenterv1.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT SUM(r.offer.price) FROM Reservation r WHERE r.reservationStatus = 'PAID' AND MONTH(r.date) = MONTH(:date) AND YEAR(r.date) = YEAR(:date)")
    Double findTotalIncomeByMonth(@Param("date") LocalDate date);
}
