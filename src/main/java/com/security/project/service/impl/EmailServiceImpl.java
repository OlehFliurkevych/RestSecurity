package com.security.project.service.impl;

import com.security.project.exception.GeneralServiceException;
import com.security.project.repository.UserRepository;
import com.security.project.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {


    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private JavaMailSender mailSender;

//    @Value("${mail.encoding.options}")
//    private String ENCODING_OPTIONS;
//
//    @Value("${mail.header.param}")
//    private String HEADER_PARAM;
//
//    @Value("${email.user.mail}")
//    private String EMAIL_TO_SEND;
//
//    @Value("${spring.mail.username}")
//    private String EMAIL_FROM_SEND;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public void sendHtmlEmail(String to, String subject, String body) {
        try {
            final MimeMessage message = mailSender.createMimeMessage();
            message.setHeader("text/html; charset=UTF-8", "text/html; charset=UTF-8");
            message.setFrom(new InternetAddress("oleh0809@gmail.com"));
            message.saveChanges();
            final MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo("oleh.fliurkevych@gmail.com");
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
}
