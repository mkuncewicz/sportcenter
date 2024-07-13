package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Address;
import com.example.sportcenterv1.entity.Client;
import com.example.sportcenterv1.entity.employee.Employee;
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

    public void updateClient(Long clientID, Client updateClient){

        Optional<Client> optionalClient = clientRepository.findById(clientID);

        if (optionalClient.isPresent()){
            Client saveClient = optionalClient.get();

            if (updateClient.getFirstName() != null) saveClient.setFirstName(updateClient.getFirstName());
            if (updateClient.getLastName() != null) saveClient.setLastName(updateClient.getLastName());
            if (updateClient.getPhoneNumber() != null) saveClient.setPhoneNumber(updateClient.getPhoneNumber());
            if (updateClient.getDateOfBirth() != null) saveClient.setDateOfBirth(updateClient.getDateOfBirth());
            if (updateClient.getAddress() != null) {
                Address addressToSave = saveClient.getAddress();
                if (updateClient.getAddress().getCity() != null) addressToSave.setCity(updateClient.getAddress().getCity());
                if (updateClient.getAddress().getStreet() != null) addressToSave.setStreet(updateClient.getAddress().getStreet());
                if (updateClient.getAddress().getBuildingNumber() != null) addressToSave.setBuildingNumber(updateClient.getAddress().getBuildingNumber());
                if (updateClient.getAddress().getApartmentNumber() != null) addressToSave.setApartmentNumber(updateClient.getAddress().getApartmentNumber());

                saveClient.setAddress(addressToSave);
            }

            clientRepository.save(saveClient);
        }
    }

    public void deleteClient(Long clientID){

        clientRepository.deleteById(clientID);
    }
}
