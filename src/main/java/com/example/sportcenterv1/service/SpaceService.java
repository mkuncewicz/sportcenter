package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Space;
import com.example.sportcenterv1.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpaceService {

    @Autowired
    private SpaceRepository spaceRepository;

    public Optional<Space> getSpace(Long spaceID){

        return spaceRepository.findById(spaceID);
    }

    public List<Space> getAllSpaces(){
        return spaceRepository.findAll();
    }

    public void createSpace(Space space){
        spaceRepository.save(space);
    }

    public void updateSpace(Long spaceID,Space updateSpace){

        Optional<Space> optionalSpace = spaceRepository.findById(spaceID);

        if (optionalSpace.isPresent()){
            Space saveSpace = optionalSpace.get();

            if (updateSpace.getName() != null) saveSpace.setName(updateSpace.getName());
            if (updateSpace.getSqrMeters() > 0) saveSpace.setSqrMeters(updateSpace.getSqrMeters());
            if (updateSpace.getWidth() > 0) saveSpace.setWidth(updateSpace.getWidth());
            if (updateSpace.getLength() > 0) saveSpace.setLength(updateSpace.getLength());
            if (updateSpace.getType() != null) saveSpace.setType(updateSpace.getType());

            spaceRepository.save(saveSpace);
        }
    }


    public void deleteSpace(Long spaceID){
        spaceRepository.deleteById(spaceID);
    }
}
