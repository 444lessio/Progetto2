package com.hotel_alduina.hotel_management.repository;

import com.hotel_alduina.hotel_management.model.Room;
import com.hotel_alduina.hotel_management.model.RoomStatus;
import org.springframework.data.repository.CrudRepository;
import java.util.Collection;


public interface RoomRepository extends CrudRepository<Room, Long> {
    
    Collection<Room> findAll();
    Collection<Room> findByStructureId(Long structureId);
    Collection<Room> findByStatus(RoomStatus status); //Utile per il personale
    Collection<Room> findByStructureIdAndStatus(Long structureId, RoomStatus status);
}
