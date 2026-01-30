package com.hotel_alduina.hotel_management.repository;

import org.springframework.data.repository.CrudRepository;/*Per creare un repository e avere a disposizione i metodi */

import com.hotel_alduina.hotel_management.model.User;

import java.util.Optional; /*Per gestire in modo sicuro errori come NullPointerException, oltre all'utilità per il legame con CrudRepository */

public interface UserRepository extends CrudRepository< User, Long> {
    //Metodo utile per l'autenticazione futura

    Optional<User> findByUsername(String username);
    
}
