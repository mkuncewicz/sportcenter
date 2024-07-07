package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.entity.enums.CourtType;
import com.example.sportcenterv1.entity.enums.TurfType;
import com.example.sportcenterv1.entity.space.*;
import com.example.sportcenterv1.repository.SpaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpaceServiceTest {

    @Mock
    private SpaceRepository spaceRepository;

    @InjectMocks
    private SpaceService spaceService;

    private BasketballRoom basketballRoom;
    private SwimmingPool swimmingPool;
    private Specialization specialization;

    @BeforeEach
    void setUp() {
        specialization = new Specialization();
        specialization.setId(1L);
        specialization.setName("Koszykówka");

        basketballRoom = new BasketballRoom();
        basketballRoom.setId(1L);
        basketballRoom.setName("Basketball Room 1");
        basketballRoom.setCapacity(500);
        basketballRoom.setSquareFootage(1000.0);
        basketballRoom.setHoopCount(4);
        basketballRoom.setCourtType(CourtType.WOOD);
        basketballRoom.getSpecializations().add(specialization);

        swimmingPool = new SwimmingPool();
        swimmingPool.setId(2L);
        swimmingPool.setName("Swimming Pool 1");
        swimmingPool.setPoolLength(50.0);
        swimmingPool.setPoolDepth(2.0);
        swimmingPool.setLaneCount(8);
        swimmingPool.getSpecializations().add(specialization);
    }

    @Test
    void testGetSpaceType() {
        when(spaceRepository.findSpaceTypeById(1L)).thenReturn("BasketballRoom");

        String result = spaceService.getSpaceType(1L);
        assertEquals("BasketballRoom", result);
    }

    @Test
    void testGetSpace() {
        when(spaceRepository.findById(1L)).thenReturn(Optional.of(basketballRoom));

        Optional<Space> result = spaceService.getSpace(1L);
        assertTrue(result.isPresent());
        assertEquals("Basketball Room 1", result.get().getName());
    }

    @Test
    void testGetAllSpaces() {
        List<Space> spaces = Arrays.asList(basketballRoom, swimmingPool);
        when(spaceRepository.findAll()).thenReturn(spaces);

        List<Space> result = spaceService.getAllSpaces();
        assertEquals(2, result.size());
        assertEquals("Basketball Room 1", result.get(0).getName());
        assertEquals("Swimming Pool 1", result.get(1).getName());
    }

    @Test
    void testGetAllSpacesBySpecialization() {
        List<Space> spaces = Arrays.asList(basketballRoom, swimmingPool);
        when(spaceRepository.findAll()).thenReturn(spaces);

        Set<Space> result = spaceService.getAllSpaceBySpecialization(List.of(specialization));
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllSpacesBySpaceType() {
        List<Space> spaces = Arrays.asList(basketballRoom);
        when(spaceRepository.findSpacesByType("BasketballRoom")).thenReturn(spaces);

        List<Space> result = spaceService.getAllSpaces("BasketballRoom");
        assertEquals(1, result.size());
        assertEquals("Basketball Room 1", result.get(0).getName());
    }

    @Test
    void testCreateSpace() {
        spaceService.createSpace(basketballRoom);
        verify(spaceRepository, times(1)).save(basketballRoom);
    }

    @Test
    void testUpdateSpace_BasketballRoom() {
        BasketballRoom updatedBasketballRoom = new BasketballRoom();
        updatedBasketballRoom.setName("Updated Basketball Room");
        updatedBasketballRoom.setCapacity(600);
        updatedBasketballRoom.setSquareFootage(1200.0);
        updatedBasketballRoom.setHoopCount(6);
        updatedBasketballRoom.setCourtType(CourtType.SYNTHETIC);

        when(spaceRepository.findById(1L)).thenReturn(Optional.of(basketballRoom));
        spaceService.updateSpace(1L, updatedBasketballRoom);

        assertEquals("Updated Basketball Room", basketballRoom.getName());
        assertEquals(600, basketballRoom.getCapacity());
        assertEquals(1200.0, basketballRoom.getSquareFootage());
        assertEquals(6, basketballRoom.getHoopCount());
        assertEquals(CourtType.SYNTHETIC, basketballRoom.getCourtType());
        verify(spaceRepository, times(1)).save(basketballRoom);
    }

    @Test
    void testDeleteSpace() {
        spaceService.deleteSpace(1L);
        verify(spaceRepository, times(1)).deleteById(1L);
    }
}
