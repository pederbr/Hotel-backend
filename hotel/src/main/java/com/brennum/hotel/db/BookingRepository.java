package com.brennum.hotel.db;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.brennum.hotel.api.model.Booking;

public interface BookingRepository extends CrudRepository<Booking, Integer> {
    Iterable<Booking> findByCustomerId(Integer customerId);
    Iterable<Booking> findByRoomId(Integer roomId);
    
    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId AND ((b.checkIn BETWEEN :startDate AND :endDate) OR (b.checkOut BETWEEN :startDate AND :endDate))")
    Iterable<Booking> findConflictingBookings(
        @Param("roomId") Integer roomId, 
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );
}