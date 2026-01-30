package com.hotel_alduina.hotel_management.controller;

import com.hotel_alduina.hotel_management.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/soggiorno")

public class StayController {
    
    @Autowired
    private RoomService roomService;

    //Mostra la pagina dove il cliente può inserire la nota
    @GetMapping("/richiesta/{roomId}")
    public String showNoteFrom(@PathVariable Long roomId, Model model) {
        model.addAttribute("roomId", roomId);
        
        return "stay/inserimento-nota";
    }

    //Gestisco qui l'invio della nota
    @PostMapping("/richiesta/invia")
    public String submitNote(@RequestParam Long roomId, @RequestParam String nota) {
        roomService.addNoteRoom(roomId, nota);

        return "redirect:/soggiorno/conferma";
    }

    @GetMapping("/conferma")
    public String showSuccessPage() {
        return "stay/conferma-invio";
    }
}
