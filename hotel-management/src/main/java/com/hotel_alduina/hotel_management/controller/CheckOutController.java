package com.hotel_alduina.hotel_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hotel_alduina.hotel_management.dto.GuestDTO;
import com.hotel_alduina.hotel_management.model.Booking;
import com.hotel_alduina.hotel_management.model.GuestDetail;
import com.hotel_alduina.hotel_management.service.BookingService;
import com.hotel_alduina.hotel_management.service.GuestDetailService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/stay/check-out")
public class CheckOutController {

    private final BookingService bookingService;
    private final GuestDetailService guestDetailService; // nuovo servizio per i GuestDetail

    public CheckOutController(BookingService bookingService, GuestDetailService guestDetailService) {
        this.bookingService = bookingService;
        this.guestDetailService = guestDetailService;
    }

    @GetMapping("/{bookingId}")
    public String showCheckOutPage(@PathVariable Long bookingId, Model model) {
        Booking booking = bookingService.findBookingById(bookingId);
        if (booking == null) {
            return "redirect:/error";
        }

        // Carico i guest associati e li trasformo in DTO
        List<GuestDTO> guestDetails = guestDetailService.findByBooking(booking)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        model.addAttribute("booking", booking);
        model.addAttribute("guestDetails", guestDetails); // <-- passo solo i DTO
        return "stay/check-out";
    }

    @PostMapping("/conferma")
    public String processCheckOut(@RequestParam Long bookingId, Model model) {
        Booking booking = bookingService.performCheckOut(bookingId);

        // Carico di nuovo i guest per mostrare conferma
        List<GuestDTO> guestDTOs = guestDetailService.findByBooking(booking)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        model.addAttribute("booking", booking);
        model.addAttribute("guestDTOs", guestDTOs);
        return "stay/check-out-success"; // assicurati che esista la view
    }

    private GuestDTO convertToDTO(GuestDetail guest) {
        GuestDTO dto = new GuestDTO();
        dto.setFirstName(guest.getFirstName());
        dto.setLastName(guest.getLastName());
        dto.setBirthDate(guest.getBirthDate().toString()); // puoi formattare come vuoi
        dto.setBirthPlace(guest.getBirthPlace());
        dto.setCitizenship(guest.getCitizenship());
        dto.setLeader(guest.isLeader());
        dto.setExemptionType(guest.getExemptionType());
        return dto;
    }
}
