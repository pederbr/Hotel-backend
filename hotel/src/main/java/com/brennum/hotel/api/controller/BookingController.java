package com.brennum.hotel.api.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brennum.hotel.api.model.Booking;
import com.brennum.hotel.api.service.BookingService;
import com.brennum.hotel.db.BookingRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired private BookingRepository bookingRepository;
    @Autowired private BookingService bookingService;

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Iterable<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingRepository.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Booking> getBookingById(@PathVariable Integer id) {
        return bookingRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Iterable<Booking>> getBookingsByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.ok(bookingRepository.findByCustomerId(customerId));
    }

    @GetMapping("/room/{roomId}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Iterable<Booking>> getBookingsByRoom(@PathVariable Integer roomId) {
        return ResponseEntity.ok(bookingRepository.findByRoomId(roomId));
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(
        @Valid @RequestParam int userId, 
        @Valid @RequestParam int roomId,
        @RequestParam String start,
        @RequestParam String end
        
    ) {
        Booking created = bookingService.createBooking(userId, roomId, start, end);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable Integer oldBookingId, 
            @Valid @RequestParam int newUserId, 
            @Valid @RequestParam int newRoomId,
            @RequestParam String newStart,
            @RequestParam String newEnd
            ) {
        Booking updated = bookingService.updateBooking(oldBookingId, newUserId, newRoomId, newStart, newEnd);
        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Integer id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
    public static void main(String[] args) {
        System.out.println(LocalDateTime.now());
    }
}
