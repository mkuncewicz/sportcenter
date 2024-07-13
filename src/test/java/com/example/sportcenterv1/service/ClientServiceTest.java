package com.example.sportcenterv1.service;

import com.example.sportcenterv1.entity.Address;
import com.example.sportcenterv1.entity.Client;
import com.example.sportcenterv1.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private Client client1;
    private Client client2;

    @BeforeEach
    void setUp() {
        client1 = new Client();
        client1.setId(1L);
        client1.setFirstName("John");
        client1.setLastName("Doe");
        client1.setPhoneNumber("123456789");
        client1.setDateOfBirth(new Date());

        client2 = new Client();
        client2.setId(2L);
        client2.setFirstName("Jane");
        client2.setLastName("Smith");
        client2.setPhoneNumber("987654321");
        client2.setDateOfBirth(new Date());
    }

    @Test
    void testGetAllClients() {
        List<Client> clients = Arrays.asList(client1, client2);
        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> result = clientService.getAllClients();
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
    }

    @Test
    void testGetAllClientsByName() {
        List<Client> clients = Arrays.asList(client1, client2);
        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> result = clientService.getAllClientsByName("john");
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void testGetClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client1));

        Optional<Client> result = clientService.getClient(1L);
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    void testCreateClient() {
        clientService.createClient(client1);
        verify(clientRepository, times(1)).save(client1);
    }

    @Test
    void testUpdateClient() {
        Client updatedClient = new Client();
        updatedClient.setFirstName("Mike");
        updatedClient.setAddress(new Address());
        updatedClient.getAddress().setCity("New City");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client1));
        clientService.updateClient(1L, updatedClient);

        assertEquals("Mike", client1.getFirstName());
        assertEquals("New City", client1.getAddress().getCity());
        verify(clientRepository, times(1)).save(client1);
    }

    @Test
    void testDeleteClient() {
        clientService.deleteClient(1L);
        verify(clientRepository, times(1)).deleteById(1L);
    }
}
