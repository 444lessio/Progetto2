package com.hotel_alduina.hotel_management.dto;

import lombok.Data;

@Data
public class GuestDTO {
    private String firstName;
    private String lastName;
    private String citizenship;
    private String birthPlace;
    private String birthDate; 
    private boolean leader;
    private String exemptionType;
    private String documentType;
    private String documentNumber;
}
