package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Offer;
import com.example.sportcenterv1.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;


    public List<Offer> getAllOffers(){

        return offerRepository.findAll();
    }

    public Optional<Offer> getOffer(Long offerID){

        return offerRepository.findById(offerID);
    }

    public void createOffer(Offer offer){

        offerRepository.save(offer);
    }

    public void updateOffer(Long offerID, Offer updateOffer){

    }

    public void deleteOffer(Long offerID){

        offerRepository.deleteById(offerID);
    }
}
