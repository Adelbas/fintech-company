package com.academy.fintech.origination.core.service.email;

import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import com.academy.fintech.origination.public_interface.application.dto.ApplicationEmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Represents email service implementation.
 * Uses {@link JavaMailSender} to send emails.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final String APPLICATION_APPROVED_TEXT = "%s %s, your application %s has been approved.";
    private static final String APPLICATION_REJECTED_TEXT = "%s %s, your application %s has been rejected.";
    private static final String APPLICATION_SUBJECT = "Application review result";

    private final JavaMailSender emailSender;

    @Override
    public void sendApplicationApprovedEmail(ApplicationEmailDto applicationEmailDto) {
        sendApplicationEmail(applicationEmailDto, APPLICATION_APPROVED_TEXT);
    }

    @Override
    public void sendApplicationRejectedEmail(ApplicationEmailDto applicationEmailDto) {
        sendApplicationEmail(applicationEmailDto, APPLICATION_REJECTED_TEXT);
    }

    private void sendApplicationEmail(ApplicationEmailDto applicationEmailDto, String messageTextTemplate) {
        log.info("Sending application review result email to {}", applicationEmailDto.email());
        String messageText = String.format(
                messageTextTemplate,
                applicationEmailDto.firstName(),
                applicationEmailDto.lastName(),
                applicationEmailDto.applicationId()
        );
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(applicationEmailDto.email());
        simpleMailMessage.setSubject(APPLICATION_SUBJECT);
        simpleMailMessage.setText(messageText);

        try {
//            emailSender.send(simpleMailMessage);
        } catch (MailSendException e) {
            log.error("Error sending email to {} :", applicationEmailDto.email(), e);
        }
    }
}
