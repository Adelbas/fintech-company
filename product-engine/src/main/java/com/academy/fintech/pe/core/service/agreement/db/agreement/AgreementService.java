package com.academy.fintech.pe.core.service.agreement.db.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.enums.AgreementStatus;
import com.academy.fintech.pe.public_interface.agreement.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgreementService {

    private final AgreementRepository agreementRepository;

    /**
     * Saves agreement to database
     *
     * @param agreement agreement to save
     * @return saved agreement
     */
    public Agreement saveAgreement(Agreement agreement) {
        return agreementRepository.save(agreement);
    }

    /**
     * Gets agreement by agreement number from database
     *
     * @param uuid agreement number
     * @return agreement
     * @throws NotFoundException if agreement not found
     */
    public Agreement getAgreement(UUID uuid) {
        return agreementRepository.findById(uuid).orElseThrow(() -> new NotFoundException("Agreement not found"));
    }

    public Optional<List<Agreement>> findAgreementsByClientIdAndStatus(UUID clientId, AgreementStatus status) {
        return agreementRepository.findByClientIdAndStatus(clientId, status);
    }
}
