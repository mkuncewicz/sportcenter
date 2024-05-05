package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.entity.space.Space;
import com.example.sportcenterv1.repository.SpaceRepository;
import com.example.sportcenterv1.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SpaceSpecializationService {

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private SpecializationRepository specializationRepository;


    public void addSpaceToSpecialization(Long spaceID, Long specID){

        Optional<Space> optionalSpace = spaceRepository.findById(spaceID);
        Optional<Specialization> optionalSpecialization = specializationRepository.findById(specID);

        if (optionalSpace.isPresent() && optionalSpecialization.isPresent()){

            Space saveSpace = optionalSpace.get();
            Specialization saveSpec = optionalSpecialization.get();

            saveSpace.getSpecializations().add(saveSpec);
            saveSpec.getSpaces().add(saveSpace);

            spaceRepository.save(saveSpace);
            specializationRepository.save(saveSpec);
        }
    }

    public void deleteSpaceFromSpecialization(Long spaceID, Long specID){

        Optional<Space> optionalSpace = spaceRepository.findById(spaceID);
        Optional<Specialization> optionalSpecialization = specializationRepository.findById(specID);

        if (optionalSpace.isPresent() && optionalSpecialization.isPresent()) {

            Space saveSpace = optionalSpace.get();
            Specialization saveSpec = optionalSpecialization.get();

            saveSpace.getSpecializations().remove(saveSpec);
            saveSpec.getSpaces().remove(saveSpace);

            spaceRepository.save(saveSpace);
            specializationRepository.save(saveSpec);
        }
    }
}
