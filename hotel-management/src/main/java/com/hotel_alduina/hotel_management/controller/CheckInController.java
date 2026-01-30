package com.hotel_alduina.hotel_management.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hotel_alduina.hotel_management.dto.CheckInForm;
import com.hotel_alduina.hotel_management.dto.GuestDTO;
import com.hotel_alduina.hotel_management.model.Booking;
import com.hotel_alduina.hotel_management.model.GuestDetail;
import com.hotel_alduina.hotel_management.model.User;
import com.hotel_alduina.hotel_management.service.CheckInService;


@Controller
@RequestMapping("/stay/check-in")

public class CheckInController {
    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @GetMapping("{bookingId}") //PathVariable per identificare la prenotazione
    public String showCheckInForm(@PathVariable Long bookingId, Model model) {
        Booking booking = checkInService.findBookingById(bookingId);
        if (booking == null) {
            return "redirect:/client/dashboard";
        }

        CheckInForm form = new CheckInForm();
        form.setBookingId(bookingId);

        List<GuestDTO> guestList = new ArrayList<>();
        int numberOfGuest = booking.getNumGuests();

        for (int i = 0; i< numberOfGuest; i++) {
            GuestDTO guest = new GuestDTO();

            if(i == 0){
                guest.setLeader(true);

                User customer = booking.getCustomer();
                if(customer != null) {
                    guest.setFirstName(customer.getFirstName());
                    guest.setLastName(customer.getLastName());
                    guest.setCitizenship(customer.getCitizenship());
                }
            } else {
                    guest.setLeader(false);
            }

                guestList.add(guest);
        }

        form.setGuests(guestList);

        model.addAttribute("checkInForm", form);
        return "stay/check-in";
    }

    @PostMapping("/conferma")
    public String processCheckIn(@ModelAttribute CheckInForm form) {
        Collection<GuestDetail> guestEntities =
                convertToEntities(form.getGuests());

        checkInService.processCheckIn(form.getBookingId(), guestEntities);

        return "redirect:/stay/check-in/successo?bookingId=" +  form.getBookingId();
    }

    @GetMapping("/successo")
    public String checkInSuccess(@RequestParam("bookingId") Long bookingId, Model model) {
        // Recupera il booking dal servizio
        Booking booking = checkInService.findBookingById(bookingId);

        if (booking == null) {
            // gestione errore, redirect o pagina di errore
            return "redirect:/stay";
        }

        model.addAttribute("booking", booking);
        return "stay/check-in-success";
    }



    //Definisco un metodo private per mappare i dati della vista verso gli oggetti del dominio 

    private Collection<GuestDetail> convertToEntities(List<GuestDTO> guestDTOs) {
        if (guestDTOs == null) return Collections.emptyList();

        return guestDTOs.stream().map(dto -> {
            GuestDetail entity = new GuestDetail();
            //Mappatura dei campi anagrafici
            entity.setFirstName(dto.getFirstName());
            entity.setLastName(dto.getLastName());
            entity.setCitizenship(dto.getCitizenship());
            entity.setBirthPlace(dto.getBirthPlace());

            //Converto la data da String a LocalDate per JPA    
            if (dto.getBirthDate() != null && !dto.getBirthDate().isEmpty()) {
                entity.setBirthDate(LocalDate.parse(dto.getBirthDate()));
            }

            //Dati per la questura e relativi alla tassa di soggiorno
            entity.setLeader(dto.isLeader());
            entity.setExemptionType(dto.getExemptionType());

            return entity;
        }).collect(Collectors.toList());
        

    }



}
