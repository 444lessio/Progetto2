package com.hotel_alduina.hotel_management.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hotel_alduina.hotel_management.dto.GuestDTO;
import com.hotel_alduina.hotel_management.dto.TaxReportDTO;
import com.hotel_alduina.hotel_management.model.Booking;
import com.hotel_alduina.hotel_management.model.GuestDetail;
import com.hotel_alduina.hotel_management.repository.BookingRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReportService {
    @Autowired
    private BookingRepository bookingRepository;


    public List<GuestDTO> getQuesturaReportData() {
        List<Booking> activeBookings = bookingRepository.findByCheckedInTrueAndCheckedOutFalse();
        List<GuestDTO> report = new ArrayList<>();

        for(Booking booking : activeBookings) {
            for(GuestDetail guest : booking.getGuestDetails()) {
                GuestDTO dto = new GuestDTO();
                dto.setFirstName(guest.getFirstName());
                dto.setLastName(guest.getLastName());
                dto.setCitizenship(guest.getCitizenship());
                dto.setBirthPlace(guest.getBirthPlace());

                dto.setBirthDate(guest.getBirthDate().toString());

                report.add(dto);
            }
        }

        return report;
    }

    //Calcola i dati per la tassa di soggiorno
    public List<TaxReportDTO> getTouristTaxReportData() {
        List<Booking> activBookings = bookingRepository.findByCheckedInTrueAndCheckedOutFalse();
        List<TaxReportDTO> taxReports = new ArrayList<>();

        for (Booking booking : activBookings) {
            TaxReportDTO dto = new TaxReportDTO();

            //Troviamo il capogruppo all'interno della lista degli ospiti
            GuestDetail leader = booking.getGuestDetails().stream()
                .filter(GuestDetail::isLeader)
                .findFirst()
                .orElse(null);
            
                if (leader != null) {
                    dto.setHeadOfGroupName(leader.getFirstName() + " " + leader.getLastName());

                    dto.setTotalGuests(booking.getGuestDetails().size());

                    long exemptCount = booking.getGuestDetails().stream()
                            .filter(g -> g.getBirthDate() != null &&
                                    java.time.Period.between(g.getBirthDate(), java.time.LocalDate.now()).getYears() < 12)
                            .count();
                    
                    dto.setExemptionsCount((int) exemptCount);
                    dto.setExemptionType(exemptCount > 0 ? "Minori di 12 anni" : "Nessuna");

                    taxReports.add(dto);
                }
        }

        return taxReports;
    }

    public String generateQuesturaXml(List<GuestDTO> guests) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<reportQuestura>\n");

        for (GuestDTO g : guests) {
            xml.append("  <ospite>\n");
            xml.append("     <nome>").append(g.getFirstName()).append("</nome>\n");
            xml.append("     <congome>").append(g.getLastName()).append("</congome>\n");
            xml.append("     <cittadinanza>").append(g.getCitizenship()).append("</cittadinanza>\n");
            xml.append("     <luogoNascita>").append(g.getBirthPlace()).append("/luogoNascita>\n");
            xml.append("     <dataNascita>").append(g.getBirthDate()).append("</dataNascita>\n");

            if (g.isLeader()) {
                //Campi solo per il capogruppo necessari
                xml.append("    <documento>\n");
                xml.append("      <tipo>").append("CARTA_IDENTITA").append("</tipo>\n");
                xml.append("      <numero>").append("XYZ123").append("</numero>\n");
                xml.append("    </documento>\n");
            }
            xml.append("  </ospite>\n");
        }
        xml.append("</reportQuestura>");

        return xml.toString();
    }

    public String generateTassaXml(List<TaxReportDTO> taxData) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<reportTassaSoggiorno>\n");
    
        for (TaxReportDTO d : taxData) {
            xml.append("  <prenotazione>\n");
            xml.append("    <capogruppo>").append(d.getHeadOfGroupName()).append("</capogruppo>\n");
            xml.append("    <numeroOspiti>").append(d.getTotalGuests()).append("</numeroOspiti>\n");
            xml.append("    <esenzioni>\n");
            xml.append("      <conteggio>").append(d.getExemptionsCount()).append("</conteggio>\n");
            xml.append("      <tipo>").append(d.getExemptionType()).append("</tipo>\n");
            xml.append("    </esenzioni>\n");
            xml.append("  </prenotazione>\n");
        }
        xml.append("</reportTassaSoggiorno>");

        return xml.toString();
    }


}
