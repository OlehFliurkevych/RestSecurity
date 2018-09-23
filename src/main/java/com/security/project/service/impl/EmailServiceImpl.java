package com.security.project.service.impl;

import com.security.project.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {


    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public JavaMailSender mailSender;


    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public void sendHtmlEmail(String to, String subject, String body) {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }



//    @Override
//    public void sendHtmlEmail(String to, String subject, String body) {
//        try {
//            final MimeMessage message = mailSender.createMimeMessage();
//            message.setHeader("text/html; charset=UTF-8", "text/html; charset=UTF-8");
//            message.setFrom(new InternetAddress("oleh0809@gmail.com"));
//            message.saveChanges();
//            final MimeMessageHelper helper = new MimeMessageHelper(message);
//            helper.setTo("oleh.fliurkevych@gmail.com");
//            helper.setSubject(subject);
//            helper.setText(body, true);
//            mailSender.send(message);
//            LOGGER.debug("Send e-mail to '{}' with subject '{}' and content={}",
//                    to, subject, body);
//        } catch (final MessagingException e) {
//            LOGGER.error("Failed to send e-mail to '{}' with subject '{}' and content={}",
//                    to, subject, body);
//            throw new GeneralServiceException("Error with sending HTML mail ", e);
//        }
//    }
}
