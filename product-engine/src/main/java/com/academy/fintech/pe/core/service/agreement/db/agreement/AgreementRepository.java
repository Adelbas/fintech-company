package com.academy.fintech.pe.core.service.agreement.db.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.enums.AgreementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, UUID> {

    Optional<List<Agreement>> findByClientIdAndStatus(UUID clientId, AgreementStatus status);

    List<Agreement> findAgreementsByNextPaymentDateBetweenAndStatus(LocalDateTime paymentDateStart,
                                                                    LocalDateTime paymentDateEnd,
                                                                    AgreementStatus status);
}
