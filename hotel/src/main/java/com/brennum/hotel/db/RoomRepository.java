package com.brennum.hotel.db;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.brennum.hotel.api.model.Room;
import com.brennum.hotel.api.model.Room.RoomStatus;
import com.brennum.hotel.api.model.Room.RoomType;

public interface RoomRepository extends CrudRepository<Room, Integer> {
    Iterable<Room> findByStatus(RoomStatus status);
    Iterable<Room> findByRoomType(RoomType roomType);  
    Iterable<Room> findByStatusAndRoomType(RoomStatus status, RoomType roomType);
    Optional<Room> findByRoomNumber(String roomNumber);
    boolean existsByRoomNumber(String roomNumber);
}