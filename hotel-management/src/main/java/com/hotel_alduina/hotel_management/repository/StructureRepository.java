package com.hotel_alduina.hotel_management.repository;

import com.hotel_alduina.hotel_management.model.Structure;
import org.springframework.data.repository.CrudRepository;
import java.util.Collection;

public interface StructureRepository extends CrudRepository<Structure, Long> {
    //Spring Data genererà automaticamente la query in base al nome del metodo
    Collection<Structure> findAll(); //Per recuperare i record presenti nella tabella associata a Structure
    Collection<Structure> findByLocation(String location); //Per la ricerca della disponibilità
}
