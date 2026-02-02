package com.hotel_alduina.hotel_management.controller;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.hotel_alduina.hotel_management.dto.GuestDTO;
import com.hotel_alduina.hotel_management.model.Booking;
import com.hotel_alduina.hotel_management.model.GuestDetail;
import com.hotel_alduina.hotel_management.service.CheckInService;
import com.hotel_alduina.hotel_management.service.GuestDetailService;


@Controller
@RequestMapping("/stay/check-in")
public class CheckInController {
    private final CheckInService checkInService;
    private final GuestDetailService guestDetailService;

    public CheckInController(CheckInService checkInService, GuestDetailService guestDetailService) {
        this.checkInService = checkInService;
        this.guestDetailService = guestDetailService;
    }

    // ⭐ NUOVA VERSIONE - Mostra solo riepilogo, non form di modifica
    @GetMapping("/{bookingId}")
    public String showCheckInSummary(@PathVariable Long bookingId, Model model) {
        Booking booking = checkInService.findBookingById(bookingId);
        if (booking == null) {
            return "redirect:/client/dashboard";
        }

        // Controlla se il check-in è già stato fatto
        if (booking.isCheckedIn()) {
            model.addAttribute("error", "Check-in già effettuato per questa prenotazione");
            return "redirect:/client/dashboard";
        }

        // Recupera gli ospiti già salvati durante la prenotazione
        List<GuestDetail> guests = guestDetailService.findByBooking(booking);
        
        // Converti in DTO per la visualizzazione
        List<GuestDTO> guestDTOs = guests.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());

        model.addAttribute("booking", booking);
        model.addAttribute("guests", guestDTOs);
        
        return "stay/check-in"; // <-- NUOVA VISTA (vedi sotto)
    }

    // ⭐ SEMPLIFICA la conferma - non accetta più dati del form
    @PostMapping("/conferma")
    public String confirmCheckIn(@RequestParam Long bookingId) {
        // Esegui solo il check-in, senza salvare nuovi ospiti
        checkInService.performCheckIn(bookingId);
        
        return "redirect:/stay/check-in/successo?bookingId=" + bookingId;
    }

    @GetMapping("/successo")
    public String checkInSuccess(@RequestParam("bookingId") Long bookingId, Model model) {
        Booking booking = checkInService.findBookingById(bookingId);
        
        if (booking == null) {
            return "redirect:/client/dashboard";
        }

        model.addAttribute("booking", booking);
        return "stay/check-in-success";
    }

    private GuestDTO convertToDTO(GuestDetail guest) {
        GuestDTO dto = new GuestDTO();
        dto.setFirstName(guest.getFirstName());
        dto.setLastName(guest.getLastName());
        dto.setCitizenship(guest.getCitizenship());
        dto.setBirthPlace(guest.getBirthPlace());
        dto.setBirthDate(guest.getBirthDate().toString());
        dto.setLeader(guest.isLeader());
        dto.setExemptionType(guest.getExemptionType());
        dto.setDocumentType(guest.getDocumentType());
        dto.setDocumentNumber(guest.getDocumentNumber());
        return dto;
    }
}