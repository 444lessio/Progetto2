package com.hotel_alduina.hotel_management.dto;

import lombok.Data;

@Data
public class GuestDTO {
    private String firstName;
    private String lastName;
    private String citizenship;
    private String birthPlace;
    private String birthDate; //String per facilitare quello che sarà il mapping dai form HTML
    private boolean isLeader;
    private String exemptionType;
}
