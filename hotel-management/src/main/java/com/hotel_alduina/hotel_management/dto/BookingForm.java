package com.hotel_alduina.hotel_management.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;



import lombok.Data;


@Data

public class BookingForm {
    private Long structureId;
    @NotNull(message = "La data di arrivo è obbligatoria")
    private LocalDate startDate;
    @NotNull(message = "La data di partenza è obbligatoria")
    private LocalDate endDate;
    private int numGuests;
    private String additionalServices;
    private Long roomId;
}
