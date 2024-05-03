package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Client;
import com.example.sportcenterv1.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getAllClients(){

        return clientRepository.findAll();
    }

    public Optional<Client> getClient(Long clientID){

        return clientRepository.findById(clientID);
    }

    public void createClient(Client client){

        clientRepository.save(client);
    }

    public void updateClient(Long clientID, Client updateClient){

    }

    public void deleteClient(Long clientID){

        clientRepository.deleteById(clientID);
    }
}
