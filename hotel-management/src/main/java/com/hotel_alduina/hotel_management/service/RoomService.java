package com.hotel_alduina.hotel_management.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel_alduina.hotel_management.model.Room;
import com.hotel_alduina.hotel_management.model.RoomStatus;
import com.hotel_alduina.hotel_management.repository.RoomRepository;

import jakarta.transaction.Transactional;


@Service
@Transactional

public class RoomService {
    
    @Autowired
    private RoomRepository roomRepository;

    public Room updateRoomStatus(Long roomId, RoomStatus newStatus) {
        Room room = roomRepository.findById(roomId)
            .orElseThrow( () -> new RuntimeException("Camera con ID" + roomId + "non trovata"));
            room.setStatus(newStatus);
            return roomRepository.save(room);
    }

    public void addNoteRoom(Long roomId, String note) {
        Room room = roomRepository.findById(roomId)
            .orElseThrow( () -> new RuntimeException("Camera non trovata"));
            room.setNotes(note);
            roomRepository.save(room);
        
    }

    public void markRoomToClean(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new RuntimeException("Camera non trovata"));
        room.setStatus(RoomStatus.DA_PULIRE);
        room.setNotes(null);
        roomRepository.save(room);
    }

    public Collection<Room> getAvailableRooms(Long structureId, LocalDate startDate, LocalDate endDate) {

        return roomRepository.findByStructureId(structureId)
                .stream()
                .filter(room -> room.getStatus() == RoomStatus.LIBERA)
                .collect(Collectors.toList());
    }

    public Collection<Room> getAllRoomsByStructure(Long structureId) {
       return roomRepository.findByStructureId(structureId);
    }

    public Collection<Room> getAllRooms() {
        return (Collection<Room>) roomRepository.findAll();
    }
}
