package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.repository.SpecializationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationRepository specializationRepository;

    public Optional<Specialization> getSpecialization(Long specID){
        return specializationRepository.findById(specID);
    }

    public List<Specialization> getAllSpecializations(){
        return specializationRepository.findAll();
    }

    public void createSpecialization(Specialization specialization){
        specializationRepository.save(specialization);
    }

    public void updateSpecialization(Long specID, Specialization updateSpecialization){

        Optional<Specialization> optionalSpecialization = specializationRepository.findById(specID);

        if (optionalSpecialization.isPresent()){

            Specialization specToSave = optionalSpecialization.get();

            if (updateSpecialization.getName() != null || !updateSpecialization.getName().isBlank()){

                specToSave.setName(updateSpecialization.getName());
            }

            specializationRepository.save(specToSave);
        }
    }
    @Transactional
    public void deleteSpecialization(Long specializationId) {
        Optional<Specialization> specializationOptional = specializationRepository.findById(specializationId);

        if (specializationOptional.isPresent()) {
            Specialization specialization = specializationOptional.get();

            // Usunięcie powiązań z pracownikami
            specialization.getEmployees().forEach(employee -> employee.getSpecializations().remove(specialization));
            specialization.getEmployees().clear();

            // Usunięcie specjalizacji
            specializationRepository.delete(specialization);
        }
    }
}
