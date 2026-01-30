package com.hotel_alduina.hotel_management.controller;


import com.hotel_alduina.hotel_management.dto.GuestDTO;
import com.hotel_alduina.hotel_management.dto.TaxReportDTO;
import com.hotel_alduina.hotel_management.service.ReportService;
import com.hotel_alduina.hotel_management.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private RoomService roomService;

    @Autowired
    private ReportService reportService;

    //Dashboard per il monitoraggio in tempo reale dell'occupazione
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        //Recupero tutte le camere della catena
        model.addAttribute("rooms", roomService.getAllRooms());
        
        return "admin_/dashboard";
    }

    @GetMapping("/report-questura")
    public String viewQuesturaReport(Model model) {
        List<GuestDTO> guests = reportService.getQuesturaReportData();
        model.addAttribute("guests", guests);

        return "admin_/report-questura";
    }

    //Per esportare il report gionraliero in formato XML
    @GetMapping(value = "/export/questura.xml", produces = "application/xml")
    @ResponseBody //Notazione che restituisce direttamente il contenuto XML invece che una vista
    public String exportQuesturaXml() {
        List<GuestDTO> data = reportService.getQuesturaReportData();

        return reportService.generateQuesturaXml(data);
    }

    @GetMapping("/report-tassa")
    public String viewTaxReport(Model model) {
        List<TaxReportDTO> taxData = reportService.getTouristTaxReportData();
        model.addAttribute("taxReports", taxData);

        return "admin_/report-tassa";
    }

    @GetMapping(value = "/export/tassa-soggiorno.xml", produces = "application/xml")
    @ResponseBody
    public String exportTaxXml() {
        List<TaxReportDTO> data = reportService.getTouristTaxReportData();

        return reportService.generateTassaXml(data);
    }

}
