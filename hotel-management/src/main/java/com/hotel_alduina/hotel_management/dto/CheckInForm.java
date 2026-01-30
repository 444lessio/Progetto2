package com.hotel_alduina.hotel_management.dto;

import lombok.Data;
import java.util.List;

@Data

public class CheckInForm {
    private Long bookingId;

    //Dati del capogruppo
    private String documentType;
    private String documentNumber;

    //Lista del DTO per gli ospiti, dove è incluso anche il capogruppo come primo elemento della lista
    private List<GuestDTO> guests;
}
