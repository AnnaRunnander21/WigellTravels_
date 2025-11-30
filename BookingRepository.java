package com.example.wigelltravels_.repositories;

import com.example.wigelltravels_.entities.Booking;
import com.example.wigelltravels_.entities.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerId(Long customerId);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByDepartureDateAfter(LocalDate date);

    List<Booking> findByDepartureDateBefore(LocalDate date);
}
