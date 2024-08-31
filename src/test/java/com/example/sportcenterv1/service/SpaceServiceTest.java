package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.entity.space.*;
import com.example.sportcenterv1.repository.SpaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpaceServiceTest {

    @Mock
    private SpaceRepository spaceRepository;

    @InjectMocks
    private SpaceService spaceService;

    private Space space1;
    private Space space2;

    @BeforeEach
    void setUp() {
        space1 = new Room();
        space1.setId(1L);
        space1.setName("Room 1");

        space2 = new BasketballRoom();
        space2.setId(2L);
        space2.setName("Basketball Room 1");
    }

    @Test
    void testGetSpaceType() {
        when(spaceRepository.findSpaceTypeById(1L)).thenReturn("Room");

        String result = spaceService.getSpaceType(1L);
        assertEquals("Room", result);
    }

    @Test
    void testGetSpace() {
        when(spaceRepository.findById(1L)).thenReturn(Optional.of(space1));

        Optional<Space> result = spaceService.getSpace(1L);
        assertTrue(result.isPresent());
        assertEquals(space1, result.get());
    }

    @Test
    void testGetAllSpaces() {
        List<Space> spaces = Arrays.asList(space1, space2);
        when(spaceRepository.findAll()).thenReturn(spaces);

        List<Space> result = spaceService.getAllSpaces();
        assertEquals(2, result.size());
        assertEquals(space1, result.get(0));
        assertEquals(space2, result.get(1));
    }

    @Test
    void testGetAllSpacesBySpecialization() {
        Specialization specialization = new Specialization();
        specialization.setId(1L);
        specialization.setName("Specialization 1");

        space1.setSpecializations(Collections.singleton(specialization));
        when(spaceRepository.findAll()).thenReturn(Arrays.asList(space1, space2));

        List<Space> result = spaceService.getAllSpaces(specialization);
        assertEquals(1, result.size());
        assertEquals(space1, result.get(0));
    }

    @Test
    void testGetAllSpaceBySpecializationList() {
        Specialization specialization = new Specialization();
        specialization.setId(1L);
        specialization.setName("Specialization 1");

        space1.setSpecializations(Collections.singleton(specialization));
        when(spaceRepository.findAll()).thenReturn(Arrays.asList(space1, space2));

        Set<Space> result = spaceService.getAllSpaceBySpecialization(Arrays.asList(specialization));
        assertEquals(1, result.size());
        assertTrue(result.contains(space1));
    }

    @Test
    void testGetAllSpacesByType() {
        when(spaceRepository.findSpacesByType("Room")).thenReturn(Collections.singletonList(space1));

        List<Space> result = spaceService.getAllSpaces("Room");
        assertEquals(1, result.size());
        assertEquals(space1, result.get(0));
    }

    @Test
    void testCreateSpace() {
        spaceService.createSpace(space1);
        verify(spaceRepository, times(1)).save(space1);
    }

    @Test
    void testUpdateSpace() {
        Room updatedRoom = new Room();
        updatedRoom.setName("Updated Room");

        when(spaceRepository.findById(1L)).thenReturn(Optional.of(space1));

        spaceService.updateSpace(1L, updatedRoom);

        assertEquals("Updated Room", space1.getName());
        verify(spaceRepository, times(1)).save(space1);
    }

    @Test
    void testDeleteSpace() {
        spaceService.deleteSpace(1L);
        verify(spaceRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateBasketballRoom() {
        BasketballRoom updatedBasketballRoom = new BasketballRoom();
        updatedBasketballRoom.setName("Updated Basketball Room");
        updatedBasketballRoom.setHoopCount(10);

        when(spaceRepository.findById(2L)).thenReturn(Optional.of(space2));

        boolean result = spaceService.updateBasketballRoom(2L, updatedBasketballRoom);
        assertTrue(result);

        assertEquals("Updated Basketball Room", ((BasketballRoom) space2).getName());
        assertEquals(10, ((BasketballRoom) space2).getHoopCount());
        verify(spaceRepository, times(1)).save(space2);
    }
}
