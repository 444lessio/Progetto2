package com.hotel_alduina.hotel_management.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Entity
@Table(name = "camere")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, length = 10)
    private String roomNumber;

    @Column(name = "prezzo_notte")
    private Double pricePerNight;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato", nullable = false)
    private RoomStatus status; //Stato della camera richiesto

    @Column(name = "note_cliente", columnDefinition = "TEXT")
    private String notes; //Per le note inserite dal cliente durante il suo soggiorno
    
    @ManyToOne //Notazione che uso per la relazione molti a uno, dato che molte camere appartengono a una sola struttura
    @JoinColumn(name = "id_struttura")
    private Structure structure;
}
