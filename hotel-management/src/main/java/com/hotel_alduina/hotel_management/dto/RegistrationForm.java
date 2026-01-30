package com.hotel_alduina.hotel_management.dto;

import lombok.Data;

@Data

public class RegistrationForm {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String citizenship;
}
