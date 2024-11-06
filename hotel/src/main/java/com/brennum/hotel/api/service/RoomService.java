package com.brennum.hotel.api.service;

import org.springframework.stereotype.Service;

import com.brennum.hotel.api.model.AuditLog;
import com.brennum.hotel.api.model.Room;
import com.brennum.hotel.db.RoomRepository;

@Service
public class RoomService {
    
    private final RoomRepository roomRepository;
    private final AuditLogService auditLogService;

    public RoomService(RoomRepository roomRepository, AuditLogService auditLogService) {
        this.roomRepository = roomRepository;
        this.auditLogService = auditLogService;
    }

    public Room createRoom(String roomNumber, String type, String status, double pricePerNight) {
        if(roomRepository.existsByRoomNumber(roomNumber)) {
            throw new IllegalArgumentException("Room number already exists");
        }
        Room r = new Room();
        r.setRoomNumber(roomNumber);
        r.setRoomType(Room.RoomType.valueOf(type));
        r.setStatus(Room.RoomStatus.valueOf(status));
        r.setPricePerNight(pricePerNight);
        roomRepository.save(r);
        auditLogService.createAuditLog(AuditLog.ActionType.CREATE, AuditLog.EntityType.ROOM, r.getId(), null);
        return r;
    }


}
