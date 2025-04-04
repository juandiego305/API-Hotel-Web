package com.juancho12.Api_Hotel.services;

import com.juancho12.Api_Hotel.models.Room;
import com.juancho12.Api_Hotel.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room updateRoom(Long id, Room roomDetails) {
        return roomRepository.findById(id).map(room -> {
            room.setType(roomDetails.getType());
            room.setPrice(roomDetails.getPrice());
            room.setCapacity(roomDetails.getCapacity());
            return roomRepository.save(room);
        }).orElseThrow(() -> new RuntimeException("Habitación no encontrada"));
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}

