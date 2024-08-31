package com.example.sportcenterv1.repository;

import com.example.sportcenterv1.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT SUM(r.offer.price) FROM Reservation r WHERE r.reservationStatus = 'PAID' AND MONTH(r.date) = MONTH(:date) AND YEAR(r.date) = YEAR(:date)")
    Double findTotalIncomeByMonth(@Param("date") LocalDate date);

    @Query("SELECT o.name, COUNT(r) " +
            "FROM Reservation r JOIN r.offer o " +
            "WHERE MONTH(r.date) = MONTH(:date) AND YEAR(r.date) = YEAR(:date) " +
            "GROUP BY o.id " +
            "ORDER BY COUNT(r) DESC")
    List<Object[]> findTopOffersByReservationsInMonth(@Param("date") LocalDate date);

    @Query("SELECT o.name, COUNT(r) FROM Reservation r JOIN r.offer o JOIN o.specializations s WHERE s.id = :specializationId AND MONTH(r.date) = MONTH(:date) AND YEAR(r.date) = YEAR(:date) GROUP BY o.name ORDER BY COUNT(r) DESC")
    List<Object[]> findTopOffersBySpecializationInMonth(@Param("date") LocalDate date, @Param("specializationId") Long specializationId);

    List<Reservation> findByEmployeeId(Long employeeId);
}
