package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.entity.employee.Employee;
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
        specialization1.setName("Specialization 1");

        specialization2 = new Specialization();
        specialization2.setId(2L);
        specialization2.setName("Specialization 2");
    }

    @Test
    void testGetSpecialization() {
        when(specializationRepository.findById(1L)).thenReturn(Optional.of(specialization1));

        Optional<Specialization> result = specializationService.getSpecialization(1L);
        assertTrue(result.isPresent());
        assertEquals("Specialization 1", result.get().getName());
    }

    @Test
    void testGetAllSpecializations() {
        List<Specialization> specializations = Arrays.asList(specialization1, specialization2);
        when(specializationRepository.findAll()).thenReturn(specializations);

        List<Specialization> result = specializationService.getAllSpecializations();
        assertEquals(2, result.size());
        assertEquals("Specialization 1", result.get(0).getName());
        assertEquals("Specialization 2", result.get(1).getName());
    }

    @Test
    void testCreateSpecialization() {
        specializationService.createSpecialization(specialization1);
        verify(specializationRepository, times(1)).save(specialization1);
    }

    @Test
    void testUpdateSpecialization() {
        Specialization updatedSpecialization = new Specialization();
        updatedSpecialization.setName("Updated Specialization");

        when(specializationRepository.findById(1L)).thenReturn(Optional.of(specialization1));
        specializationService.updateSpecialization(1L, updatedSpecialization);

        assertEquals("Updated Specialization", specialization1.getName());
        verify(specializationRepository, times(1)).save(specialization1);
    }

    @Test
    void testDeleteSpecialization() {
        when(specializationRepository.findById(1L)).thenReturn(Optional.of(specialization1));

        specializationService.deleteSpecialization(1L);
        verify(specializationRepository, times(1)).delete(specialization1);
    }

    @Test
    void testDeleteSpecialization_WithEmployees() {
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        specialization1.getEmployees().add(employee1);
        specialization1.getEmployees().add(employee2);

        when(specializationRepository.findById(1L)).thenReturn(Optional.of(specialization1));

        specializationService.deleteSpecialization(1L);

        assertTrue(specialization1.getEmployees().isEmpty());
        assertFalse(employee1.getSpecializations().contains(specialization1));
        assertFalse(employee2.getSpecializations().contains(specialization1));
        verify(specializationRepository, times(1)).delete(specialization1);
    }
}
