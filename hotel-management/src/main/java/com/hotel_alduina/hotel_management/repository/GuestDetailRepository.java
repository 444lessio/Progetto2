package com.hotel_alduina.hotel_management.repository;

import com.hotel_alduina.hotel_management.model.GuestDetail;
import com.hotel_alduina.hotel_management.model.Booking;
import org.springframework.data.repository.CrudRepository;
import java.util.Collection;

public interface GuestDetailRepository extends CrudRepository<GuestDetail, Long>{
    
    //Recupero tutti gli ospiti che sono legati a una determinata prenotazione
    Collection<GuestDetail> findByBooking(Booking booking);

    Collection<GuestDetail> findByBookingAndIsLeaderTrue(Booking booking);
}
