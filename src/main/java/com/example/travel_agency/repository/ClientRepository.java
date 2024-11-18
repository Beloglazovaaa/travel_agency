// ClientRepository.java
package com.example.travel_agency.repository;

import com.example.travel_agency.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    // Дополнительные методы, если необходимо
}
