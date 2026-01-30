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

        //Il return restituisce il nome della vista Thymeleaf templates/login.html
        return "client_/login";
                        }
}

/*Non creo il POST dato che in Spring Security la gestione del form di login
  viene configurata centralmente nella classe di configurazioned della sicurezza
  Quindi in questo caso il controller lo uso solamente per le richieste GET*/