package com.security.project.service.impl;

import com.security.project.exception.GeneralServiceException;
import com.security.project.repository.UserRepository;
import com.security.project.service.EmailService;
import com.security.project.utils.EmailGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

//    private final MailProperties mailProperties;
//    private final Configuration templates;
//
//    @Autowired
//    public EmailServiceImpl(MailProperties mailProperties, Configuration templates) {
//        this.mailProperties = mailProperties;
//        this.templates = templates;
//    }

    @Value("${mail.header.param}")
    private String HEADER_PARAM;

    @Value("${mail.encoding.options}")
    private String ENCODING_OPTIONS;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Value("${email.verification}")
    private String VERIFICATION;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public void sendHtmlEmail(String to, String subject, String body) {
        try {
            final MimeMessage message = mailSender.createMimeMessage();
            message.setHeader(HEADER_PARAM, ENCODING_OPTIONS);
            message.saveChanges();
            final MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
            LOGGER.debug("Send e-mail to '{}' with subject '{}' and content={}",
                    to, subject, body);
        } catch (final MessagingException e) {
            LOGGER.error("Failed to send e-mail to '{}' with subject '{}' and content={}",
                    to, subject, body);
            throw new GeneralServiceException("Error with sending HTML mail ", e);
        }
    }


    @Override
    public void sendVerificationEmail(String name, String email, String token) {
        final String body = EmailGenerator.generateVerificationMail(name,
                token,
                VERIFICATION);
        sendHtmlEmail(email, "Verification account", body);
    }
}
