package com.brennum.hotel.db;

import com.brennum.hotel.api.model.Room;
import com.brennum.hotel.api.model.Room.RoomStatus;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoomRepository extends CrudRepository<Room, Integer> {
    Iterable<Room> findByStatus(RoomStatus status);
    Optional<Room> findByRoomNumber(String roomNumber);
}