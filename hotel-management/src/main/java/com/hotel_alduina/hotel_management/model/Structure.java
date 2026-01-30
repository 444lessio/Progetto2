package com.hotel_alduina.hotel_management.model;

import jakarta.persistence.*; /*Per permette di far comunicare le classi Java con le tabelle del DB */
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity /*Questa annotazione mi indica che questa classe rappresenta una tabella del DB */
@Table(name = "strutture")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Structure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Autoincremento
    private long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String name;
    
    @Column(name = "localita", nullable = false, length = 100)
    private String location;
}
