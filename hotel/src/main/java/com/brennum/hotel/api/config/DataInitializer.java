package com.brennum.hotel.api.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.brennum.hotel.api.model.Room;
import com.brennum.hotel.api.model.Room.RoomType;
import com.brennum.hotel.db.RoomRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final RoomRepository roomRepository;
    
    public DataInitializer(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    
    @Override
    @Transactional
    public void run(String... args) {
        try {
            if (roomRepository.count() == 0) {
                logger.info("Starting room initialization...");
                List<Room> rooms = new ArrayList<>();
                
                // First Floor - Single Rooms (101-106)
                for (int i = 1; i <= 6; i++) {
                    Room room = new Room();
                    room.setRoomNumber(String.format("10%d", i));
                    room.setRoomType(RoomType.SINGLE);
                    room.setPricePerNight(89.99);
                    rooms.add(room);
                }
                
                // Second Floor - Double Rooms (201-208)
                for (int i = 1; i <= 8; i++) {
                    Room room = new Room();
                    room.setRoomNumber(String.format("20%d", i));
                    room.setRoomType(RoomType.DOUBLE);
                    room.setPricePerNight(129.99);
                    rooms.add(room);
                }
                
                // Third Floor - Family Rooms (301-306)
                for (int i = 1; i <= 6; i++) {
                    Room room = new Room();
                    room.setRoomNumber(String.format("30%d", i));
                    room.setRoomType(RoomType.FAMILY);
                    room.setPricePerNight(199.99);
                    rooms.add(room);
                }
                
                roomRepository.saveAll(rooms);
                logger.info("Successfully initialized {} rooms", rooms.size());
            } else {
                logger.info("Rooms already initialized, skipping...");
            }
        } catch (Exception e) {
            logger.error("Error initializing rooms: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize rooms", e);
        }
    }
}