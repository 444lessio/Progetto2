package com.hotel_alduina.hotel_management.service;

import com.hotel_alduina.hotel_management.dto.GuestDTO;
import com.hotel_alduina.hotel_management.model.Booking;
import com.hotel_alduina.hotel_management.model.GuestDetail;
import com.hotel_alduina.hotel_management.repository.GuestDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuestDetailService {

    private final GuestDetailRepository guestDetailRepository;

    public GuestDetailService(GuestDetailRepository guestDetailRepository) {
        this.guestDetailRepository = guestDetailRepository;
    }

    // 🔹 Recupera tutti gli ospiti di una prenotazione
    @Transactional(readOnly = true)
    public List<GuestDetail> findByBooking(Booking booking) {
        return guestDetailRepository.findByBooking(booking)
                .stream()
                .collect(Collectors.toList());
    }

    // 🔹 Recupera il capogruppo di una prenotazione
    @Transactional(readOnly = true)
    public GuestDetail findLeaderByBooking(Booking booking) {
        return guestDetailRepository.findByBookingAndIsLeaderTrue(booking)
                .stream()
                .findFirst()
                .orElse(null);
    }

    // 🔹 Salva un guest a partire da un DTO
    @Transactional
    public GuestDetail saveGuest(GuestDTO guestDTO, Booking booking) {
        GuestDetail guest = new GuestDetail();
        guest.setFirstName(guestDTO.getFirstName());
        guest.setLastName(guestDTO.getLastName());
        guest.setCitizenship(guestDTO.getCitizenship());
        guest.setBirthPlace(guestDTO.getBirthPlace());
        guest.setBirthDate(LocalDate.parse(guestDTO.getBirthDate())); // attenzione al formato yyyy-MM-dd
        guest.setLeader(guestDTO.isLeader());
        guest.setExemptionType(guestDTO.getExemptionType());
        guest.setBooking(booking);

        return guestDetailRepository.save(guest);
    }

    // 🔹 Salva una lista di guest da DTO
    @Transactional
    public List<GuestDetail> saveGuests(List<GuestDTO> guestDTOs, Booking booking) {
        return guestDTOs.stream()
                .map(dto -> saveGuest(dto, booking))
                .collect(Collectors.toList());
    }

    // 🔹 Elimina tutti i guest di una prenotazione
    @Transactional
    public void deleteByBooking(Booking booking) {
        findByBooking(booking).forEach(guestDetailRepository::delete);
    }
}
