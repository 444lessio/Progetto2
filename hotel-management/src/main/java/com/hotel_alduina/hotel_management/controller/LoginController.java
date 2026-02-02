package com.hotel_alduina.hotel_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class LoginController {
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Username o password errati");
        }

        if (logout != null) {
            model.addAttribute("message", "Disconnessione avvenuta con successo");
        }

        
        return "client_/login";
                        }
}

