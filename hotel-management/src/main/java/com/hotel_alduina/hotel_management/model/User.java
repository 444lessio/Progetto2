package com.hotel_alduina.hotel_management.model;

import jakarta.persistence.Column; /*Per permette di far comunicare le classi Java con le tabelle del DB */
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity /*Questa annotazione mi indica che questa classe rappresenta una tabella del DB */
@Table(name = "Utenti") 
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {
    
    @Id /*Per specificare quale campo rappresenta la chiave primaria della tabella */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; /*La mia chiave primaria */

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
    
    @Column(unique = true, nullable = false)
    private String email;

    //Ora definisco il ruolo cosi da gestire i diversi accessi
    /* CLIENTE, PERSONALE, GESTORE */

    @Enumerated(EnumType.STRING)
    @Column(name = "ruolo", nullable = false)
    private Role role;

    //Dati Anagrafici
    @Column(name = "nome", nullable = false)
    private String firstName;
    @Column(name = "cognome", nullable = false)
    private String lastName;
    @Column(name = "cittadinanza", nullable = false)
    private String citizenship;
}
