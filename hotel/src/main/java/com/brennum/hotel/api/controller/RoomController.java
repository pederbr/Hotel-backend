package com.brennum.hotel.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brennum.hotel.api.model.Room;
import com.brennum.hotel.api.service.RoomService;
import com.brennum.hotel.db.RoomRepository;



@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    
    @Autowired private RoomRepository roomRepository;
    @Autowired private RoomService roomService;


    @GetMapping()
    @PreAuthorize("hasAuthority('SCOPE_admin')")
        public ResponseEntity<Iterable<Room>> getAllRooms() {
        return ResponseEntity.ok(roomRepository.findAll());
    }

    @GetMapping("available")
    public ResponseEntity<Iterable<Room>> getAvailableRooms() {
        if (!roomRepository.findByStatus(Room.RoomStatus.AVAILABLE).iterator().hasNext()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(roomRepository.findByStatus(Room.RoomStatus.AVAILABLE));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Room> getRoomById(@PathVariable Integer id) {
        return roomRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("available/id/{id}")
    public ResponseEntity<Room> getAvailableRoomById(@PathVariable Integer id) {
        return roomRepository.findById(id)
            .filter(room -> room.getStatus() == Room.RoomStatus.AVAILABLE)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    @GetMapping("available/type/{type}")
    public ResponseEntity<Iterable<Room>> getAvailableRoomsByType(@PathVariable String type) {
        if (!roomRepository.findByStatusAndRoomType(Room.RoomStatus.AVAILABLE, Room.RoomType.valueOf(type)).iterator().hasNext()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(roomRepository.findByStatusAndRoomType(Room.RoomStatus.AVAILABLE, Room.RoomType.valueOf(type)));
    }

    @PostMapping()
    public ResponseEntity<Room> createRoom(
        @RequestParam String roomNumber,
        @RequestParam String type,
        @RequestParam String status,
        @RequestParam double pricePerNight
            ) {
        Room r = roomService.createRoom(roomNumber, type, status, pricePerNight);
        return ResponseEntity.ok(r);
    }

    






    



    
    
    
}
