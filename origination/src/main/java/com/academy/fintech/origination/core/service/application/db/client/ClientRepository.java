package com.academy.fintech.origination.core.service.application.db.client;

import com.academy.fintech.origination.core.service.application.db.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findByEmail(String email);
}
