package com.academy.fintech.origination.core.service.agreement.db.agreement;

import com.academy.fintech.origination.core.service.agreement.db.agreement.entity.Agreement;
import com.academy.fintech.origination.public_interface.application.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgreementDbService {

    private final AgreementRepository agreementRepository;

    public void saveAgreement(Agreement agreement) {
        log.info("Saving agreement: {}", agreement);
        agreementRepository.save(agreement);
    }

    public Agreement getAgreement(UUID id) {
        return agreementRepository.findById(id).orElseThrow(()->new NotFoundException("Agreement not found"));
    }
}
