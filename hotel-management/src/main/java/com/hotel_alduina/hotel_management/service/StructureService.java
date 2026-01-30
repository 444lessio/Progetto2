package com.hotel_alduina.hotel_management.service;

import com.hotel_alduina.hotel_management.model.Structure;
import com.hotel_alduina.hotel_management.repository.StructureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.Collection;

@Service
@Transactional
public class StructureService {
    
    @Autowired
    private StructureRepository structureRepository;

    public Structure createStructure(Structure structure){
        return structureRepository.save(structure);
    }

    public Collection<Structure> getAllStructures() {
        return structureRepository.findAll();
    }

    public Collection<Structure> getStructuresByLocation(String location) {
        return structureRepository.findByLocation(location);
    }
}


