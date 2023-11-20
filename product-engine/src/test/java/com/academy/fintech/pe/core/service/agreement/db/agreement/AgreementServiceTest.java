package com.academy.fintech.pe.core.service.agreement.db.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.Agreement;
import com.academy.fintech.pe.public_interface.agreement.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgreementServiceTest {

    @Mock
    private AgreementRepository agreementRepository;

    @InjectMocks
    private AgreementService agreementService;

    @Test
    void saveAgreement() {
        final Agreement agreement = mock(Agreement.class);
        when(agreementRepository.save(agreement)).thenReturn(agreement);

        Agreement savedAgreement = agreementService.saveAgreement(agreement);

        assertThat(savedAgreement).isEqualTo(agreement);
        verify(agreementRepository, only()).save(agreement);
    }

    @Test
    void getAgreement_whenAgreementExists() {
        final UUID agreementNumber = UUID.randomUUID();
        final Agreement expectedAgreement = mock(Agreement.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        expectedAgreement.setAgreementNumber(agreementNumber);
        when(agreementRepository.findById(agreementNumber)).thenReturn(Optional.of(expectedAgreement));

        Agreement actualAgreement = agreementService.getAgreement(agreementNumber);

        assertThat(actualAgreement).isEqualTo(expectedAgreement);
        verify(agreementRepository, only()).findById(agreementNumber);
    }

    @Test
    void getAgreement_whenAgreementNotExists() {
        final UUID agreementNumber = UUID.randomUUID();
        when(agreementRepository.findById(agreementNumber)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> agreementService.getAgreement(agreementNumber));
        verify(agreementRepository, only()).findById(agreementNumber);
    }
}