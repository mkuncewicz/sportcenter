package com.example.sportcenterv1.repository;

import com.example.sportcenterv1.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
