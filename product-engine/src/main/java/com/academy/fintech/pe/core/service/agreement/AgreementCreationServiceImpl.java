package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.calculation.payment_schedule.PaymentScheduleCalculations;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.product.ProductService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.PaymentSchedule;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.PaymentSchedulePayment;
import com.academy.fintech.pe.core.service.agreement.db.product.entity.Product;
import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.enums.AgreementStatus;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.enums.PaymentStatus;
import com.academy.fintech.pe.public_interface.agreement.AgreementMapper;
import com.academy.fintech.pe.public_interface.agreement.dto.AgreementActivationDto;
import com.academy.fintech.pe.public_interface.agreement.dto.AgreementDto;
import com.academy.fintech.pe.public_interface.agreement.dto.PaymentScheduleDto;
import com.academy.fintech.pe.public_interface.agreement.exception.InvalidParametersException;
import com.academy.fintech.pe.public_interface.agreement.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Represents agreement creation service implementation.
 * Uses {@link AgreementService} and {@link ProductService} to interact with database.
 * Uses {@link AgreementMapper} to map entity to DTO.
 */
@Service
@RequiredArgsConstructor
public class AgreementCreationServiceImpl implements AgreementCreationService {

    private final AgreementService agreementService;

    private final ProductService productService;

    private final AgreementMapper agreementMapper;

    /**
     * Provides agreement creation.
     * Validates agreement parameters with product parameters.
     * Counts principalAmount from origination and disbursement amount.
     * Sets agreement status to {@link AgreementStatus#NEW}.
     * Saves agreement to database.
     *
     * @param agreementDto agreementDto with agreement parameters
     * @return created agreement number
     * @throws InvalidParametersException if agreement validation is failed
     */
    @Override
    public UUID createAgreement(AgreementDto agreementDto) {
        Product product = productService.getProduct(agreementDto.productCode());

        if (!isValidAgreementParameters(agreementDto, product)) {
            throw new InvalidParametersException("Invalid agreement parameters");
        }

        BigDecimal principalAmount = agreementDto.disbursementAmount().add(agreementDto.originationAmount());

        Agreement agreement = agreementService.saveAgreement(Agreement.builder()
                .clientId(agreementDto.clientId())
                .product(product)
                .loanTerm(agreementDto.loanTerm())
                .interest(agreementDto.interest())
                .principalAmount(principalAmount)
                .originationAmount(agreementDto.originationAmount())
                .status(AgreementStatus.NEW)
                .build()
        );

        return agreement.getAgreementNumber();
    }

    /**
     * Validate agreement parameters with product parameters
     *
     * @param agreementDto agreementDto with agreement parameters
     * @param product      product to validate with
     * @return true if validation succeeded false else
     */
    private boolean isValidAgreementParameters(AgreementDto agreementDto, Product product) {
        BigDecimal principalAmount = agreementDto.disbursementAmount().add(agreementDto.originationAmount());

        return principalAmount.compareTo(product.getPrincipal_amount_min()) >= 0 &&
                principalAmount.compareTo(product.getPrincipal_amount_max()) <= 0 &&
                agreementDto.originationAmount().compareTo(product.getOrigination_amount_min()) >= 0 &&
                agreementDto.originationAmount().compareTo(product.getOrigination_amount_max()) <= 0 &&
                agreementDto.interest().compareTo(product.getInterest_min()) >= 0 &&
                agreementDto.interest().compareTo(product.getInterest_max()) <= 0 &&
                agreementDto.loanTerm() >= product.getLoan_term_min() &&
                agreementDto.loanTerm() <= product.getLoan_term_max();
    }

    /**
     * Provides agreement activation.
     * Generate first payment schedule and its payments and saves them to database.
     * Sets disbursementDate, nextPaymentDate and status to {@link AgreementStatus#ACTIVE}
     *
     * @param agreementActivationDto agreementActivationDto
     * @return first payment schedule with all payments
     * @throws InvalidParametersException if agreement is already active
     */
    @Override
    @Transactional
    public PaymentScheduleDto activateAgreement(AgreementActivationDto agreementActivationDto) {
        Agreement agreement = agreementService.getAgreement(agreementActivationDto.agreementNumber());

        if (agreement.getStatus().equals(AgreementStatus.ACTIVE)) {
            throw new InvalidParametersException("Agreement is already activated");
        }

        agreement.setDisbursementDate(agreementActivationDto.disbursementDate());

        PaymentSchedule firstPaymentSchedule = this.generatePaymentSchedule(agreement);
        List<PaymentSchedule> paymentSchedules = agreement.getPaymentSchedules();
        paymentSchedules.add(firstPaymentSchedule);

        agreement.setPaymentSchedules(paymentSchedules);
        agreement.setNextPaymentDate(this.getNextPaymentDate(firstPaymentSchedule));
        agreement.setStatus(AgreementStatus.ACTIVE);

        agreementService.saveAgreement(agreement);

        return agreementMapper.toPaymentScheduleDto(firstPaymentSchedule);
    }

    /**
     * Gets next payment date with status {@link PaymentStatus#FUTURE} from current payment schedule
     *
     * @param paymentSchedule payment schedule to get payment date from
     * @return next payment date if exists
     * @throws NotFoundException if next payment not exists
     */
    private LocalDateTime getNextPaymentDate(PaymentSchedule paymentSchedule) {
        return paymentSchedule.getPayments()
                .stream()
                .filter(p -> p.getStatus().equals(PaymentStatus.FUTURE))
                .min(Comparator.comparing(PaymentSchedulePayment::getPaymentDate))
                .orElseThrow(() -> new NotFoundException("Next payment not found"))
                .getPaymentDate();
    }

    /**
     * Provides payment schedule generating.
     *
     * @param agreement agreement generate payment schedule for
     * @return payment schedule with generated payments
     */
    private PaymentSchedule generatePaymentSchedule(Agreement agreement) {
        int version = agreement.getPaymentSchedules().size() + 1;

        PaymentSchedule paymentSchedule = PaymentSchedule.builder()
                .agreement(agreement)
                .version(version)
                .build();

        List<PaymentSchedulePayment> paymentSchedulePayments = generatePaymentSchedulePayments(paymentSchedule);
        paymentSchedule.setPayments(paymentSchedulePayments);

        return paymentSchedule;
    }

    /**
     * Generates payment schedule payments for each period.
     * Uses {@link PaymentScheduleCalculations} to calculate payments values.
     *
     * @param paymentSchedule generating payments for this payment schedule
     * @return List of {@link PaymentSchedulePayment payments}
     */
    private List<PaymentSchedulePayment> generatePaymentSchedulePayments(PaymentSchedule paymentSchedule) {
        Agreement agreement = paymentSchedule.getAgreement();
        List<PaymentSchedulePayment> paymentSchedulePayments = new ArrayList<>();

        LocalDateTime paymentDate = agreement.getDisbursementDate();
        BigDecimal balance = agreement.getPrincipalAmount();
        BigDecimal periodPayment = PaymentScheduleCalculations.calculatePMT(agreement.getPrincipalAmount(), agreement.getInterest(), agreement.getLoanTerm());

        for (int i = 0; i < agreement.getLoanTerm(); i++) {
            int period = i + 1;
            BigDecimal interestPayment = PaymentScheduleCalculations.calculateIPMT(agreement.getPrincipalAmount(), agreement.getInterest(), periodPayment, period);

            //If it is last period payment do rounding for last payment
            BigDecimal principalPayment = PaymentScheduleCalculations.calculatePPMT(periodPayment, interestPayment);
            if (period == agreement.getLoanTerm() && !principalPayment.equals(balance)) {
                principalPayment = balance;
                periodPayment = principalPayment.add(interestPayment);
            }

            balance = balance.subtract(principalPayment);
            paymentDate = PaymentScheduleCalculations.calculateNextPaymentDate(paymentDate);

            paymentSchedulePayments.add(PaymentSchedulePayment.builder()
                    .paymentSchedule(paymentSchedule)
                    .status(PaymentStatus.FUTURE)
                    .periodPayment(periodPayment)
                    .interestPayment(interestPayment)
                    .principalPayment(principalPayment)
                    .balance(balance)
                    .paymentDate(paymentDate)
                    .periodNumber(period)
                    .build()
            );
        }

        return paymentSchedulePayments;
    }
}
