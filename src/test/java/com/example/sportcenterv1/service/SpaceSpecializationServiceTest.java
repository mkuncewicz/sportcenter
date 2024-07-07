package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.entity.space.Space;
import com.example.sportcenterv1.repository.SpaceRepository;
import com.example.sportcenterv1.repository.SpecializationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpaceSpecializationServiceTest {

    @Mock
    private SpaceRepository spaceRepository;

    @Mock
    private SpecializationRepository specializationRepository;

    @InjectMocks
    private SpaceSpecializationService spaceSpecializationService;

    private Space space;
    private Specialization specialization;

    @BeforeEach
    void setUp() {
        space = new Space() {
            {
                setId(1L);
                setName("Space 1");
            }
        };

        specialization = new Specialization();
        specialization.setId(1L);
        specialization.setName("Specialization 1");
    }

    @Test
    void testAddSpaceToSpecialization() {
        when(spaceRepository.findById(1L)).thenReturn(Optional.of(space));
        when(specializationRepository.findById(1L)).thenReturn(Optional.of(specialization));

        spaceSpecializationService.addSpaceToSpecialization(1L, 1L);

        assertTrue(space.getSpecializations().contains(specialization));
        assertTrue(specialization.getSpaces().contains(space));

        verify(spaceRepository, times(1)).save(space);
        verify(specializationRepository, times(1)).save(specialization);
    }

    @Test
    void testDeleteSpaceFromSpecialization() {
        space.getSpecializations().add(specialization);
        specialization.getSpaces().add(space);

        when(spaceRepository.findById(1L)).thenReturn(Optional.of(space));
        when(specializationRepository.findById(1L)).thenReturn(Optional.of(specialization));

        spaceSpecializationService.deleteSpaceFromSpecialization(1L, 1L);

        assertFalse(space.getSpecializations().contains(specialization));
        assertFalse(specialization.getSpaces().contains(space));

        verify(spaceRepository, times(1)).save(space);
        verify(specializationRepository, times(1)).save(specialization);
    }
}
