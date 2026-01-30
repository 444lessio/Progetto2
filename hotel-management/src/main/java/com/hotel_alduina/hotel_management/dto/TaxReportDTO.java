package com.hotel_alduina.hotel_management.dto;

import lombok.Data;

@Data

public class TaxReportDTO {
    private String headOfGroupName;
    private int totalGuests;
    private int exemptionsCount;
    private String exemptionType;
}
