package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Offer;
import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.repository.OfferRepository;
import com.example.sportcenterv1.repository.SpecializationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private SpecializationRepository specializationRepository;

    public List<Offer> getAllOffers(){

        return offerRepository.findAll();
    }

    public List<Offer> getAllOffersByName(String name){

        List<Offer> offerList = offerRepository.findAll();

        List<Offer> result = new ArrayList<>();

        if (name.isBlank()) return  offerList;

        for (Offer offer : offerList){

            String offerNameLower = offer.getName().toLowerCase();

            if (offerNameLower.contains(name.toLowerCase())) result.add(offer);
        }

        return result;
    }

    public List<Offer> getAllOffersBySpecialization(Specialization specialization){

        List<Offer> offerList = offerRepository.findAll();

        Set<Offer> resut = new HashSet<>();

        if (specialization == null) return offerList;

        for (Offer offer : offerList){

            if (offer.getSpecializations().contains(specialization)) resut.add(offer);
        }

        return new ArrayList<>(resut);
    }

    public Optional<Offer> getOffer(Long offerID){

        return offerRepository.findById(offerID);
    }

    public void createOffer(Offer offer){

        offerRepository.save(offer);
    }

    public void updateOffer(Long offerID, Offer updateOffer){

        Optional<Offer> optionalOffer = offerRepository.findById(offerID);

        if (optionalOffer.isPresent()){
            Offer saveOffer = optionalOffer.get();

            if (!updateOffer.getName().isBlank()) saveOffer.setName(updateOffer.getName());
            if (!updateOffer.getDescription().isBlank()) saveOffer.setDescription(updateOffer.getDescription());
            if (updateOffer.getPrice() > 0) saveOffer.setPrice(updateOffer.getPrice());
            if (updateOffer.getOfferType() != null) saveOffer.setOfferType(updateOffer.getOfferType());

            offerRepository.save(saveOffer);
        }
    }

    public void deleteOffer(Long offerID){

        offerRepository.deleteById(offerID);
    }

    @Transactional
    public void addSpecializationToOffer(Long offerID, Long specID){

        Optional<Offer> optionalOffer = offerRepository.findById(offerID);
        Optional<Specialization> optionalSpecialization = specializationRepository.findById(specID);

        if (optionalOffer.isPresent() && optionalSpecialization.isPresent()){

            Offer saveOffer = optionalOffer.get();
            Specialization saveSpecialization = optionalSpecialization.get();

            saveOffer.getSpecializations().add(saveSpecialization);
            saveSpecialization.getOffers().add(saveOffer);

            offerRepository.save(saveOffer);
            specializationRepository.save(saveSpecialization);
        }
    }

    @Transactional
    public void removeSpecializationFromOffer(Long offerID, Long specID){

        Optional<Offer> optionalOffer = offerRepository.findById(offerID);
        Optional<Specialization> optionalSpecialization = specializationRepository.findById(specID);

        if (optionalOffer.isPresent() && optionalSpecialization.isPresent()){

            Offer saveOffer = optionalOffer.get();
            Specialization saveSpecialization = optionalSpecialization.get();

            saveOffer.getSpecializations().remove(saveSpecialization);
            saveSpecialization.getOffers().remove(saveOffer);

            offerRepository.save(saveOffer);
            specializationRepository.save(saveSpecialization);
        }
    }
}
