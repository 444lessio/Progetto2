package com.hotel_alduina.hotel_management.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "prenotazioni")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    private User customer; //Il cliente che effettua la prenotazione

    @ManyToOne
    @JoinColumn(name = "id_camera")
    private Room room; //La camera selezionata

    @Column(name = "data_inizio", nullable = false)
    private LocalDate startDate;

    @Column(name = "data_fine", nullable = false)
    private LocalDate endDate;

    //Qua inserisco i servizi aggiuntivi richiesti
    @Column(name = "servizi_extra", columnDefinition = "TEXT")
    private String additionalServices;

    @Column(name = "prezzo_totale")
    private Double totalCost;

    @Column(name = "note_cliente", columnDefinition = "TEXT")
    private String clientNotes;

    //Qua i campi per gestire il flusso richiesto, quindi check-in e check-out
    @Column(name = "check_in_effettuato")
    private boolean checkedIn = false;

    @Column(name = "check_out_effettuato")
    private boolean checkedOut = false;

    @Column(name = "num_occupanti", nullable = false)
    private int NumGuests;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GuestDetail> guestDetails;
}
