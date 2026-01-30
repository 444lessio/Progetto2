package com.hotel_alduina.hotel_management.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "occupanti")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class GuestDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Dati richiesti e utili per la questure e per la tassa di soggiorno
    @Column(name = "nome", nullable = false, length = 64)
    private String firstName;

    @Column(name = "cognome", nullable = false, length = 64)
    private String lastName;

    @Column(name = "cittadinanza", nullable = false)
    private String citizenship;

    @Column(name = "luogo_nascita", nullable = false)
    private String birthPlace;

    @Column(name = "data_nascita", nullable = false)
    private LocalDate birthDate;

    //La questure richiede un report dal capogruppo, quindi definiamo i dati specifici per il capogruppo
    @Column(name = "is_capogruppo")
    private boolean isLeader; //Campo per identificare se è il capogruppo

    @Column(name = "tipo_documento")
    private String documentType;

    @Column(name = "numero_documento")
    private String documentNumber;

    //Gestione della tassa di soggiorno
    @Column(name = "tipo_esenzione")
    private String exemptionType; //campo per un eventuale esenzione nel caso di diasbilità, età, o se nessuna esenzione

    @ManyToOne
    @JoinColumn(name = "id_prenotazione", nullable = false)
    private Booking booking; //Collegamento alla prenotazione
}
