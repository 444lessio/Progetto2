package com.hotel_alduina.hotel_management.controller;

import com.hotel_alduina.hotel_management.dto.BookingForm;
import com.hotel_alduina.hotel_management.model.Room;
import com.hotel_alduina.hotel_management.service.BookingService;
import com.hotel_alduina.hotel_management.service.RoomService;
import com.hotel_alduina.hotel_management.service.StructureService;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
@RequestMapping("/prenota") // Questa è la path di base per tutte le operazioni di prenotazione
public class BookingController {
    private final BookingService bookingService;
    private final StructureService structureService;
    private final RoomService roomService;

    public BookingController(BookingService bookingService,
            StructureService structureService,
            RoomService roomService) {
        this.bookingService = bookingService;
        this.structureService = structureService;
        this.roomService = roomService;
    }

    /*
     * Mostra il form di ricerca iniziale e
     * Carica la lsita delle strutture per permettere la selezione nel menu a
     * tendina
     */
    @GetMapping("/ricerca")
    public String showSearchForm(Model model) {
        model.addAttribute("bookingForm", new BookingForm());
        model.addAttribute("structures", structureService.getAllStructures());
        return "booking/search";
    }

    /*
     * Mostra i risultati della ricerca
     * Filtra le camere disponibili in base a quale struttura viene selezionata e
     * alle date selezionate
     */

    @GetMapping("/disponibilita")
    public String searchRooms(@ModelAttribute BookingForm form, Model model) {
        // Qui richiamo la logica per recuperare le camere disponibili, logica delegata
        // al Service
        Collection<Room> availableRooms = roomService.getAvailableRooms(
                form.getStructureId(),
                form.getStartDate(),
                form.getEndDate());

        model.addAttribute("availableRooms", availableRooms);
        model.addAttribute("bookingForm", form);
        return "booking/selection";
    }

    /*
     * @GetMapping("/servizi/{roomId}")
     * public String showServiceForm(@PathVariable Long roomId,
     * 
     * @ModelAttribute("bookingForm") BookingForm form,
     * Model model) {
     * form.setRoomId(roomId);
     * model.addAttribute("bookingForm", form);
     * return "booking/selection";
     * }
     */

    /* Conferma della prenotazione e riepilogo dei costi */
    @PostMapping("/riepilogo")
    public String showSummary(@Valid @ModelAttribute BookingForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
        model.addAttribute("structures", structureService.getAllStructures());
        return "booking/search";
    }

        Double totalCost = bookingService.calculateTotalCost(form);
        model.addAttribute("booking", form);
        model.addAttribute("totalCost", totalCost);
        return "booking/confirmation";
    }

    /* Salvataggio nel DB */
    @PostMapping("/conferma")
    public String confirmBooking(@ModelAttribute BookingForm form,  BindingResult result, Authentication authentication, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("structures", structureService.getAllStructures());
            return "booking/search"; // torna al form di ricerca
        }

        bookingService.saveBooking(form, authentication.getName());
        return "redirect:/prenota/successo";
    }

    @GetMapping("/successo")
    public String bookingSuccess() {
        return "booking/success";
    }
}
