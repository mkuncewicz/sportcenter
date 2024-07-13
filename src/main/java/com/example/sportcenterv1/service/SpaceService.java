package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.entity.space.*;
import com.example.sportcenterv1.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SpaceService {

    @Autowired
    private SpaceRepository spaceRepository;


    public String getSpaceType(Long spaceID){
        return spaceRepository.findSpaceTypeById(spaceID);
    }



    public Optional<Space> getSpace(Long spaceID){

        return spaceRepository.findById(spaceID);
    }

    public List<Space> getAllSpaces(){
        return spaceRepository.findAll();
    }
    //Przeciążenie metody
    public List<Space> getAllSpaces(Specialization specialization){

        if (specialization == null) return spaceRepository.findAll();

        List<Space> result = spaceRepository.findAll().stream()
                .filter(space -> space.getSpecializations().contains(specialization)).collect(Collectors.toList());

        return result;
    }

    public Set<Space> getAllSpaceBySpecialization(List<Specialization> specializationList) {
        if (specializationList == null || specializationList.isEmpty()) {
            return Collections.emptySet();
        }

        return spaceRepository.findAll().stream()
                .filter(space -> space.getSpecializations().stream()
                        .anyMatch(specializationList::contains))
                .collect(Collectors.toSet());
    }

    //Przeciążenie metody
    public List<Space> getAllSpaces(String spaceType){
        return spaceRepository.findSpacesByType(spaceType);
    }
    public void createSpace(Space space){
        spaceRepository.save(space);
    }

    public void updateSpace(Long spaceID,Space updateSpace){

        if (updateSpace instanceof BasketballRoom){
            BasketballRoom updateBasketball = (BasketballRoom) updateSpace;
            updateBasketballRoom(spaceID,updateBasketball);

        }else if (updateSpace instanceof MartialArtsRoom){
            updateMartialArtsRoom(spaceID,updateSpace);

        }else if (updateSpace instanceof MedicalRoom){
            updateMedicalRoom(spaceID,updateSpace);

        } else if (updateSpace instanceof Room) {
            updateRoom(spaceID,updateSpace);

        }else if (updateSpace instanceof SoccerField){
            updateSoccerField(spaceID,updateSpace);

        }else if (updateSpace instanceof SwimmingPool){
            updateSwimmingPool(spaceID,updateSpace);
        }
    }

    public boolean updateBasketballRoom(Long spaceID, BasketballRoom updateBasketballRoom){
        try {
            Optional<Space> optionalSpace = spaceRepository.findById(spaceID);
            if (optionalSpace.isPresent() && updateBasketballRoom != null) {
                BasketballRoom spaceSave = (BasketballRoom) optionalSpace.get();

                boolean isUpdated = false;
                if (updateBasketballRoom.getName() != null && !updateBasketballRoom.getName().equals(spaceSave.getName())) {
                    spaceSave.setName(updateBasketballRoom.getName());
                    isUpdated = true;
                }
                if (updateBasketballRoom.getCapacity() > 0 && updateBasketballRoom.getCapacity() != spaceSave.getCapacity()) {
                    spaceSave.setCapacity(updateBasketballRoom.getCapacity());
                    isUpdated = true;
                }
                if (updateBasketballRoom.getSquareFootage() > 0 && updateBasketballRoom.getSquareFootage() != spaceSave.getSquareFootage()) {
                    spaceSave.setSquareFootage(updateBasketballRoom.getSquareFootage());
                    isUpdated = true;
                }
                if (updateBasketballRoom.getHoopCount() >= 0 && updateBasketballRoom.getHoopCount() != spaceSave.getHoopCount()) {
                    spaceSave.setHoopCount(updateBasketballRoom.getHoopCount());
                    isUpdated = true;
                }
                if (updateBasketballRoom.getCourtType() != null && updateBasketballRoom.getCourtType() != spaceSave.getCourtType()) {
                    spaceSave.setCourtType(updateBasketballRoom.getCourtType());
                    isUpdated = true;
                }

                if (isUpdated) {
                    spaceRepository.save(spaceSave);
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error updating basketball room: " + e.getMessage());
            return false;
        }
    }

    private void updateMartialArtsRoom(Long spaceID, Space updateSpace){

        Optional<Space> optionalSpace = spaceRepository.findById(spaceID);
        if (optionalSpace.isPresent() && updateSpace instanceof MartialArtsRoom){

            MartialArtsRoom spaceSave = (MartialArtsRoom) optionalSpace.get();
            MartialArtsRoom martialArtsRoom = (MartialArtsRoom) updateSpace;

            if (martialArtsRoom.getName() != null) spaceSave.setName(martialArtsRoom.getName());
            if (martialArtsRoom.getCapacity() > 0) spaceSave.setCapacity(martialArtsRoom.getCapacity());
            if (martialArtsRoom.getSquareFootage() > 0) spaceSave.setSquareFootage(martialArtsRoom.getSquareFootage());
            if (martialArtsRoom.getMatCount() > -1) spaceSave.setMatCount(martialArtsRoom.getMatCount());

            spaceRepository.save(spaceSave);
        }
    }

    private void updateMedicalRoom(Long spaceID, Space updateSpace){

        Optional<Space> optionalSpace = spaceRepository.findById(spaceID);
        if (optionalSpace.isPresent() && updateSpace instanceof MedicalRoom){

            MedicalRoom spaceSave = (MedicalRoom) optionalSpace.get();
            MedicalRoom medicalRoom = (MedicalRoom) updateSpace;

            if (medicalRoom.getName() != null) spaceSave.setName(medicalRoom.getName());
            if (medicalRoom.getCapacity() > 0) spaceSave.setCapacity(medicalRoom.getCapacity());
            if (medicalRoom.getSquareFootage() > 0) spaceSave.setSquareFootage(medicalRoom.getSquareFootage());
            if (medicalRoom.isSterile() != spaceSave.isSterile()) spaceSave.setSterile(medicalRoom.isSterile());

            spaceRepository.save(spaceSave);
        }
    }

    private void updateRoom(Long spaceID, Space updateSpace){

        Optional<Space> optionalSpace = spaceRepository.findById(spaceID);
        if (optionalSpace.isPresent() && updateSpace instanceof Room){

            Room spaceSave = (Room) optionalSpace.get();
            Room room = (Room) updateSpace;

            if (room.getName() != null) spaceSave.setName(room.getName());
            if (room.getCapacity() > 0) spaceSave.setCapacity(room.getCapacity());
            if (room.getSquareFootage() > 0) spaceSave.setSquareFootage(room.getSquareFootage());

            spaceRepository.save(spaceSave);
        }
    }

    private void updateSoccerField(Long spaceID, Space updateSpace){

        Optional<Space> optionalSpace = spaceRepository.findById(spaceID);
        if (optionalSpace.isPresent() && updateSpace instanceof SoccerField){

            SoccerField spaceSave = (SoccerField) optionalSpace.get();
            SoccerField soccerField = (SoccerField) updateSpace;

            if (soccerField.getName() != null) spaceSave.setName(soccerField.getName());
            if (soccerField.getSquareFootage() > 0) spaceSave.setSquareFootage(soccerField.getSquareFootage());
            if (soccerField.getGoalCount() > -1) spaceSave.setGoalCount(soccerField.getGoalCount());
            if (soccerField.getTurfType() != null && soccerField.getTurfType() != spaceSave.getTurfType()) spaceSave.setTurfType(soccerField.getTurfType());

            spaceRepository.save(spaceSave);
        }
    }

    private void updateSwimmingPool(Long spaceID, Space updateSpace){

        Optional<Space> optionalSpace = spaceRepository.findById(spaceID);
        if(optionalSpace.isPresent() && updateSpace instanceof  SwimmingPool){

            SwimmingPool spaceSave = (SwimmingPool) optionalSpace.get();
            SwimmingPool swimmingPool = (SwimmingPool) updateSpace;

            if (swimmingPool.getName() != null) spaceSave.setName(swimmingPool.getName());
            if (swimmingPool.getPoolLength() > 0) spaceSave.setPoolLength(swimmingPool.getPoolLength());
            if (swimmingPool.getPoolDepth() > 0) spaceSave.setPoolDepth(swimmingPool.getPoolDepth());
            if (swimmingPool.getLaneCount() > -1) spaceSave.setLaneCount(swimmingPool.getLaneCount());

            spaceRepository.save(spaceSave);
        }
    }
    public void deleteSpace(Long spaceID){
        spaceRepository.deleteById(spaceID);
    }
}
