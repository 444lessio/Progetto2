package com.hotel_alduina.hotel_management.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel_alduina.hotel_management.model.Booking;
import com.hotel_alduina.hotel_management.model.Room;
import com.hotel_alduina.hotel_management.model.RoomStatus;
import com.hotel_alduina.hotel_management.repository.BookingRepository;

import com.hotel_alduina.hotel_management.repository.RoomRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional

public class CheckInService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    public void performCheckIn(Long bookingId) {
        //Recupero della prenotazione tramite l'id
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow( () -> new RuntimeException("Prenotazione non trovata"));

        
        if (booking.isCheckedIn()) {
            throw new IllegalStateException("Check-in già effettuato");
        }


        //Aggiorno lo stato della prenotazione
        booking.setCheckedIn(true);

        //Qui invece aggiorno lo stato della camera che è associata, al momento però del check-in
        Room room = booking.getRoom();
        if(room != null) {
            room.setStatus(RoomStatus.OCCUPATA);
            roomRepository.save(room);
        }

        bookingRepository.save(booking);

    }

    public Booking findBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Prenotazione non trovata"));
    }
    
}
