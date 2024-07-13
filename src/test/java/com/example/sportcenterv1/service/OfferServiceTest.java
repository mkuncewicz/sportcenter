package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Offer;
import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.repository.OfferRepository;
import com.example.sportcenterv1.repository.SpecializationRepository;
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
class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private SpecializationRepository specializationRepository;

    @InjectMocks
    private OfferService offerService;

    private Offer offer1;
    private Offer offer2;
    private Specialization specialization;

    @BeforeEach
    void setUp() {
        specialization = new Specialization();
        specialization.setId(1L);
        specialization.setName("Specialization 1");

        offer1 = new Offer();
        offer1.setId(1L);
        offer1.setName("Offer 1");
        offer1.setDescription("Description 1");
        offer1.setPrice(100.0);
        offer1.getSpecializations().add(specialization);

        offer2 = new Offer();
        offer2.setId(2L);
        offer2.setName("Offer 2");
        offer2.setDescription("Description 2");
        offer2.setPrice(200.0);
    }

    @Test
    void testGetAllOffers() {
        List<Offer> offers = Arrays.asList(offer1, offer2);
        when(offerRepository.findAll()).thenReturn(offers);

        List<Offer> result = offerService.getAllOffers();
        assertEquals(2, result.size());
        assertEquals("Offer 1", result.get(0).getName());
        assertEquals("Offer 2", result.get(1).getName());
    }

    @Test
    void testGetAllOffersByName() {
        List<Offer> offers = Arrays.asList(offer1, offer2);
        when(offerRepository.findAll()).thenReturn(offers);

        List<Offer> result = offerService.getAllOffersByName("offer 1");
        assertEquals(1, result.size());
        assertEquals("Offer 1", result.get(0).getName());
    }

    @Test
    void testGetOffer() {
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer1));

        Optional<Offer> result = offerService.getOffer(1L);
        assertTrue(result.isPresent());
        assertEquals("Offer 1", result.get().getName());
    }

    @Test
    void testCreateOffer() {
        offerService.createOffer(offer1);
        verify(offerRepository, times(1)).save(offer1);
    }

    @Test
    void testUpdateOffer() {
        Offer updatedOffer = new Offer();
        updatedOffer.setName("Updated Offer 1");
        updatedOffer.setDescription("Updated Description 1");
        updatedOffer.setPrice(150.0);

        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer1));
        offerService.updateOffer(1L, updatedOffer);

        assertEquals("Updated Offer 1", offer1.getName());
        assertEquals("Updated Description 1", offer1.getDescription());
        assertEquals(150.0, offer1.getPrice());
        verify(offerRepository, times(1)).save(offer1);
    }

    @Test
    void testDeleteOffer() {
        offerService.deleteOffer(1L);
        verify(offerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testAddSpecializationToOffer() {
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer1));
        when(specializationRepository.findById(1L)).thenReturn(Optional.of(specialization));

        offerService.addSpecializationToOffer(1L, 1L);

        assertTrue(offer1.getSpecializations().contains(specialization));
        assertTrue(specialization.getOffers().contains(offer1));

        verify(offerRepository, times(1)).save(offer1);
        verify(specializationRepository, times(1)).save(specialization);
    }

    @Test
    void testRemoveSpecializationFromOffer() {
        offer1.getSpecializations().add(specialization);
        specialization.getOffers().add(offer1);

        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer1));
        when(specializationRepository.findById(1L)).thenReturn(Optional.of(specialization));

        offerService.removeSpecializationFromOffer(1L, 1L);

        assertFalse(offer1.getSpecializations().contains(specialization));
        assertFalse(specialization.getOffers().contains(offer1));

        verify(offerRepository, times(1)).save(offer1);
        verify(specializationRepository, times(1)).save(specialization);
    }
}
