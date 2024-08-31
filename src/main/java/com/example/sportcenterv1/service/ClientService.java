package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Address;
import com.example.sportcenterv1.entity.Client;
import com.example.sportcenterv1.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getAllClients(){

        return clientRepository.findAll();
    }

    public List<Client> getAllClientsByName(String name){

        List<Client> clientList = clientRepository.findAll();

        if (name.isBlank()) return  clientList;

        List<Client> result = new ArrayList<>();

        for(Client client : clientList){
            String nameOfEmployee = client.getFirstName() + " " + client.getLastName();
            String lowerName = nameOfEmployee.toLowerCase();

            if (lowerName.contains(name.toLowerCase())) result.add(client);
        }

        return result;
    }

    public Optional<Client> getClient(Long clientID){

        return clientRepository.findById(clientID);
    }

    @Transactional
    public void createClient(Client client){

        if (client.getAddress() == null){
            client.setAddress(new Address());
        }
        clientRepository.save(client);
    }


    public void updateClient(Long clientID, Client updateClient) {
        Optional<Client> optionalClient = clientRepository.findById(clientID);
        if (optionalClient.isPresent()) {
            Client saveClient = optionalClient.get();
            boolean hasChanges = false;

            if (updateClient.getFirstName() != null && !updateClient.getFirstName().equals(saveClient.getFirstName())) {
                saveClient.setFirstName(updateClient.getFirstName());
                hasChanges = true;
            }
            if (updateClient.getLastName() != null && !updateClient.getLastName().equals(saveClient.getLastName())) {
                saveClient.setLastName(updateClient.getLastName());
                hasChanges = true;
            }
            if (updateClient.getPhoneNumber() != null && !updateClient.getPhoneNumber().equals(saveClient.getPhoneNumber())) {
                saveClient.setPhoneNumber(updateClient.getPhoneNumber());
                hasChanges = true;
            }
            if (updateClient.getDateOfBirth() != null && !updateClient.getDateOfBirth().equals(saveClient.getDateOfBirth())) {
                saveClient.setDateOfBirth(updateClient.getDateOfBirth());
                hasChanges = true;
            }
            if (updateClient.getAddress() != null) {
                Address addressToSave = saveClient.getAddress();
                if (updateClient.getAddress().getCity() != null && !updateClient.getAddress().getCity().equals(addressToSave.getCity())) {
                    addressToSave.setCity(updateClient.getAddress().getCity());
                    hasChanges = true;
                }
                if (updateClient.getAddress().getStreet() != null && !updateClient.getAddress().getStreet().equals(addressToSave.getStreet())) {
                    addressToSave.setStreet(updateClient.getAddress().getStreet());
                    hasChanges = true;
                }
                if (updateClient.getAddress().getBuildingNumber() != null && !updateClient.getAddress().getBuildingNumber().equals(addressToSave.getBuildingNumber())) {
                    addressToSave.setBuildingNumber(updateClient.getAddress().getBuildingNumber());
                    hasChanges = true;
                }
                if (updateClient.getAddress().getApartmentNumber() != null && !updateClient.getAddress().getApartmentNumber().equals(addressToSave.getApartmentNumber())) {
                    addressToSave.setApartmentNumber(updateClient.getAddress().getApartmentNumber());
                    hasChanges = true;
                }
                saveClient.setAddress(addressToSave);
            }

            if (hasChanges) {
                clientRepository.save(saveClient);
            }
        }
    }

    public void deleteClient(Long clientID){

        clientRepository.deleteById(clientID);
    }
}
