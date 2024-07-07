package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.repository.SpecializationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecializationServiceTest {

    @Mock
    private SpecializationRepository specializationRepository;

    @InjectMocks
    private SpecializationService specializationService;

    private Specialization specialization1;
    private Specialization specialization2;

    @BeforeEach
    void setUp() {
        specialization1 = new Specialization();
        specialization1.setId(1L);
        specialization1.setName("Koszykówka");
        specialization1.setLevel("Zaawansowany");

        specialization2 = new Specialization();
        specialization2.setId(2L);
        specialization2.setName("Piłka Nożna");
        specialization2.setLevel("Średniozaawansowany");
    }

    @Test
    void testGetSpecialization() {
        when(specializationRepository.findById(1L)).thenReturn(Optional.of(specialization1));

        Optional<Specialization> result = specializationService.getSpecialization(1L);
        assertTrue(result.isPresent());
        assertEquals("Koszykówka", result.get().getName());
    }

    @Test
    void testGetAllSpecializations() {
        List<Specialization> specializations = Arrays.asList(specialization1, specialization2);
        when(specializationRepository.findAll()).thenReturn(specializations);

        List<Specialization> result = specializationService.getAllSpecializations();
        assertEquals(2, result.size());
        assertEquals("Koszykówka", result.get(0).getName());
        assertEquals("Piłka Nożna", result.get(1).getName());
    }

    @Test
    void testCreateSpecialization() {
        specializationService.createSpecialization(specialization1);
        verify(specializationRepository, times(1)).save(specialization1);
    }

    @Test
    void testUpdateSpecialization() {
        Specialization updatedSpecialization = new Specialization();
        updatedSpecialization.setName("Siatkówka");

        when(specializationRepository.findById(1L)).thenReturn(Optional.of(specialization1));
        specializationService.updateSpecialization(1L, updatedSpecialization);

        assertEquals("Siatkówka", specialization1.getName());
        verify(specializationRepository, times(1)).save(specialization1);
    }

    @Test
    void testDeleteSpecialization() {
        when(specializationRepository.findById(1L)).thenReturn(Optional.of(specialization1));
        specializationService.deleteSpecialization(1L);

        verify(specializationRepository, times(1)).delete(specialization1);
    }
}
