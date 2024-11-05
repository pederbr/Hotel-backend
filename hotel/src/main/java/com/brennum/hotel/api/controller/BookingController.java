package com.brennum.hotel.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brennum.hotel.api.model.Booking;
import com.brennum.hotel.api.service.AuditLogService;
import com.brennum.hotel.api.service.BookingService;
import com.brennum.hotel.db.BookingRepository;
import com.brennum.hotel.db.CustomerRepository;
import com.brennum.hotel.db.RoomRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    @Autowired private final BookingRepository bookingRepository;
    @Autowired private final BookingService bookingService;
    
    @Autowired private final RoomRepository roomRepository;
    @Autowired private final CustomerRepository customerRepository;
    @Autowired private final AuditLogService auditLogService;


    public BookingController(BookingRepository bookingRepository, RoomRepository roomRepository, CustomerRepository customerRepository, AuditLogService auditLogService) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.customerRepository = customerRepository;
        this.auditLogService = auditLogService;
        this.bookingService = new BookingService(bookingRepository, roomRepository, customerRepository, auditLogService);
    }

    @GetMapping
    public ResponseEntity<Iterable<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Integer id) {
        return bookingRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Iterable<Booking>> getBookingsByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.ok(bookingRepository.findByCustomerId(customerId));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Iterable<Booking>> getBookingsByRoom(@PathVariable Integer roomId) {
        return ResponseEntity.ok(bookingRepository.findByRoomId(roomId));
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(
        @Valid @RequestBody int userId, 
        @Valid @RequestBody int roomId,
        @RequestBody String start,
        @RequestBody String end
        
    ) {
        Booking created = bookingService.createBooking(userId, roomId, start, end);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable Integer oldBookingId, 
            @Valid @RequestBody int newUserId, 
            @Valid @RequestBody int newRoomId,
            @RequestBody String newStart,
            @RequestBody String newEnd
            ) {
        Booking updated = bookingService.updateBooking(oldBookingId, newUserId, newRoomId, newStart, newEnd);
        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Integer id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
}
