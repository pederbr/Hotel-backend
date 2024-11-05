package com.brennum.hotel.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brennum.hotel.api.model.AuditLog;
import com.brennum.hotel.api.model.Booking;
import com.brennum.hotel.api.model.Customer;
import com.brennum.hotel.api.model.Room;
import com.brennum.hotel.db.BookingRepository;
import com.brennum.hotel.db.CustomerRepository;
import com.brennum.hotel.db.RoomRepository;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final CustomerRepository customerRepository;
    private final AuditLogService auditLogService;

    public BookingService(BookingRepository bookingRepository,
                         RoomRepository roomRepository,
                         CustomerRepository customerRepository,
                         AuditLogService auditLogService) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.customerRepository = customerRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public Booking createBooking(int userID, int roomID, String startString, String endString) {

        LocalDateTime start = LocalDateTime.parse(startString);
        LocalDateTime end = LocalDateTime.parse(endString);
        
        Room room = roomRepository.findById(roomID)
            .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        
        Customer customer = customerRepository.findById(userID)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        if (room.getStatus() != Room.RoomStatus.AVAILABLE) {
            throw new IllegalStateException("Room is not available");
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Check-in date must be before check-out date");
        }

        if (hasConflictingBookings(room.getId(), start, end)) {
            throw new IllegalStateException("Room is already booked for these dates");
        }

        // Calculate total price
        long nightsStay = (end.getDayOfYear() - start.getDayOfYear());
        double totalPrice = room.getPricePerNight() * nightsStay;

        // Update room status
        room.setStatus(Room.RoomStatus.OCCUPIED);
        roomRepository.save(room);

        // Create booking
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setCheckIn(start);
        booking.setCheckOut(end);
        booking.setTotalPrice(totalPrice);
        
        //save booking
        Booking savedBooking = bookingRepository.save(booking);

        // Create audit log
        auditLogService.createAuditLog(AuditLog.ActionType.CREATE, AuditLog.EntityType.BOOKING, savedBooking.getId(), customer);

        // Set booking status to confirmed
        savedBooking.setStatus(Booking.BookingStatus.CONFIRMED);

        return savedBooking;
    }

    private boolean hasConflictingBookings(Integer roomId, LocalDateTime checkIn, LocalDateTime checkOut) {
        return bookingRepository.findConflictingBookings(roomId, checkIn, checkOut).iterator().hasNext();
    }

    @Transactional
    public Booking cancelBooking(Integer bookingID) {
        
        Booking booking = bookingRepository.findById(bookingID)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        
        // Free up the room
        Room room = booking.getRoom();
        room.setStatus(Room.RoomStatus.AVAILABLE);
        roomRepository.save(room);

        auditLogService.createAuditLog(AuditLog.ActionType.STATUS_CHANGE, AuditLog.EntityType.BOOKING, booking.getId(), booking.getCustomer());

        // Save booking
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking updateBooking(int bookingID, int userID, int roomID, String startString, String endString) {
        Booking booking = bookingRepository.findById(bookingID)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking is cancelled");
        }

        if (booking.getStatus() == Booking.BookingStatus.CHECKED_IN) {
            throw new IllegalStateException("Booking is checked in");
        }

        if (booking.getStatus() == Booking.BookingStatus.CHECKED_OUT) {
            throw new IllegalStateException("Booking is checked out");
        }

        cancelBooking(bookingID);


        return createBooking(userID, roomID, startString, endString);
    } 
}