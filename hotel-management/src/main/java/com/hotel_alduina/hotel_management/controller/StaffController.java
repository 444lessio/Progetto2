package com.hotel_alduina.hotel_management.controller;

import com.hotel_alduina.hotel_management.model.Room;
import com.hotel_alduina.hotel_management.model.RoomStatus;
import com.hotel_alduina.hotel_management.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;



@Controller
@RequestMapping("/staff") //Questo è l'endpoint protetto per il personale
public class StaffController {
    
  private final RoomService roomService;

  public StaffController(RoomService roomService) {
    this.roomService = roomService;

  }

  //Visualizza la dashboard del personale che contiene la lista delle camere, necessaria per vederne lo stato
  @GetMapping("/dashboard")
  public String showDashboard(Model model) {

    Collection<Room> allRooms = roomService.getAllRooms();
    model.addAttribute("rooms", allRooms);

    return "staff/dashboard"; 
  }

  @PostMapping("/pulizia/conferma")
  public String markAsCleaned(@RequestParam("roomId") Long roomId) {
    //Cambia lo stato della camera da DA_PULIRE a LIBERA
    roomService.updateRoomStatus(roomId, RoomStatus.LIBERA);

    return "redirect:/staff/dashboard";
  }

  @GetMapping("/note")
  public String viewClientNotes(Model model) {

    Collection<Room> roomWithNotes = roomService.getAllRooms();

    //Aggiungiamo la collezione al modelo con request
    model.addAttribute("rooms", roomWithNotes);

    return "staff/dashboard";
  }
}
