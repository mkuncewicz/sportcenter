package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Space;
import com.example.sportcenterv1.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaceService {

    @Autowired
    private SpaceRepository spaceRepository;

    public List<Space> getAllSpaces(){
        return spaceRepository.findAll();
    }

    public void createSpace(Space space){
        spaceRepository.save(space);
    }
}
