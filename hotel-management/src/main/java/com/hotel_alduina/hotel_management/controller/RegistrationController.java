package com.hotel_alduina.hotel_management.controller;

import com.hotel_alduina.hotel_management.dto.RegistrationForm;
import com.hotel_alduina.hotel_management.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller //Per dare la specifica che la classe è un controller Spring MVC

public class RegistrationController {
    
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration") //Per visualizzare il form
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "/client_/registration"; //Questo return, ritorna il nome della vista Thymeleaf
    }

    @PostMapping("/registration") //Per gesire l'invio dei dati
    public String registrationUser(@ModelAttribute("registrationForm") RegistrationForm form) {
        userService.registraCliente(form);
        return "redirect:/login"; //Queto return mi reinderizza all'accesso
    }
}
