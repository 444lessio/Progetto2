package com.hotel_alduina.hotel_management.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hotel_alduina.hotel_management.model.Booking;
import com.hotel_alduina.hotel_management.model.User;

public interface BookingRepository extends CrudRepository<Booking, Long> {

    Collection<Booking> findAll();
    List<Booking> findByCustomer(User customer);
    List<Booking> findByCheckedInTrueAndCheckedOutFalse();
}
