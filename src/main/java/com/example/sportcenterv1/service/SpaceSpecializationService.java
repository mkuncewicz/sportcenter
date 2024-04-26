package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Space;
import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.repository.SpaceRepository;
import com.example.sportcenterv1.repository.SpecializationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SpaceSpecializationService {

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private SpecializationRepository specializationRepository;

    @Transactional
    public void addSpecializationToSpace(Long spaceID, Long specID){

        Optional<Space> optionalSpace = spaceRepository.findById(spaceID);
        Optional<Specialization> optionalSpecialization = specializationRepository.findById(specID);

        if (optionalSpace.isPresent() && optionalSpecialization.isPresent()){
            Space saveSpace = optionalSpace.get();
            Specialization saveSpecialization = optionalSpecialization.get();

            saveSpace.getSpecializations().add(saveSpecialization);
            saveSpecialization.getSpaces().add(saveSpace);

            spaceRepository.save(saveSpace);
            specializationRepository.save(saveSpecialization);
        }
    }

    @Transactional
    public void deleteSpecializationFromSpace(Long spaceID, Long specID){
        Optional<Space> optionalSpace = spaceRepository.findById(spaceID);
        Optional<Specialization> optionalSpecialization = specializationRepository.findById(specID);

        if (optionalSpace.isPresent() && optionalSpecialization.isPresent()){
            Space saveSpace = optionalSpace.get();
            Specialization saveSpecialization = optionalSpecialization.get();

            saveSpace.getSpecializations().remove(saveSpecialization);
            saveSpecialization.getSpaces().remove(saveSpace);

            spaceRepository.save(saveSpace);
            specializationRepository.save(saveSpecialization);
        }
    }
}
