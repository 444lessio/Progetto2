package com.hotel_alduina.hotel_management.controller;

import com.hotel_alduina.hotel_management.model.Booking;
import com.hotel_alduina.hotel_management.model.User;
import com.hotel_alduina.hotel_management.repository.BookingRepository;
import com.hotel_alduina.hotel_management.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/client")
public class DashboardController {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public DashboardController(BookingRepository bookingRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();

        // Trovo l'utente loggato
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // Recupero tutte le prenotazioni dell'utente
        List<Booking> userBookings = bookingRepository.findByCustomer(user);

        model.addAttribute("userBookings", userBookings);
        return "/client_/dashboard"; // Thymeleaf template: dashboard.html
    }
}
